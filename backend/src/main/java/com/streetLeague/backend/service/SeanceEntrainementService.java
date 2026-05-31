package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.SeanceEntrainementDTO;
import com.streetLeague.backend.entity.AffectationProgramme;
import com.streetLeague.backend.entity.ProgrammeEntrainement;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutProgramme;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.enums.TypeAffectationProgramme;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.SeanceMapper;
import com.streetLeague.backend.repository.AffectationProgrammeRepository;
import com.streetLeague.backend.repository.SeanceEntrainementRepository;
import com.streetLeague.backend.repository.SousEspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeanceEntrainementService {

    private final SeanceEntrainementRepository seanceRepository;
    private final ProgrammeEntrainementService programmeService;
    private final AffectationProgrammeRepository affectationRepository;
    private final SousEspaceRepository sousEspaceRepository;
    private final WeatherService weatherService;

    public SeanceEntrainementDTO.Response create(SeanceEntrainementDTO.Request dto) {
        ProgrammeEntrainement programme = programmeService.findOrThrow(dto.getProgrammeId());

        // Règle métier : impossible de créer une séance si programme TERMINE
        if (programme.getStatut() == StatutProgramme.TERMINE) {
            throw new BusinessRuleException("Impossible de créer une séance pour un programme terminé");
        }

        // Règle métier : la date de la séance doit être dans l'intervalle du programme
        validateDateInProgrammeRange(dto.getDateSeance(), programme);

        /* Step 8 : valider que heureDebut < heureFin */
        validateTimeRange(dto.getHeureDebut(), dto.getHeureFin());        /* Step 3 : vérifier les conflits d'emploi du temps du coach */
        validateCoachScheduleConflict(programme, dto.getDateSeance(), dto.getHeureDebut(), dto.getHeureFin(), null);

        SeanceEntrainement seance = SeanceMapper.toEntity(dto);
        seance.setProgramme(programme);

        /* Step 4 : lier le lieu (SousEspace) si fourni */
        if (dto.getSousEspaceId() != null) {
            SousEspace lieu = sousEspaceRepository.findById(dto.getSousEspaceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lieu non trouvé avec id: " + dto.getSousEspaceId()));
            seance.setLieu(lieu);
            /* Step 4 : vérifier que le lieu n'est pas déjà réservé au même créneau */
            validateLocationConflict(dto.getSousEspaceId(), dto.getDateSeance(), dto.getHeureDebut(), dto.getHeureFin(), null);
        }

        return SeanceMapper.toResponse(seanceRepository.save(seance));
    }

    @Transactional(readOnly = true)
    public List<SeanceEntrainementDTO.Response> getAll() {
        return seanceRepository.findAll().stream().map(SeanceMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SeanceEntrainementDTO.Response getById(Integer id) {
        SeanceEntrainement seance = findOrThrow(id);
        SeanceEntrainementDTO.Response response = SeanceMapper.toResponse(seance);
        // Ajouter l'alerte météo si séance en plein air
        if (Boolean.TRUE.equals(seance.getEnPleinAir()) && seance.getLieu() != null) {
            SousEspace lieu = seance.getLieu();
            Double lat = lieu.getEndroit() != null ? lieu.getEndroit().getLatitude() : 36.8065;
            Double lon = lieu.getEndroit() != null ? lieu.getEndroit().getLongitude() : 10.1815;
            WeatherService.WeatherInfo weather = weatherService.getWeatherForecast(lat, lon);
            if (weather != null) {
                String alert = weatherService.getWeatherRecommendation(weather);
                if (alert != null) {
                    response.setWeatherAlert(alert);
                } else {
                    response.setWeatherAlert("✅ Météo favorable : " + weather.description() + " (" + String.format("%.1f", weather.temperature()) + "°C)");
                }
            }
        }
        return response;
    }

    @Transactional(readOnly = true)
    public List<SeanceEntrainementDTO.Response> getByProgramme(Integer programmeId) {
        return seanceRepository.findByProgrammeIdProgramme(programmeId).stream()
                .map(SeanceMapper::toResponse).toList();
    }

    public SeanceEntrainementDTO.Response update(Integer id, SeanceEntrainementDTO.Request dto) {
        SeanceEntrainement seance = findOrThrow(id);

        // Règle métier : impossible de modifier une séance REALISEE
        if (seance.getStatut() == StatutSeance.REALISEE) {
            throw new BusinessRuleException("Impossible de modifier une séance déjà réalisée");
        }

        seance.setTitreSeance(dto.getTitreSeance());
        seance.setDateSeance(dto.getDateSeance());
        seance.setDureeMinutes(dto.getDureeMinutes());
        seance.setIntensite(dto.getIntensite());

        /* Step 2 : mise à jour des champs horaire et capacité */
        if (dto.getHeureDebut() != null) seance.setHeureDebut(dto.getHeureDebut());
        if (dto.getHeureFin() != null) seance.setHeureFin(dto.getHeureFin());
        if (dto.getMaxParticipants() != null) seance.setMaxParticipants(dto.getMaxParticipants());

        /* Step 4 : mise à jour du lieu */
        if (dto.getSousEspaceId() != null) {
            SousEspace lieu = sousEspaceRepository.findById(dto.getSousEspaceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lieu non trouvé avec id: " + dto.getSousEspaceId()));
            seance.setLieu(lieu);
        }
        if (dto.getEnPleinAir() != null) seance.setEnPleinAir(dto.getEnPleinAir());

        // Règle métier : la date de la séance doit être dans l'intervalle du programme
        ProgrammeEntrainement currentProgramme = seance.getProgramme();
        if (dto.getProgrammeId() != null && !dto.getProgrammeId().equals(currentProgramme.getIdProgramme())) {
            currentProgramme = programmeService.findOrThrow(dto.getProgrammeId());
            seance.setProgramme(currentProgramme);
        }
        validateDateInProgrammeRange(dto.getDateSeance(), currentProgramme);

        /* Step 3 : vérifier les conflits d'emploi du temps du coach */
        LocalTime newDebut = dto.getHeureDebut() != null ? dto.getHeureDebut() : seance.getHeureDebut();
        LocalTime newFin = dto.getHeureFin() != null ? dto.getHeureFin() : seance.getHeureFin();
        validateCoachScheduleConflict(currentProgramme, dto.getDateSeance(), newDebut, newFin, seance.getIdSeance());

        /* Step 4 : vérifier conflit de lieu */
        Long lieuId = dto.getSousEspaceId() != null ? dto.getSousEspaceId()
                : (seance.getLieu() != null ? seance.getLieu().getId() : null);
        if (lieuId != null) {
            validateLocationConflict(lieuId, dto.getDateSeance(), newDebut, newFin, seance.getIdSeance());
        }

        if (dto.getStatut() != null) {
            // Règle métier : séance ne peut passer à REALISEE que si elle a au moins 1 exercice
            if (dto.getStatut() == StatutSeance.REALISEE
                    && (seance.getSeanceExercices() == null || seance.getSeanceExercices().isEmpty())) {
                throw new BusinessRuleException(
                        "Impossible de marquer la séance comme réalisée : aucun exercice associé");
            }
            seance.setStatut(dto.getStatut());
        }
        return SeanceMapper.toResponse(seanceRepository.save(seance));
    }

    public void delete(Integer id) {
        findOrThrow(id);
        seanceRepository.deleteById(id);
    }

    public SeanceEntrainement findOrThrow(Integer id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée avec id: " + id));
    }

    private void validateDateInProgrammeRange(LocalDate dateSeance, ProgrammeEntrainement programme) {
        if (dateSeance == null || programme.getDateDebut() == null || programme.getDateFin() == null) {
            return;
        }
        if (dateSeance.isBefore(programme.getDateDebut()) || dateSeance.isAfter(programme.getDateFin())) {
            throw new BusinessRuleException(
                    "La date de la séance doit être comprise entre le "
                    + programme.getDateDebut() + " et le " + programme.getDateFin()
                    + " (dates du programme « " + programme.getTitre() + " »)");
        }
    }

    /**
     * Step 3 : Vérifie qu'un coach n'a pas déjà une séance au même créneau horaire.
     * On cherche le coach affecté au programme, puis on vérifie toutes ses séances
     * dans tous ses programmes pour détecter un chevauchement.
     *
     * @param programme     le programme de la séance
     * @param dateSeance    la date de la séance
     * @param heureDebut    l'heure de début
     * @param heureFin      l'heure de fin
     * @param excludeSeanceId  ID de la séance à exclure (pour l'update, on exclut la séance elle-même)
     */
    private void validateCoachScheduleConflict(ProgrammeEntrainement programme,
                                                LocalDate dateSeance,
                                                LocalTime heureDebut,
                                                LocalTime heureFin,
                                                Integer excludeSeanceId) {
        // Si pas d'heure définie, on ne peut pas vérifier le chevauchement
        if (heureDebut == null || heureFin == null || dateSeance == null) {
            return;
        }

        // Trouver le coach affecté à ce programme
        var coachAffectation = affectationRepository
                .findByProgrammeIdProgrammeAndType(programme.getIdProgramme(), TypeAffectationProgramme.COACH);

        if (coachAffectation.isEmpty()) {
            return; // Pas de coach affecté, pas de conflit possible
        }

        Integer coachId = coachAffectation.get().getUser().getIdUser();

        // Trouver tous les programmes où ce coach est affecté
        List<AffectationProgramme> coachProgrammes = affectationRepository
                .findByUserIdUserAndType(coachId, TypeAffectationProgramme.COACH);

        // Pour chaque programme du coach, vérifier les séances à la même date
        for (AffectationProgramme aff : coachProgrammes) {
            List<SeanceEntrainement> seancesMemeJour = seanceRepository
                    .findByProgrammeIdProgrammeAndDateSeance(aff.getProgramme().getIdProgramme(), dateSeance);

            for (SeanceEntrainement existante : seancesMemeJour) {
                // Exclure la séance en cours de modification
                if (excludeSeanceId != null && excludeSeanceId.equals(existante.getIdSeance())) {
                    continue;
                }

                // Vérifier le chevauchement horaire
                if (existante.getHeureDebut() != null && existante.getHeureFin() != null) {
                    if (heureDebut.isBefore(existante.getHeureFin()) && heureFin.isAfter(existante.getHeureDebut())) {
                        throw new BusinessRuleException(
                                "Conflit d'emploi du temps du coach : la séance « " + existante.getTitreSeance()
                                + " » est déjà planifiée le " + dateSeance
                                + " de " + existante.getHeureDebut() + " à " + existante.getHeureFin()
                                + " (programme « " + existante.getProgramme().getTitre() + " »)");
                    }
                }
            }
        }
    }

    /**
     * Step 4 : Vérifie qu'un lieu (SousEspace) n'est pas déjà utilisé par une autre séance
     * au même créneau horaire le même jour.
     */
    private void validateLocationConflict(Long sousEspaceId, LocalDate dateSeance,
                                           LocalTime heureDebut, LocalTime heureFin,
                                           Integer excludeSeanceId) {
        if (sousEspaceId == null || dateSeance == null || heureDebut == null || heureFin == null) {
            return;
        }

        // Chercher toutes les séances à cette date
        List<SeanceEntrainement> seancesMemeJour = seanceRepository.findByDateSeance(dateSeance);

        for (SeanceEntrainement existante : seancesMemeJour) {
            if (excludeSeanceId != null && excludeSeanceId.equals(existante.getIdSeance())) {
                continue;
            }
            // Même lieu ?
            if (existante.getLieu() == null || !existante.getLieu().getId().equals(sousEspaceId)) {
                continue;
            }
            // Chevauchement horaire ?
            if (existante.getHeureDebut() != null && existante.getHeureFin() != null) {
                if (heureDebut.isBefore(existante.getHeureFin()) && heureFin.isAfter(existante.getHeureDebut())) {
                    throw new BusinessRuleException(
                            "Conflit de lieu : le lieu « " + existante.getLieu().getNom()
                            + " » est déjà réservé le " + dateSeance
                            + " de " + existante.getHeureDebut() + " à " + existante.getHeureFin()
                            + " pour la séance « " + existante.getTitreSeance() + " »");
                }
            }
        }
    }

    /** Step 8 : Valide que heureDebut est avant heureFin */
    private void validateTimeRange(LocalTime heureDebut, LocalTime heureFin) {
        if (heureDebut != null && heureFin != null && !heureFin.isAfter(heureDebut)) {
            throw new BusinessRuleException("L'heure de fin doit être après l'heure de début");
        }
    }
}
