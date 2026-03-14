package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.ProgrammeEntrainementDTO;
import com.streetLeague.backend.entity.ProgrammeEntrainement;
import com.streetLeague.backend.enums.StatutProgramme;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.ProgrammeMapper;
import com.streetLeague.backend.repository.ProgrammeEntrainementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgrammeEntrainementService {

    private final ProgrammeEntrainementRepository programmeRepository;

    public ProgrammeEntrainementDTO.Response create(ProgrammeEntrainementDTO.Request dto) {
        validateDates(dto);
        ProgrammeEntrainement programme = ProgrammeMapper.toEntity(dto);
        return ProgrammeMapper.toResponse(programmeRepository.save(programme));
    }

    @Transactional(readOnly = true)
    public List<ProgrammeEntrainementDTO.Response> getAll() {
        return programmeRepository.findAll().stream().map(ProgrammeMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProgrammeEntrainementDTO.Response getById(Integer id) {
        return ProgrammeMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ProgrammeEntrainementDTO.Response> getByStatut(StatutProgramme statut) {
        return programmeRepository.findByStatut(statut).stream().map(ProgrammeMapper::toResponse).toList();
    }

    public ProgrammeEntrainementDTO.Response update(Integer id, ProgrammeEntrainementDTO.Request dto) {
        ProgrammeEntrainement programme = findOrThrow(id);

        // Règle métier : impossible de modifier un programme TERMINE
        if (programme.getStatut() == StatutProgramme.TERMINE) {
            throw new BusinessRuleException("Impossible de modifier un programme terminé");
        }

        validateDates(dto);
        programme.setTitre(dto.getTitre());
        programme.setDescription(dto.getDescription());
        programme.setDateDebut(dto.getDateDebut());
        programme.setDateFin(dto.getDateFin());
        if (dto.getStatut() != null) {
            programme.setStatut(dto.getStatut());
        }
        return ProgrammeMapper.toResponse(programmeRepository.save(programme));
    }

    public void delete(Integer id) {
        findOrThrow(id);
        programmeRepository.deleteById(id);
    }

    public ProgrammeEntrainement findOrThrow(Integer id) {
        return programmeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé avec id: " + id));
    }

    private void validateDates(ProgrammeEntrainementDTO.Request dto) {
        if (dto.getDateDebut() != null && dto.getDateFin() != null
                && dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new BusinessRuleException("La date de fin doit être après la date de début");
        }
    }
}
