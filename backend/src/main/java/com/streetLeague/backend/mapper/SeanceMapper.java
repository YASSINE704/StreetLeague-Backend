package com.streetLeague.backend.mapper;

import com.streetLeague.backend.dto.*;
import com.streetLeague.backend.entity.*;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;

import java.util.Collections;
import java.util.List;

public class SeanceMapper {

    private SeanceMapper() {}

    public static SeanceEntrainement toEntity(SeanceEntrainementDTO.Request dto) {
        return SeanceEntrainement.builder()
                .titreSeance(dto.getTitreSeance())
                .dateSeance(dto.getDateSeance())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .dureeMinutes(dto.getDureeMinutes())
                .maxParticipants(dto.getMaxParticipants() != null ? dto.getMaxParticipants() : 5)
                .enPleinAir(dto.getEnPleinAir() != null ? dto.getEnPleinAir() : false)
                .intensite(dto.getIntensite())
                .statut(dto.getStatut() != null ? dto.getStatut() : StatutSeance.PREVUE)
                .build();
    }

    public static SeanceEntrainementDTO.Response toResponse(SeanceEntrainement entity) {
        List<SeanceExerciceDTO.Response> exercices = entity.getSeanceExercices() != null
                ? entity.getSeanceExercices().stream().map(ExerciceMapper::toSeanceExerciceResponse).toList()
                : Collections.emptyList();

        SuiviSeanceDTO.Response suivi = (entity.getSuiviSeances() != null && !entity.getSuiviSeances().isEmpty())
                ? SuiviMapper.toResponse(entity.getSuiviSeances().get(0))
                : null;

        /* Calculer les réservations actives et les places restantes */
        List<ReservationSeanceDTO.Response> reservations = Collections.emptyList();
        int placesRestantes = entity.getMaxParticipants() != null ? entity.getMaxParticipants() : 5;

        if (entity.getReservations() != null) {
            reservations = entity.getReservations().stream()
                    .map(ReservationSeanceMapper::toResponse).toList();
            long reserveesActives = entity.getReservations().stream()
                    .filter(r -> r.getStatut() != StatutReservationSeance.ANNULEE)
                    .count();
            placesRestantes = Math.max(0, placesRestantes - (int) reserveesActives);
        }

        return SeanceEntrainementDTO.Response.builder()
                .idSeance(entity.getIdSeance())
                .titreSeance(entity.getTitreSeance())
                .dateSeance(entity.getDateSeance())
                .heureDebut(entity.getHeureDebut())
                .heureFin(entity.getHeureFin())
                .dureeMinutes(entity.getDureeMinutes())
                .maxParticipants(entity.getMaxParticipants())
                .placesRestantes(placesRestantes)
                .intensite(entity.getIntensite())
                .statut(entity.getStatut())
                .programmeId(entity.getProgramme().getIdProgramme())
                .programmeTitre(entity.getProgramme().getTitre())
                /* Step 4 : lieu */
                .sousEspaceId(entity.getLieu() != null ? entity.getLieu().getId() : null)
                .lieuNom(entity.getLieu() != null ? entity.getLieu().getNom() : null)
                .endroitNom(entity.getLieu() != null && entity.getLieu().getEndroit() != null
                        ? entity.getLieu().getEndroit().getNom() : null)
                .enPleinAir(entity.getEnPleinAir())
                .exercices(exercices)
                .suiviSeance(suivi)
                .reservations(reservations)
                .build();
    }
}
