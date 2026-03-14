package com.streetLeague.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class SeanceExerciceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull(message = "L'identifiant de la séance est obligatoire")
        private Integer seanceId;

        @NotNull(message = "L'identifiant de l'exercice est obligatoire")
        private Integer exerciceId;

        @Min(value = 1, message = "Le nombre de séries doit être au moins 1")
        private Integer series;

        @Min(value = 1, message = "Le nombre de répétitions doit être au moins 1")
        private Integer repetitions;

        @Min(value = 0, message = "La charge ne peut pas être négative")
        private Float charge;

        @Min(value = 1, message = "Le temps doit être au moins 1 seconde")
        private Integer tempsSecondes;

        @Min(value = 1, message = "L'ordre doit être au moins 1")
        private Integer ordre;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idSeanceExercice;
        private Integer seanceId;
        private Integer exerciceId;
        private String exerciceNom;
        private Integer series;
        private Integer repetitions;
        private Float charge;
        private Integer tempsSecondes;
        private Integer ordre;
    }
}
