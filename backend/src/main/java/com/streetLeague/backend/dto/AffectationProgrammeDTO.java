package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.TypeAffectationProgramme;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class AffectationProgrammeDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull
        private Integer programmeId;
        @NotNull
        private Integer userId;
        @NotNull
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
