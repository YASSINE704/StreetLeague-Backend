package com.streetLeague.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeSousEspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousEspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeSousEspace type;

    private Integer capacite;

    private Double prixBase; // prix par heure en DT

    @Enumerated(EnumType.STRING)
    private StatutEndroit statut;

    @ManyToOne
    @JoinColumn(name = "endroit_id")
    @JsonIgnore
    private Endroit endroit;

    @JsonProperty("endroitNom")
    public String fetchEndroitNom() {
        return endroit != null ? endroit.getNom() : null;
    }

    @JsonProperty("endroitId")
    public Long fetchEndroitId() {
        return endroit != null ? endroit.getId() : null;
    }

    @OneToMany(mappedBy = "sousEspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipement> equipements = new ArrayList<>();

    @OneToMany(mappedBy = "sousEspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();
}
