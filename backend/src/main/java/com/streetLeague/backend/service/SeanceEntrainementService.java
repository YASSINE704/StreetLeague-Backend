package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.SeanceEntrainementDTO;
import com.streetLeague.backend.entity.ProgrammeEntrainement;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.enums.StatutProgramme;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.SeanceMapper;
import com.streetLeague.backend.repository.SeanceEntrainementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeanceEntrainementService {

    private final SeanceEntrainementRepository seanceRepository;
    private final ProgrammeEntrainementService programmeService;

    public SeanceEntrainementDTO.Response create(SeanceEntrainementDTO.Request dto) {
        ProgrammeEntrainement programme = programmeService.findOrThrow(dto.getProgrammeId());

        // Règle métier : impossible de créer une séance si programme TERMINE
        if (programme.getStatut() == StatutProgramme.TERMINE) {
            throw new BusinessRuleException("Impossible de créer une séance pour un programme terminé");
        }

        // Règle métier : la date de la séance doit être dans l'intervalle du programme
        validateDateInProgrammeRange(dto.getDateSeance(), programme);

        SeanceEntrainement seance = SeanceMapper.toEntity(dto);
        seance.setProgramme(programme);
        return SeanceMapper.toResponse(seanceRepository.save(seance));
    }

    @Transactional(readOnly = true)
    public List<SeanceEntrainementDTO.Response> getAll() {
        return seanceRepository.findAll().stream().map(SeanceMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SeanceEntrainementDTO.Response getById(Integer id) {
        return SeanceMapper.toResponse(findOrThrow(id));
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

        // Règle métier : la date de la séance doit être dans l'intervalle du programme
        ProgrammeEntrainement currentProgramme = seance.getProgramme();
        if (dto.getProgrammeId() != null && !dto.getProgrammeId().equals(currentProgramme.getIdProgramme())) {
            currentProgramme = programmeService.findOrThrow(dto.getProgrammeId());
            seance.setProgramme(currentProgramme);
        }
        validateDateInProgrammeRange(dto.getDateSeance(), currentProgramme);

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
}
