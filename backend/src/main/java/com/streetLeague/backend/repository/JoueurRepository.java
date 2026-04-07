package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Joueur (Player) entity.
 * 
 * Provides database access and CRUD operations for player records.
 * Extends JpaRepository to inherit standard Spring Data JPA functionality.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Repository
public interface JoueurRepository extends JpaRepository<Joueur, Long> {

    /**
     * Finds all players assigned to a specific team.
     *
     * @param equipeId the team id
     * @return players assigned to the team
     */
    List<Joueur> findByEquipeId(Long equipeId);
}
