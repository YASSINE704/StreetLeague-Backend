package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findBySousEspaceId(Long sousEspaceId);
    List<Reservation> findByStatut(StatutReservation statut);
    List<Reservation> findByDateDebutBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT r FROM Reservation r WHERE r.sousEspace.id = :sousEspaceId " +
           "AND r.statut IN (:statuts) " +
           "AND r.dateDebut < :dateFin AND r.dateFin > :dateDebut")
    List<Reservation> findOverlapping(
        @Param("sousEspaceId") Long sousEspaceId,
        @Param("dateDebut") LocalDateTime dateDebut,
        @Param("dateFin") LocalDateTime dateFin,
        @Param("statuts") List<StatutReservation> statuts
    );
}
