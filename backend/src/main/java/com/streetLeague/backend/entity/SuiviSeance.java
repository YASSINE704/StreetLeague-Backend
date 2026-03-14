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

    @OneToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;
}
