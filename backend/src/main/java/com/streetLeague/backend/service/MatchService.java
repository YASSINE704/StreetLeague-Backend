package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.MatchDTO;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.entity.Equipe;
import com.streetLeague.backend.entity.Terrain;
import com.streetLeague.backend.enums.MatchStatus;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.MatchRepository;
import com.streetLeague.backend.repository.EquipeRepository;
import com.streetLeague.backend.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Match entities.
 * 
 * Provides business logic for match scheduling, results recording,
 * and match history tracking.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private TerrainRepository terrainRepository;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private MatchCompletionService matchCompletionService;

    /**
     * Retrieves all matches from the database.
     * 
     * @return List of all Match entities
     */
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    /**
     * Retrieves a specific match by its ID.
     * 
     * @param id The unique identifier of the match
     * @return The Match entity with the specified ID
     * @throws ResourceNotFoundException if match is not found
     */
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Match not found with id: %d", id)
                ));
    }

    /**
     * Retrieves match with full details as DTO.
     * 
     * @param id The match ID
     * @return MatchDTO with complete match information
     */
    public MatchDTO getMatchDetails(Long id) {
        Match match = getMatchById(id);
        return convertToDTO(match);
    }

    /**
     * Creates and saves a new match to the database.
     * 
     * @param match The Match entity to save
     * @return The saved Match entity with generated ID
     */
    public Match saveMatch(Match match) {
        match.setHomeTeam(resolveTeam(match.getHomeTeam(), "Home team"));
        match.setAwayTeam(resolveTeam(match.getAwayTeam(), "Away team"));
        match.setTerrain(resolveTerrain(match.getTerrain()));
        validateDistinctTeams(match.getHomeTeam(), match.getAwayTeam());

        if (match.getStatus() == null) {
            match.setStatus(MatchStatus.SCHEDULED);
        }
        return matchRepository.save(match);
    }

    /**
     * Schedules a new match between two teams on a terrain.
     * 
     * @param homeTeamId The home team ID
     * @param awayTeamId The away team ID
     * @param terrainId The terrain ID
     * @param matchDate The match date and time
     * @return Created Match
     */
    public Match scheduleMatch(Long homeTeamId, Long awayTeamId, Long terrainId, LocalDateTime matchDate) {
        Equipe homeTeam = equipeRepository.findById(homeTeamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Team not found with id: %d", homeTeamId)
                ));
        
        Equipe awayTeam = equipeRepository.findById(awayTeamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Team not found with id: %d", awayTeamId)
                ));
        
        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Terrain not found with id: %d", terrainId)
                ));
        
        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setTerrain(terrain);
        match.setMatchDate(matchDate);
        match.setStatus(MatchStatus.SCHEDULED);
        
        return saveMatch(match);
    }

    /**
     * Updates an existing match.
     * 
     * @param id The match ID
     * @param matchDetails The updated match details
     * @return The updated Match entity
     */
    public Match updateMatch(Long id, Match matchDetails) {
        Match match = getMatchById(id);
        MatchStatus oldStatus = match.getStatus();
        
        if (matchDetails.getHomeTeam() != null) {
            match.setHomeTeam(resolveTeam(matchDetails.getHomeTeam(), "Home team"));
        }
        if (matchDetails.getAwayTeam() != null) {
            match.setAwayTeam(resolveTeam(matchDetails.getAwayTeam(), "Away team"));
        }
        if (matchDetails.getTerrain() != null) {
            match.setTerrain(resolveTerrain(matchDetails.getTerrain()));
        }
        if (matchDetails.getMatchDate() != null) {
            match.setMatchDate(matchDetails.getMatchDate());
        }
        if (matchDetails.getStatus() != null) {
            match.setStatus(matchDetails.getStatus());
        }

        validateDistinctTeams(match.getHomeTeam(), match.getAwayTeam());
        
        Match updatedMatch = matchRepository.save(match);
        
        // Auto-record player stats if status changed to COMPLETED
        if (oldStatus != MatchStatus.COMPLETED && updatedMatch.getStatus() == MatchStatus.COMPLETED) {
            matchCompletionService.recordMatchStatistics(updatedMatch);
        }
        
        return updatedMatch;
    }

    /**
     * Records the result of a completed match and updates team statistics.
     * Automatically generates player statistics for all players in the match.
     * 
     * @param id The match ID
     * @param homeTeamScore Goals scored by home team
     * @param awayTeamScore Goals scored by away team
     * @return Updated Match with results
     */
    public Match recordMatchResult(Long id, int homeTeamScore, int awayTeamScore) {
        Match match = getMatchById(id);

        if (match.getStatus() == MatchStatus.COMPLETED
                && match.getHomeTeamScore() != null
                && match.getAwayTeamScore() != null) {
            updateMatchTeamStats(match, match.getHomeTeamScore(), match.getAwayTeamScore(), -1);
        }

        match.setHomeTeamScore(homeTeamScore);
        match.setAwayTeamScore(awayTeamScore);
        match.setStatus(MatchStatus.COMPLETED);

        updateMatchTeamStats(match, homeTeamScore, awayTeamScore, 1);
        
        Match savedMatch = matchRepository.save(match);
        
        // Auto-record player stats when match result is recorded
        matchCompletionService.recordMatchStatistics(savedMatch);
        
        return savedMatch;
    }

    /**
     * Deletes a match from the database.
     * 
     * @param id The unique identifier of the match to delete
     * @throws ResourceNotFoundException if match is not found
     */
    public void deleteMatch(Long id) {
        Match match = getMatchById(id);

        if (match.getStatus() == MatchStatus.COMPLETED
                && match.getHomeTeamScore() != null
                && match.getAwayTeamScore() != null) {
            updateMatchTeamStats(match, match.getHomeTeamScore(), match.getAwayTeamScore(), -1);
        }

        matchRepository.delete(match);
    }

    /**
     * Retrieves all scheduled matches.
     * 
     * @return List of scheduled Match entities
     */
    public List<Match> getScheduledMatches() {
        return matchRepository.findByStatus(MatchStatus.SCHEDULED);
    }

    /**
     * Retrieves all completed matches.
     * 
     * @return List of completed Match entities
     */
    public List<Match> getMatchHistory() {
        return matchRepository.findByStatus(MatchStatus.COMPLETED);
    }

    /**
     * Retrieves matches for a specific team.
     * 
     * @param teamId The team ID
     * @return List of matches involving that team
     */
    public List<Match> getTeamMatches(Long teamId) {
        return matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId);
    }

    /**
     * Retrieves matches played on a specific terrain.
     * 
     * @param terrainId The terrain ID
     * @return List of matches on that terrain
     */
    public List<Match> getTerrainMatches(Long terrainId) {
        return matchRepository.findByTerrainId(terrainId);
    }

    /**
     * Retrieves matches within a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of matches in that period
     */
    public List<Match> getMatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return matchRepository.findByMatchDateBetween(startDate, endDate);
    }

    /**
     * Converts Match entity to MatchDTO.
     * 
     * @param match The Match entity
     * @return MatchDTO with match information
     */
    public MatchDTO convertToDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        Equipe homeTeam = match.getHomeTeam();
        Equipe awayTeam = match.getAwayTeam();
        Terrain terrain = match.getTerrain();

        dto.setId(match.getId());
        dto.setHomeTeamId(homeTeam != null ? homeTeam.getId() : null);
        dto.setHomeTeamName(homeTeam != null ? homeTeam.getNom() : "Home team");
        dto.setAwayTeamId(awayTeam != null ? awayTeam.getId() : null);
        dto.setAwayTeamName(awayTeam != null ? awayTeam.getNom() : "Away team");
        dto.setTerrainId(terrain != null ? terrain.getId() : null);
        dto.setTerrainName(terrain != null ? terrain.getNom() : "Unassigned field");
        dto.setTerrainAddress(terrain != null ? terrain.getAddress() : null);
        dto.setMatchDate(match.getMatchDate());
        dto.setStatus(match.getStatus());
        dto.setHomeTeamScore(match.getHomeTeamScore());
        dto.setAwayTeamScore(match.getAwayTeamScore());

        Integer homeScore = match.getHomeTeamScore();
        Integer awayScore = match.getAwayTeamScore();

        if (match.getStatus() == MatchStatus.COMPLETED && homeScore != null && awayScore != null) {
            if (homeScore > awayScore) {
                dto.setResult(dto.getHomeTeamName() + " Win");
            } else if (awayScore > homeScore) {
                dto.setResult(dto.getAwayTeamName() + " Win");
            } else {
                dto.setResult("Draw");
            }
        } else {
            dto.setResult("Not Played");
        }
        
        return dto;
    }

    /**
     * Gets all matches as DTOs.
     * 
     * @return List of MatchDTOs
     */
    public List<MatchDTO> getAllMatchesAsDTO() {
        return getAllMatches().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Equipe resolveTeam(Equipe team, String label) {
        if (team == null || team.getId() == null) {
            throw new IllegalArgumentException(label + " cannot be null");
        }

        return equipeRepository.findById(team.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Team not found with id: %d", team.getId())
                ));
    }

    private Terrain resolveTerrain(Terrain terrain) {
        if (terrain == null || terrain.getId() == null) {
            throw new IllegalArgumentException("Terrain cannot be null");
        }

        return terrainRepository.findById(terrain.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Terrain not found with id: %d", terrain.getId())
                ));
    }

    private void validateDistinctTeams(Equipe homeTeam, Equipe awayTeam) {
        if (homeTeam != null && awayTeam != null && homeTeam.getId().equals(awayTeam.getId())) {
            throw new IllegalArgumentException("Home and away teams must be different");
        }
    }

    private void updateMatchTeamStats(Match match, int homeTeamScore, int awayTeamScore, int direction) {
        int homeWinDelta = homeTeamScore > awayTeamScore ? direction : 0;
        int homeLossDelta = awayTeamScore > homeTeamScore ? direction : 0;
        int homeDrawDelta = homeTeamScore == awayTeamScore ? direction : 0;
        int awayWinDelta = awayTeamScore > homeTeamScore ? direction : 0;
        int awayLossDelta = homeTeamScore > awayTeamScore ? direction : 0;
        int awayDrawDelta = homeTeamScore == awayTeamScore ? direction : 0;

        equipeService.adjustTeamStats(
                match.getHomeTeam().getId(),
                direction * homeTeamScore,
                direction * awayTeamScore,
                homeWinDelta,
                homeLossDelta,
                homeDrawDelta
        );
        equipeService.adjustTeamStats(
                match.getAwayTeam().getId(),
                direction * awayTeamScore,
                direction * homeTeamScore,
                awayWinDelta,
                awayLossDelta,
                awayDrawDelta
        );
    }
}
