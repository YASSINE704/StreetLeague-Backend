package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.ReservationSeanceDTO;
import com.streetLeague.backend.entity.ReservationSeance;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.mapper.ReservationSeanceMapper;
import com.streetLeague.backend.repository.ReservationSeanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Service de réservation de séances de coaching.
 *
 * Règles métier :
 * 1. Seuls les SPORTIF/ADMIN peuvent réserver (le COACH gère, il ne réserve pas)
 * 2. Max 5 participants par séance (configurable via maxParticipants)
 * 3. Pas de doublon : un sportif ne peut pas réserver 2 fois la même séance
 * 4. Pas de chevauchement : un sportif ne peut pas réserver 2 séances au même créneau
 * 5. La séance doit être PREVUE (pas REALISEE, ANNULEE)
 * 6. Le mode de paiement est obligatoire
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationSeanceService {

    private final ReservationSeanceRepository reservationRepository;
    private final SeanceEntrainementService seanceService;
    private final CoachingRoleService roleService;

    /**
     * Réserver une place dans une séance.
     * Le userId vient du header X-User-Id.
     */
    public ReservationSeanceDTO.Response reserver(Integer userId, ReservationSeanceDTO.Request dto) {
        // Vérifier que l'utilisateur est SPORTIF ou ADMIN
        User user = roleService.requireSportifOrCoachOrAdmin(userId);

        SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());

        // Règle 1 : la séance doit être PREVUE
        if (seance.getStatut() != StatutSeance.PREVUE) {
            throw new BusinessRuleException(
                    "Impossible de réserver : la séance n'est pas disponible (statut actuel : " + seance.getStatut() + ")");
        }

        // Règle 2 : vérifier la capacité (max participants)
        List<ReservationSeance> reservationsActives = reservationRepository
                .findBySeanceIdSeanceAndStatutNot(dto.getSeanceId(), StatutReservationSeance.ANNULEE);
        int maxPlaces = seance.getMaxParticipants() != null ? seance.getMaxParticipants() : 5;
        if (reservationsActives.size() >= maxPlaces) {
            throw new BusinessRuleException(
                    "Séance complète : capacité maximale atteinte (" + maxPlaces + "/" + maxPlaces + " places)");
        }

        // Règle 3 : pas de doublon (même sportif, même séance)
        reservationRepository.findByUserIdUserAndSeanceIdSeanceAndStatutNot(
                userId, dto.getSeanceId(), StatutReservationSeance.ANNULEE
        ).ifPresent(existing -> {
            throw new BusinessRuleException("Vous avez déjà réservé cette séance");
        });

        // Règle 4 : pas de chevauchement horaire avec une autre séance réservée
        if (seance.getHeureDebut() != null && seance.getHeureFin() != null) {
            validateNoTimeOverlap(userId, seance);
        }

        // Créer la réservation
        ReservationSeance reservation = ReservationSeance.builder()
                .user(user)
                .seance(seance)
                .dateReservation(LocalDateTime.now())
                .statut(StatutReservationSeance.RESERVEE)
                .modePaiement(dto.getModePaiement())
                .build();

        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));
    }

    /**
     * Annuler une réservation (par le sportif ou le coach).
     */
    public ReservationSeanceDTO.Response annuler(Integer reservationId, String motif) {
        ReservationSeance reservation = findOrThrow(reservationId);

        if (reservation.getStatut() == StatutReservationSeance.ANNULEE) {
            throw new BusinessRuleException("Cette réservation est déjà annulée");
        }

        reservation.setStatut(StatutReservationSeance.ANNULEE);
        reservation.setMotifAnnulation(motif != null ? motif : "Annulée par l'utilisateur");
        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));
    }

    /**
     * Confirmer une réservation (par le coach).
     */
    public ReservationSeanceDTO.Response confirmer(Integer reservationId) {
        ReservationSeance reservation = findOrThrow(reservationId);

        if (reservation.getStatut() != StatutReservationSeance.RESERVEE) {
            throw new BusinessRuleException(
                    "Seules les réservations en attente peuvent être confirmées (statut actuel : " + reservation.getStatut() + ")");
        }

        reservation.setStatut(StatutReservationSeance.CONFIRMEE);
        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));
    }

    /** Réservations d'une séance */
    @Transactional(readOnly = true)
    public List<ReservationSeanceDTO.Response> getBySeance(Integer seanceId) {
        return reservationRepository.findBySeanceIdSeance(seanceId).stream()
                .map(ReservationSeanceMapper::toResponse).toList();
    }

    /** Réservations d'un sportif */
    @Transactional(readOnly = true)
    public List<ReservationSeanceDTO.Response> getByUser(Integer userId) {
        return reservationRepository.findByUserIdUser(userId).stream()
                .map(ReservationSeanceMapper::toResponse).toList();
    }

    /** Nombre de places restantes pour une séance */
    @Transactional(readOnly = true)
    public int getPlacesRestantes(Integer seanceId) {
        SeanceEntrainement seance = seanceService.findOrThrow(seanceId);
        int maxPlaces = seance.getMaxParticipants() != null ? seance.getMaxParticipants() : 5;
        long reservees = reservationRepository
                .findBySeanceIdSeanceAndStatutNot(seanceId, StatutReservationSeance.ANNULEE).size();
        return Math.max(0, maxPlaces - (int) reservees);
    }

    /** Step 6 : Marquer une réservation comme payée (par le coach) */
    public ReservationSeanceDTO.Response marquerPaye(Integer reservationId) {
        ReservationSeance reservation = findOrThrow(reservationId);
        if (reservation.getStatut() == StatutReservationSeance.ANNULEE) {
            throw new BusinessRuleException("Impossible de marquer comme payée une réservation annulée");
        }
        reservation.setStatutPaiement(com.streetLeague.backend.enums.StatutPaiement.PAYE);
        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));
    }

    private ReservationSeance findOrThrow(Integer id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec id: " + id));
    }

    /**
     * Vérifie qu'un sportif n'a pas déjà une réservation active
     * pour une autre séance qui chevauche le même créneau horaire.
     */
    private void validateNoTimeOverlap(Integer userId, SeanceEntrainement newSeance) {
        List<ReservationSeance> mesReservations = reservationRepository
                .findByUserIdUserAndStatutNot(userId, StatutReservationSeance.ANNULEE);

        for (ReservationSeance r : mesReservations) {
            SeanceEntrainement existante = r.getSeance();

            // Même date ?
            if (existante.getDateSeance() == null || !existante.getDateSeance().equals(newSeance.getDateSeance())) {
                continue;
            }

            // Vérifier le chevauchement horaire
            if (existante.getHeureDebut() != null && existante.getHeureFin() != null) {
                LocalTime newDebut = newSeance.getHeureDebut();
                LocalTime newFin = newSeance.getHeureFin();
                LocalTime existDebut = existante.getHeureDebut();
                LocalTime existFin = existante.getHeureFin();

                // Chevauchement : newDebut < existFin AND newFin > existDebut
                if (newDebut.isBefore(existFin) && newFin.isAfter(existDebut)) {
                    throw new BusinessRuleException(
                            "Conflit horaire : vous avez déjà réservé la séance « " + existante.getTitreSeance()
                            + " » le même jour de " + existDebut + " à " + existFin);
                }
            }
        }
    }
}
