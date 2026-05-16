package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.SuiviSeanceDTO;
import com.streetLeague.backend.entity.SuiviSeance;

public class SuiviMapper {

    private SuiviMapper() {}

    public static SuiviSeanceDTO.Response toResponse(SuiviSeance entity) {
        return SuiviSeanceDTO.Response.builder()
                .idSuivi(entity.getIdSuivi())
                .dateValidation(entity.getDateValidation())
                .ressenti(entity.getRessenti())
                .fatigue(entity.getFatigue())
                .commentaire(entity.getCommentaire())
                .note(entity.getNote())
                .auteurId(entity.getAuteur() != null ? entity.getAuteur().getIdUser() : null)
                .auteurNom(entity.getAuteur() != null
                        ? entity.getAuteur().getNom() + " " + entity.getAuteur().getPrenom() : null)
                .seanceId(entity.getSeance().getIdSeance())
                .build();
    }
}
