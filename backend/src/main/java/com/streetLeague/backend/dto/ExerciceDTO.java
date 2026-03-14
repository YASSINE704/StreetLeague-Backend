package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.TypeExercice;
import jakarta.validation.constraints.*;
import lombok.*;

public class ExerciceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Le nom de l'exercice est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
        private String nom;

        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        private String description;

        private TypeExercice type;

        @Size(max = 500, message = "L'URL vidéo ne doit pas dépasser 500 caractères")
        private String videoUrl;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idExercice;
        private String nom;
        private String description;
        private TypeExercice type;
        private String videoUrl;
    }
}
