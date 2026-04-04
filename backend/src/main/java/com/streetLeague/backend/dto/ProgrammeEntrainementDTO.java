package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.StatutProgramme;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ProgrammeEntrainementDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Le titre est obligatoire")
        @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
        private String titre;

        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        private String description;

        @NotNull(message = "La date de début est obligatoire")
        private LocalDate dateDebut;

        @NotNull(message = "La date de fin est obligatoire")
        private LocalDate dateFin;

        private StatutProgramme statut;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idProgramme;
        private String titre;
        private String description;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private StatutProgramme statut;
        private List<SeanceEntrainementDTO.Response> seances;
        private List<AffectationProgrammeDTO.Response> affectations;
    }
}
