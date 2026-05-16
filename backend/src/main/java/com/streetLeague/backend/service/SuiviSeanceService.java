package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.SuiviSeanceDTO;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SuiviSeance;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.enums.TypeAffectationProgramme;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.SuiviMapper;
import com.streetLeague.backend.repository.AffectationProgrammeRepository;
import com.streetLeague.backend.repository.ReservationSeanceRepository;
import com.streetLeague.backend.repository.SuiviSeanceRepository;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SuiviSeanceService {

    private final SuiviSeanceRepository suiviRepository;
    private final SeanceEntrainementService seanceService;
    private final UserRepository userRepository;
    private final ReservationSeanceRepository reservationRepository;
    private final AffectationProgrammeRepository affectationRepository;

    public SuiviSeanceDTO.Response create(SuiviSeanceDTO.Request dto, Integer userId) {
        SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());

        // Règle : la séance doit être REALISEE pour donner un feedback
        if (seance.getStatut() != StatutSeance.REALISEE) {
            throw new BusinessRuleException(
                    "Le feedback ne peut être donné que pour une séance terminée (statut REALISEE). Statut actuel: " + seance.getStatut());
        }

        User auteur = null;
        if (userId != null) {
            auteur = userRepository.findById(userId).orElse(null);
        }

        // Règle : vérifier que le SPORTIF a bien participé (a une réservation non annulée)
        if (auteur != null && auteur.getRole() == Role.SPORTIF) {
            boolean aParticipe = reservationRepository
                    .findByUserIdUserAndSeanceIdSeanceAndStatutNot(userId, dto.getSeanceId(), StatutReservationSeance.ANNULEE)
                    .isPresent();
            if (!aParticipe) {
                throw new BusinessRuleException(
                        "Vous ne pouvez pas donner de feedback : vous n'avez pas participé à cette séance");
            }
        }

        // Règle : un utilisateur ne peut pas donner 2 feedbacks pour la même séance
        if (userId != null) {
            suiviRepository.findBySeanceIdSeanceAndAuteurIdUser(dto.getSeanceId(), userId)
                    .ifPresent(existing -> {
                        throw new BusinessRuleException("Vous avez déjà donné un feedback pour cette séance");
                    });
        }

        SuiviSeance suivi = SuiviSeance.builder()
                .seance(seance)
                .dateValidation(LocalDateTime.now())
                .ressenti(dto.getRessenti())
                .fatigue(dto.getFatigue())
                .commentaire(dto.getCommentaire())
                .note(dto.getNote())
                .auteur(auteur)
                .build();
        return SuiviMapper.toResponse(suiviRepository.save(suivi));
    }

    /**
     * Retourne les suivis filtrés par le coach connecté.
     * Un coach ne voit que les feedbacks de ses propres séances/programmes.
     * Un ADMIN voit tout.
     */
    @Transactional(readOnly = true)
    public List<SuiviSeanceDTO.Response> getAllForCoach(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        // ADMIN voit tout
        if (user != null && user.getRole() == Role.ADMIN) {
            return suiviRepository.findAll().stream().map(SuiviMapper::toResponse).toList();
        }

        // COACH : filtrer par ses programmes
        if (user != null && user.getRole() == Role.COACH) {
            // Trouver tous les programmes où ce coach est affecté
            List<Integer> mesProgrammeIds = affectationRepository
                    .findByUserIdUserAndType(userId, TypeAffectationProgramme.COACH)
                    .stream()
                    .map(aff -> aff.getProgramme().getIdProgramme())
                    .collect(Collectors.toList());

            // Filtrer les suivis dont la séance appartient à un de ces programmes
            return suiviRepository.findAll().stream()
                    .filter(s -> s.getSeance() != null
                            && s.getSeance().getProgramme() != null
                            && mesProgrammeIds.contains(s.getSeance().getProgramme().getIdProgramme()))
                    .map(SuiviMapper::toResponse)
                    .toList();
        }

        // Autres rôles : liste vide
        return List.of();
    }

    @Transactional(readOnly = true)
    public List<SuiviSeanceDTO.Response> getAll() {
        return suiviRepository.findAll().stream().map(SuiviMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SuiviSeanceDTO.Response getById(Integer id) {
        return SuiviMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<SuiviSeanceDTO.Response> getBySeance(Integer seanceId) {
        return suiviRepository.findAllBySeanceIdSeance(seanceId).stream()
                .map(SuiviMapper::toResponse).toList();
    }

    public SuiviSeanceDTO.Response update(Integer id, SuiviSeanceDTO.Request dto) {
        SuiviSeance suivi = findOrThrow(id);
        suivi.setRessenti(dto.getRessenti());
        suivi.setFatigue(dto.getFatigue());
        suivi.setCommentaire(dto.getCommentaire());
        if (dto.getNote() != null) suivi.setNote(dto.getNote());
        return SuiviMapper.toResponse(suiviRepository.save(suivi));
    }

    public void delete(Integer id) {
        findOrThrow(id);
        suiviRepository.deleteById(id);
    }

    private SuiviSeance findOrThrow(Integer id) {
        return suiviRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suivi non trouvé avec id: " + id));
    }
}
