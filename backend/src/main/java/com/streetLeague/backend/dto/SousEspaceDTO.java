package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeSousEspace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousEspaceDTO {
    private Long id;
    private String nom;
    private TypeSousEspace type;
    private Integer capacite;
    private StatutEndroit statut;
    private Long endroitId;
    private String endroitNom;
}
