package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.TypeExercice;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExercice;

    private String nom;
    private String description;

    @Enumerated(EnumType.STRING)
    private TypeExercice type;

    private String videoUrl;
}
