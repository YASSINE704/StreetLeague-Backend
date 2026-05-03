package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.Intensite;
import com.streetLeague.backend.enums.StatutSeance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeanceEntrainement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeance;

    private String titreSeance;
    private LocalDate dateSeance;
    private Integer dureeMinutes;

    /* ── Champs ajoutés pour Step 2 : réservation & planning ── */
    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Builder.Default
    private Integer maxParticipants = 5;

    /* ── Step 4 : lieu et météo ── */
    @ManyToOne
    @JoinColumn(name = "sous_espace_id")
    private SousEspace lieu;

    @Builder.Default
    private Boolean enPleinAir = false;

    @Enumerated(EnumType.STRING)
    private Intensite intensite;

    @Enumerated(EnumType.STRING)
    private StatutSeance statut;

    @ManyToOne
    @JoinColumn(name = "programme_id")
    private ProgrammeEntrainement programme;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SeanceExercice> seanceExercices = new ArrayList<>();

    @OneToOne(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    private SuiviSeance suiviSeance;

    /* ── Réservations des sportifs pour cette séance ── */
    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservationSeance> reservations = new ArrayList<>();
}
