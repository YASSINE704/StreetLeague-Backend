package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndroitDTO {
    private Long id;
    private String nom;
    private TypeEndroit type;
    private String adresse;
    private String ville;
    private Double latitude;
    private Double longitude;
    private Integer capacite;
    private StatutEndroit statut;
    private String description;
    private String imageUrl;
}
