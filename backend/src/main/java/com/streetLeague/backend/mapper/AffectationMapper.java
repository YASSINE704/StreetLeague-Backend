package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.AffectationProgrammeDTO;
import com.streetLeague.backend.entity.AffectationProgramme;

public class AffectationMapper {

    private AffectationMapper() {}

    public static AffectationProgrammeDTO.Response toResponse(AffectationProgramme entity) {
        return AffectationProgrammeDTO.Response.builder()
                .idAffectation(entity.getIdAffectation())
                .type(entity.getType())
                .dateAffectation(entity.getDateAffectation())
                .userId(entity.getUser().getIdUser())
                .userNom(entity.getUser().getNom() + " " + entity.getUser().getPrenom())
                .programmeId(entity.getProgramme().getIdProgramme())
                .build();
    }
}
