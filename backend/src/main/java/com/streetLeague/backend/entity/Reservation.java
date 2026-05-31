package com.streetLeague.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.streetLeague.backend.enums.StatutReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    private LocalDateTime dateCreation;
    private String motifAnnulation;
    private Double prixTotal;

    @ManyToOne
    @JoinColumn(name = "sous_espace_id")
    @JsonIgnore
    private SousEspace sousEspace;

    @JsonProperty("sousEspaceNom")
    public String fetchSousEspaceNom() {
        return sousEspace != null ? sousEspace.getNom() : null;
    }

    @JsonProperty("sousEspaceId")
    public Long fetchSousEspaceId() {
        return sousEspace != null ? sousEspace.getId() : null;
    }

    @JsonProperty("endroitNom")
    public String fetchEndroitNom() {
        return sousEspace != null && sousEspace.getEndroit() != null ? sousEspace.getEndroit().getNom() : null;
    }

    @JsonProperty("endroitId")
    public Long fetchEndroitId() {
        return sousEspace != null && sousEspace.getEndroit() != null ? sousEspace.getEndroit().getId() : null;
    }
}
