package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for PlayerStats entity.
 * 
 * Provides database access and CRUD operations for player statistics records.
 * Includes custom queries for retrieving player performance data.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {
    
    /**
     * Retrieves all statistics records for a specific player.
     * 
     * @param joueurId The ID of the player
     * @return List of PlayerStats for the player
     */
    List<PlayerStats> findByJoueurId(Long joueurId);
    
    /**
     * Retrieves all statistics records from a specific match.
     * 
     * @param matchId The ID of the match
     * @return List of PlayerStats from the match
     */
    List<PlayerStats> findByMatchId(Long matchId);
}
