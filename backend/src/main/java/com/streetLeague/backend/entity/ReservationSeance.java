package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.ModePaiement;
import com.streetLeague.backend.enums.StatutPaiement;
import com.streetLeague.backend.enums.StatutReservationSeance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Réservation d'un sportif pour une séance de coaching.
 * Un sportif réserve sa place dans une séance — le coach peut confirmer ou annuler.
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationSeance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservation;

    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    private StatutReservationSeance statut;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    /* Step 6 : statut du paiement */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutPaiement statutPaiement = StatutPaiement.EN_ATTENTE;

    private Double montant;

    private String motifAnnulation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;
}
