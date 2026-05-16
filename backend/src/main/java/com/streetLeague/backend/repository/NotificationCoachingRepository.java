package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.NotificationCoaching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationCoachingRepository extends JpaRepository<NotificationCoaching, Integer> {

    /** Vérifier si une notification a déjà été envoyée pour une séance et un type */
    boolean existsBySeanceIdSeanceAndTypeAndUserIdUser(Integer seanceId, String type, Integer userId);

    /** Notifications d'un utilisateur */
    List<NotificationCoaching> findByUserIdUserOrderByDateEnvoiDesc(Integer userId);

    /** Notifications d'une séance */
    List<NotificationCoaching> findBySeanceIdSeance(Integer seanceId);
}
