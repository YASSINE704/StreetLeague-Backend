package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.StatutProgramme;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgrammeEntrainement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProgramme;

    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutProgramme statut;

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SeanceEntrainement> seances = new ArrayList<>();

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AffectationProgramme> affectations = new ArrayList<>();
}
