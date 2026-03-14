package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.Intensite;
import com.streetLeague.backend.enums.StatutSeance;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class SeanceEntrainementDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Le titre de la séance est obligatoire")
        @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
        private String titreSeance;

        @NotNull(message = "La date de la séance est obligatoire")
        private LocalDate dateSeance;

        @Min(value = 1, message = "La durée doit être d'au moins 1 minute")
        @Max(value = 300, message = "La durée ne peut pas dépasser 300 minutes")
        private Integer dureeMinutes;

        private Intensite intensite;
        private StatutSeance statut;

        @NotNull(message = "Le programme est obligatoire")
        private Integer programmeId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idSeance;
        private String titreSeance;
        private LocalDate dateSeance;
        private Integer dureeMinutes;
        private Intensite intensite;
        private StatutSeance statut;
        private Integer programmeId;
        private String programmeTitre;
        private List<SeanceExerciceDTO.Response> exercices;
        private SuiviSeanceDTO.Response suiviSeance;
    }
}
