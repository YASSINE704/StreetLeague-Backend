package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.TypeAffectationProgramme;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffectationProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idAffectation;

    @Enumerated(EnumType.STRING)
    private TypeAffectationProgramme type;

    @Column(name = "date_affectation")
    private LocalDateTime dateAffectation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "programme_id")
    private ProgrammeEntrainement programme;
}
