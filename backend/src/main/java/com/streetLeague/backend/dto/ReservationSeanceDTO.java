package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.ModePaiement;
import com.streetLeague.backend.enums.StatutPaiement;
import com.streetLeague.backend.enums.StatutReservationSeance;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class ReservationSeanceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull(message = "L'identifiant de la séance est obligatoire")
        private Integer seanceId;

        @NotNull(message = "Le mode de paiement est obligatoire")
        private ModePaiement modePaiement;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idReservation;
        private LocalDateTime dateReservation;
        private StatutReservationSeance statut;
        private ModePaiement modePaiement;
        private StatutPaiement statutPaiement;
        private Double montant;
        private String motifAnnulation;
        private Integer userId;
        private String userNom;
        private Integer seanceId;
        private String seanceTitre;
    }
}
