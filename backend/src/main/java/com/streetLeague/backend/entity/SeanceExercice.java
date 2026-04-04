package com.streetLeague.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeanceExercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeanceExercice;

    private Integer series;
    private Integer repetitions;
    private Float charge;
    private Integer tempsSecondes;
    private Integer ordre;

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;

    @ManyToOne
    @JoinColumn(name = "exercice_id")
    private Exercice exercice;
}
