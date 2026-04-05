package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutReservation;
import com.streetLeague.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SousEspaceService sousEspaceService;
    private final NotificationService notificationService;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    }

    public Reservation createReservation(Long sousEspaceId, Reservation reservation) {
        // Validation: dateDebut must be before dateFin
        if (reservation.getDateDebut().isAfter(reservation.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        if (reservation.getDateDebut().isEqual(reservation.getDateFin())) {
            throw new IllegalArgumentException("La date de début ne peut pas être égale à la date de fin");
        }

        // Validation: no overlapping CONFIRMEE or EN_ATTENTE reservations
        List<Reservation> overlapping = reservationRepository.findOverlapping(
            sousEspaceId,
            reservation.getDateDebut(),
            reservation.getDateFin(),
            List.of(StatutReservation.CONFIRMEE, StatutReservation.EN_ATTENTE)
        );
        if (!overlapping.isEmpty()) {
            String conflictStatut = overlapping.get(0).getStatut().name();
            throw new IllegalArgumentException(
                "Il existe déjà une réservation " + conflictStatut +
                " sur ce créneau (du " + overlapping.get(0).getDateDebut() +
                " au " + overlapping.get(0).getDateFin() + ")"
            );
        }

        SousEspace sousEspace = sousEspaceService.getSousEspaceById(sousEspaceId);
        reservation.setSousEspace(sousEspace);
        reservation.setDateCreation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyReservationCreated(saved.getId(), sousEspace.getNom(),
            sousEspace.getEndroit() != null ? sousEspace.getEndroit().getNom() : "");
        return saved;
    }

    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation reservation = getReservationById(id);

        if (reservationDetails.getDateDebut() != null && reservationDetails.getDateFin() != null) {
            if (reservationDetails.getDateDebut().isAfter(reservationDetails.getDateFin())) {
                throw new IllegalArgumentException("La date de début doit être avant la date de fin");
            }
        }

        reservation.setDateDebut(reservationDetails.getDateDebut());
        reservation.setDateFin(reservationDetails.getDateFin());
        reservation.setStatut(reservationDetails.getStatut());
        reservation.setMotifAnnulation(reservationDetails.getMotifAnnulation());
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsBySousEspaceId(Long sousEspaceId) {
        return reservationRepository.findBySousEspaceId(sousEspaceId);
    }

    public List<Reservation> getReservationsByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    public Reservation confirmerReservation(Long id) {
        Reservation reservation = getReservationById(id);

        // Check no other CONFIRMEE reservation overlaps
        List<Reservation> overlapping = reservationRepository.findOverlapping(
            reservation.getSousEspace().getId(),
            reservation.getDateDebut(),
            reservation.getDateFin(),
            List.of(StatutReservation.CONFIRMEE)
        );
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException(
                "Impossible de confirmer: une réservation CONFIRMEE existe déjà sur ce créneau"
            );
        }

        reservation.setStatut(StatutReservation.CONFIRMEE);
        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyReservationConfirmed(saved.getId(),
            reservation.getSousEspace().getNom(),
            reservation.getSousEspace().getEndroit() != null ? reservation.getSousEspace().getEndroit().getNom() : "");
        return saved;
    }

    public Reservation annulerReservation(Long id, String motif) {
        Reservation reservation = getReservationById(id);
        reservation.setStatut(StatutReservation.ANNULEE);
        reservation.setMotifAnnulation(motif);
        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyReservationCancelled(saved.getId(),
            reservation.getSousEspace().getNom(),
            reservation.getSousEspace().getEndroit() != null ? reservation.getSousEspace().getEndroit().getNom() : "");
        return saved;
    }
}
