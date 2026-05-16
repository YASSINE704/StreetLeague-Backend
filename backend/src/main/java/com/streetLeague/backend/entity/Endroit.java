package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
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
public class Endroit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeEndroit type;

    private String adresse;
    private String ville;
    private Double latitude;
    private Double longitude;
    private Integer capacite;

    @Enumerated(EnumType.STRING)
    private StatutEndroit statut;

    private String description;
    private String imageUrl;

    @OneToMany(mappedBy = "endroit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SousEspace> sousEspaces = new ArrayList<>();
}
