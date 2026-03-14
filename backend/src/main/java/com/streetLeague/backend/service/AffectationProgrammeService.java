package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.AffectationProgrammeDTO;
import com.streetLeague.backend.entity.AffectationProgramme;
import com.streetLeague.backend.entity.ProgrammeEntrainement;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.TypeAffectationProgramme;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.AffectationMapper;
import com.streetLeague.backend.repository.AffectationProgrammeRepository;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AffectationProgrammeService {

    private final AffectationProgrammeRepository affectationRepository;
    private final ProgrammeEntrainementService programmeService;
    private final UserRepository userRepository;

    public AffectationProgrammeDTO.Response create(AffectationProgrammeDTO.Request dto) {
        ProgrammeEntrainement programme = programmeService.findOrThrow(dto.getProgrammeId());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User non trouvé avec id: " + dto.getUserId()));

        // Règle métier : exactement 1 COACH et 1 SPORTIF par programme
        if (affectationRepository.findByProgrammeIdProgrammeAndType(dto.getProgrammeId(), dto.getType()).isPresent()) {
            throw new BusinessRuleException(
                    "Une affectation de type " + dto.getType() + " existe déjà pour ce programme");
        }

        AffectationProgramme affectation = AffectationProgramme.builder()
                .type(dto.getType())
                .dateAffectation(LocalDateTime.now())
                .user(user)
                .programme(programme)
                .build();
        return AffectationMapper.toResponse(affectationRepository.save(affectation));
    }

    @Transactional(readOnly = true)
    public List<AffectationProgrammeDTO.Response> getByProgramme(Integer programmeId) {
        return affectationRepository.findByProgrammeIdProgramme(programmeId).stream()
                .map(AffectationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AffectationProgrammeDTO.Response> getByUser(Integer userId) {
        return affectationRepository.findByUserIdUser(userId).stream()
                .map(AffectationMapper::toResponse).toList();
    }

    public void delete(Integer id) {
        if (!affectationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Affectation non trouvée avec id: " + id);
        }
        affectationRepository.deleteById(id);
    }
}
