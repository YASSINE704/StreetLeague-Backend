package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.*;
import com.streetLeague.backend.entity.*;

public class ExerciceMapper {

    private ExerciceMapper() {}

    public static Exercice toEntity(ExerciceDTO.Request dto) {
        return Exercice.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .type(dto.getType())
                .videoUrl(dto.getVideoUrl())
                .build();
    }

    public static ExerciceDTO.Response toResponse(Exercice entity) {
        return ExerciceDTO.Response.builder()
                .idExercice(entity.getIdExercice())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .type(entity.getType())
                .videoUrl(entity.getVideoUrl())
                .build();
    }

    public static SeanceExerciceDTO.Response toSeanceExerciceResponse(SeanceExercice entity) {
        return SeanceExerciceDTO.Response.builder()
                .idSeanceExercice(entity.getIdSeanceExercice())
                .seanceId(entity.getSeance().getIdSeance())
                .seanceTitre(entity.getSeance().getTitreSeance())
                .exerciceId(entity.getExercice().getIdExercice())
                .exerciceNom(entity.getExercice().getNom())
                .series(entity.getSeries())
                .repetitions(entity.getRepetitions())
                .charge(entity.getCharge())
                .tempsSecondes(entity.getTempsSecondes())
                .ordre(entity.getOrdre())
                .build();
    }
}
