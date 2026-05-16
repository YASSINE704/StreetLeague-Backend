package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.ReservationSeance;
import com.streetLeague.backend.enums.StatutReservationSeance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationSeanceRepository extends JpaRepository<ReservationSeance, Integer> {

    /** Toutes les réservations actives (non annulées) pour une séance */
    List<ReservationSeance> findBySeanceIdSeanceAndStatutNot(Integer seanceId, StatutReservationSeance statut);

    /** Vérifier si un sportif a déjà réservé cette séance (non annulée) */
    Optional<ReservationSeance> findByUserIdUserAndSeanceIdSeanceAndStatutNot(
            Integer userId, Integer seanceId, StatutReservationSeance statut);

    /** Toutes les réservations d'un sportif (non annulées) */
    List<ReservationSeance> findByUserIdUserAndStatutNot(Integer userId, StatutReservationSeance statut);

    /** Toutes les réservations pour une séance */
    List<ReservationSeance> findBySeanceIdSeance(Integer seanceId);

    /** Toutes les réservations d'un sportif */
    List<ReservationSeance> findByUserIdUser(Integer userId);
}
