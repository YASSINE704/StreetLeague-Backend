package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.ReservationSeanceDTO;
import com.streetLeague.backend.entity.ReservationSeance;

public class ReservationSeanceMapper {

    private ReservationSeanceMapper() {}

    public static ReservationSeanceDTO.Response toResponse(ReservationSeance entity) {
        return ReservationSeanceDTO.Response.builder()
                .idReservation(entity.getIdReservation())
                .dateReservation(entity.getDateReservation())
                .statut(entity.getStatut())
                .modePaiement(entity.getModePaiement())
                .statutPaiement(entity.getStatutPaiement())
                .montant(entity.getMontant())
                .motifAnnulation(entity.getMotifAnnulation())
                .userId(entity.getUser().getIdUser())
                .userNom(entity.getUser().getNom() + " " + entity.getUser().getPrenom())
                .seanceId(entity.getSeance().getIdSeance())
                .seanceTitre(entity.getSeance().getTitreSeance())
                .build();
    }
}
