package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.SuiviSeanceDTO;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SuiviSeance;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.SuiviMapper;
import com.streetLeague.backend.repository.SuiviSeanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SuiviSeanceService {

    private final SuiviSeanceRepository suiviRepository;
    private final SeanceEntrainementService seanceService;

    public SuiviSeanceDTO.Response create(SuiviSeanceDTO.Request dto) {
        SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());

        // Règle métier : SuiviSeance seulement si statut = REALISEE
        if (seance.getStatut() != StatutSeance.REALISEE) {
            throw new BusinessRuleException(
                    "Le suivi ne peut être créé que pour une séance avec statut REALISEE. Statut actuel: " + seance.getStatut());
        }

        // Vérifier qu'il n'existe pas déjà un suivi pour cette séance
        if (suiviRepository.findBySeanceIdSeance(dto.getSeanceId()).isPresent()) {
            throw new BusinessRuleException("Un suivi existe déjà pour la séance id: " + dto.getSeanceId());
        }

        SuiviSeance suivi = SuiviSeance.builder()
                .seance(seance)
                .dateValidation(LocalDateTime.now())
                .ressenti(dto.getRessenti())
                .fatigue(dto.getFatigue())
                .commentaire(dto.getCommentaire())
                .build();
        return SuiviMapper.toResponse(suiviRepository.save(suivi));
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
    public SuiviSeanceDTO.Response getBySeance(Integer seanceId) {
        return SuiviMapper.toResponse(suiviRepository.findBySeanceIdSeance(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Suivi non trouvé pour la séance id: " + seanceId)));
    }

    public SuiviSeanceDTO.Response update(Integer id, SuiviSeanceDTO.Request dto) {
        SuiviSeance suivi = findOrThrow(id);
        suivi.setRessenti(dto.getRessenti());
        suivi.setFatigue(dto.getFatigue());
        suivi.setCommentaire(dto.getCommentaire());
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
