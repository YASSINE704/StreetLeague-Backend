package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Match (Game) entity.
 * 
 * Provides database access and CRUD operations for match records.
 * Includes custom queries for retrieving matches by various criteria.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    /**
     * Retrieves all matches with a specific status.
     * 
     * @param status The match status (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED)
     * @return List of matches with the specified status
     */
    List<Match> findByStatus(MatchStatus status);
    
    /**
     * Retrieves all matches played on a specific terrain.
     * 
     * @param terrainId The ID of the terrain
     * @return List of matches played on that terrain
     */
    List<Match> findByTerrainId(Long terrainId);
    
    /**
     * Retrieves all matches scheduled for a specific team (home or away).
     * 
     * @param teamId The ID of the team
     * @return List of matches involving this team
     */
    List<Match> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);
    
    /**
     * Retrieves matches scheduled within a date range.
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of matches within the date range
     */
    List<Match> findByMatchDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
