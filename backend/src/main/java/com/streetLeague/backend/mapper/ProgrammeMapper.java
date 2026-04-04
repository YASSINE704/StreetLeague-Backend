package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.*;
import com.streetLeague.backend.entity.*;
import com.streetLeague.backend.enums.StatutProgramme;

import java.util.Collections;
import java.util.List;

public class ProgrammeMapper {

    private ProgrammeMapper() {}

    public static ProgrammeEntrainement toEntity(ProgrammeEntrainementDTO.Request dto) {
        return ProgrammeEntrainement.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .statut(dto.getStatut() != null ? dto.getStatut() : StatutProgramme.BROUILLON)
                .build();
    }

    public static ProgrammeEntrainementDTO.Response toResponse(ProgrammeEntrainement entity) {
        List<SeanceEntrainementDTO.Response> seances = entity.getSeances() != null
                ? entity.getSeances().stream().map(SeanceMapper::toResponse).toList()
                : Collections.emptyList();

        List<AffectationProgrammeDTO.Response> affectations = entity.getAffectations() != null
                ? entity.getAffectations().stream().map(AffectationMapper::toResponse).toList()
                : Collections.emptyList();

        return ProgrammeEntrainementDTO.Response.builder()
                .idProgramme(entity.getIdProgramme())
                .titre(entity.getTitre())
                .description(entity.getDescription())
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .statut(entity.getStatut())
                .seances(seances)
                .affectations(affectations)
                .build();
    }
}
