package com.streetLeague.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SuiviSeance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSuivi;

    private LocalDateTime dateValidation;
    private Integer ressenti;
    private Integer fatigue;
    private String commentaire;

    /* Step 7 : note (1-5 étoiles) et auteur du feedback */
    private Integer note;

    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private User auteur;

    @OneToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;
}
