package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.*;
import com.streetLeague.backend.entity.*;
import com.streetLeague.backend.enums.StatutSeance;

import java.util.Collections;
import java.util.List;

public class SeanceMapper {

    private SeanceMapper() {}

    public static SeanceEntrainement toEntity(SeanceEntrainementDTO.Request dto) {
        return SeanceEntrainement.builder()
                .titreSeance(dto.getTitreSeance())
                .dateSeance(dto.getDateSeance())
                .dureeMinutes(dto.getDureeMinutes())
                .intensite(dto.getIntensite())
                .statut(dto.getStatut() != null ? dto.getStatut() : StatutSeance.PREVUE)
                .build();
    }

    public static SeanceEntrainementDTO.Response toResponse(SeanceEntrainement entity) {
        List<SeanceExerciceDTO.Response> exercices = entity.getSeanceExercices() != null
                ? entity.getSeanceExercices().stream().map(ExerciceMapper::toSeanceExerciceResponse).toList()
                : Collections.emptyList();

        SuiviSeanceDTO.Response suivi = entity.getSuiviSeance() != null
                ? SuiviMapper.toResponse(entity.getSuiviSeance())
                : null;

        return SeanceEntrainementDTO.Response.builder()
                .idSeance(entity.getIdSeance())
                .titreSeance(entity.getTitreSeance())
                .dateSeance(entity.getDateSeance())
                .dureeMinutes(entity.getDureeMinutes())
                .intensite(entity.getIntensite())
                .statut(entity.getStatut())
                .programmeId(entity.getProgramme().getIdProgramme())
                .programmeTitre(entity.getProgramme().getTitre())
                .exercices(exercices)
                .suiviSeance(suivi)
                .build();
    }
}
