package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.ExerciceDTO;
import com.streetLeague.backend.entity.Exercice;
import com.streetLeague.backend.enums.TypeExercice;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.ExerciceMapper;
import com.streetLeague.backend.repository.ExerciceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;

    public ExerciceDTO.Response create(ExerciceDTO.Request dto) {
        Exercice exercice = ExerciceMapper.toEntity(dto);
        return ExerciceMapper.toResponse(exerciceRepository.save(exercice));
    }

    public List<ExerciceDTO.Response> getAll() {
        return exerciceRepository.findAll().stream().map(ExerciceMapper::toResponse).toList();
    }

    public ExerciceDTO.Response getById(Integer id) {
        return ExerciceMapper.toResponse(findOrThrow(id));
    }

    public List<ExerciceDTO.Response> getByType(TypeExercice type) {
        return exerciceRepository.findByType(type).stream().map(ExerciceMapper::toResponse).toList();
    }

    public ExerciceDTO.Response update(Integer id, ExerciceDTO.Request dto) {
        Exercice exercice = findOrThrow(id);
        exercice.setNom(dto.getNom());
        exercice.setDescription(dto.getDescription());
        exercice.setType(dto.getType());
        exercice.setVideoUrl(dto.getVideoUrl());
        return ExerciceMapper.toResponse(exerciceRepository.save(exercice));
    }

    public void delete(Integer id) {
        findOrThrow(id);
        exerciceRepository.deleteById(id);
    }

    private Exercice findOrThrow(Integer id) {
        return exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice non trouvé avec id: " + id));
    }
}
