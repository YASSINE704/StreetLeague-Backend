package com.streetLeague.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class SuiviSeanceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull(message = "L'identifiant de la séance est obligatoire")
        private Integer seanceId;

        @NotNull(message = "Le ressenti est obligatoire")
        @Min(value = 1, message = "Le ressenti doit être entre 1 et 10")
        @Max(value = 10, message = "Le ressenti doit être entre 1 et 10")
        private Integer ressenti;

        @NotNull(message = "La fatigue est obligatoire")
        @Min(value = 1, message = "La fatigue doit être entre 1 et 10")
        @Max(value = 10, message = "La fatigue doit être entre 1 et 10")
        private Integer fatigue;

        @Size(max = 500, message = "Le commentaire ne doit pas dépasser 500 caractères")
        private String commentaire;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idSuivi;
        private LocalDateTime dateValidation;
        private Integer ressenti;
        private Integer fatigue;
        private String commentaire;
        private Integer seanceId;
    }
}
