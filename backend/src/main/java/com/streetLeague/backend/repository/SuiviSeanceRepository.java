package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.SuiviSeance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuiviSeanceRepository extends JpaRepository<SuiviSeance, Integer> {
    /** Tous les suivis d'une séance (multiple feedbacks possibles) */
    List<SuiviSeance> findAllBySeanceIdSeance(Integer seanceId);

    /** Premier suivi d'une séance (compatibilité) */
    Optional<SuiviSeance> findBySeanceIdSeance(Integer seanceId);

    /** Vérifier si un utilisateur a déjà donné un feedback pour une séance */
    Optional<SuiviSeance> findBySeanceIdSeanceAndAuteurIdUser(Integer seanceId, Integer auteurId);

    /** Tous les suivis créés par un auteur */
    List<SuiviSeance> findByAuteurIdUser(Integer auteurId);
}
