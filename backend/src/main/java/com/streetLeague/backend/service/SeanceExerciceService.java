package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.SeanceExerciceDTO;
import com.streetLeague.backend.entity.Exercice;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SeanceExercice;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.ExerciceMapper;
import com.streetLeague.backend.repository.ExerciceRepository;
import com.streetLeague.backend.repository.SeanceExerciceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeanceExerciceService {

    private final SeanceExerciceRepository seanceExerciceRepository;
    private final SeanceEntrainementService seanceService;
    private final ExerciceRepository exerciceRepository;

    public SeanceExerciceDTO.Response create(SeanceExerciceDTO.Request dto) {
        SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());
        Exercice exercice = exerciceRepository.findById(dto.getExerciceId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercice non trouvé avec id: " + dto.getExerciceId()));

        SeanceExercice se = SeanceExercice.builder()
                .seance(seance)
                .exercice(exercice)
                .series(dto.getSeries())
                .repetitions(dto.getRepetitions())
                .charge(dto.getCharge())
                .tempsSecondes(dto.getTempsSecondes())
                .ordre(dto.getOrdre())
                .build();
        return ExerciceMapper.toSeanceExerciceResponse(seanceExerciceRepository.save(se));
    }

    @Transactional(readOnly = true)
    public List<SeanceExerciceDTO.Response> getBySeance(Integer seanceId) {
        return seanceExerciceRepository.findBySeanceIdSeanceOrderByOrdreAsc(seanceId).stream()
                .map(ExerciceMapper::toSeanceExerciceResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<SeanceExerciceDTO.Response> getByExercice(Integer exerciceId) {
        return seanceExerciceRepository.findByExerciceIdExercice(exerciceId).stream()
                .map(ExerciceMapper::toSeanceExerciceResponse).toList();
    }

    public SeanceExerciceDTO.Response update(Integer id, SeanceExerciceDTO.Request dto) {
        SeanceExercice se = seanceExerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SeanceExercice non trouvé avec id: " + id));
        se.setSeries(dto.getSeries());
        se.setRepetitions(dto.getRepetitions());
        se.setCharge(dto.getCharge());
        se.setTempsSecondes(dto.getTempsSecondes());
        se.setOrdre(dto.getOrdre());
        return ExerciceMapper.toSeanceExerciceResponse(seanceExerciceRepository.save(se));
    }

    public void delete(Integer id) {
        if (!seanceExerciceRepository.existsById(id)) {
            throw new ResourceNotFoundException("SeanceExercice non trouvé avec id: " + id);
        }
        seanceExerciceRepository.deleteById(id);
    }
}
