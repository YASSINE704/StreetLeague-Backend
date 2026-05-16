package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipementDTO {
    private Long id;
    private String nom;
    private Integer quantite;
    private Long sousEspaceId;
    private String sousEspaceNom;
}
