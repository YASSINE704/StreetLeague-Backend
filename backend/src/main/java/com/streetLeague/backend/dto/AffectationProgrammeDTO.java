package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.TypeAffectationProgramme;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

public class AffectationProgrammeDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull
        @Positive(message = "L'identifiant du programme doit être positif")
        private Integer programmeId;
        @NotNull
        @Positive(message = "L'identifiant de l'utilisateur doit être positif")
        private Integer userId;
        @NotNull(message = "Le type d'affectation est obligatoire")
        private TypeAffectationProgramme type;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idAffectation;
        private TypeAffectationProgramme type;
        private LocalDateTime dateAffectation;
        private Integer userId;
        private String userNom;
        private Integer programmeId;
    }
}
