package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private StatutReservation statut;
    private LocalDateTime dateCreation;
    private String motifAnnulation;
    private Long sousEspaceId;
    private String sousEspaceNom;
    private Long endroitId;
    private String endroitNom;
    private Double prixTotal;
}
