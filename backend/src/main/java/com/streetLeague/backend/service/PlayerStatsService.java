package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.PlayerStatsDTO;
import com.streetLeague.backend.entity.PlayerStats;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.PlayerStatsRepository;
import com.streetLeague.backend.repository.JoueurRepository;
import com.streetLeague.backend.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing PlayerStats entities.
 * 
 * Provides business logic for tracking player performance statistics
 * including goals, assists, minutes played, and performance ratings.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class PlayerStatsService {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private MatchRepository matchRepository;

    /**
     * Retrieves all player statistics records.
     * 
     * @return List of all PlayerStats
     */
    public List<PlayerStats> getAllPlayerStats() {
        return playerStatsRepository.findAll();
    }

    /**
     * Retrieves statistics for a specific player.
     * 
     * @param playerId The player ID
     * @return List of PlayerStats for that player
     */
    public List<PlayerStats> getPlayerStatistics(Long playerId) {
        joueurRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", playerId)
                ));
        return playerStatsRepository.findByJoueurId(playerId);
    }

    /**
     * Retrieves statistics for a specific match.
     * 
     * @param matchId The match ID
     * @return List of PlayerStats from that match
     */
    public List<PlayerStats> getMatchStatistics(Long matchId) {
        matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Match not found with id: %d", matchId)
                ));
        return playerStatsRepository.findByMatchId(matchId);
    }

    /**
     * Creates and saves player statistics for a match.
     * 
     * @param playerStats The PlayerStats entity to save
     * @return Saved PlayerStats
     */
    public PlayerStats savePlayerStats(PlayerStats playerStats) {
        playerStats.setJoueur(resolvePlayer(playerStats.getJoueur()));
        playerStats.setMatch(resolveMatch(playerStats.getMatch()));
        return playerStatsRepository.save(playerStats);
    }

    /**
     * Records player performance for a match.
     * 
     * @param playerId The player ID
     * @param matchId The match ID
     * @param goals Goals scored
     * @param assists Assists provided
     * @param minutesPlayed Minutes on field
     * @param performanceRating Rating (0-10)
     * @return Created PlayerStats
     */
    public PlayerStats recordPlayerPerformance(
            Long playerId, Long matchId, int goals, int assists,
            int minutesPlayed, double performanceRating) {
        
        Joueur joueur = joueurRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", playerId)
                ));
        
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Match not found with id: %d", matchId)
                ));
        
        PlayerStats stats = new PlayerStats();
        stats.setJoueur(joueur);
        stats.setMatch(match);
        stats.setGoals(goals);
        stats.setAssists(assists);
        stats.setMinutesPlayed(minutesPlayed);
        stats.setPerformanceRating(performanceRating);
        
        return playerStatsRepository.save(stats);
    }

    /**
     * Converts PlayerStats entity to DTO.
     * 
     * @param stats The PlayerStats entity
     * @return PlayerStatsDTO
     */
    public PlayerStatsDTO convertToDTO(PlayerStats stats) {
        PlayerStatsDTO dto = new PlayerStatsDTO();
        dto.setId(stats.getId());
        dto.setPlayerId(stats.getJoueur().getId());
        dto.setPlayerName(stats.getJoueur().getNom());
        dto.setMatchId(stats.getMatch().getId());
        dto.setMatchDate(stats.getMatch().getMatchDate());

        Long playerTeamId = stats.getJoueur().getEquipe() != null
                ? stats.getJoueur().getEquipe().getId()
                : null;

        Long homeTeamId = stats.getMatch().getHomeTeam() != null ? stats.getMatch().getHomeTeam().getId() : null;
        Long awayTeamId = stats.getMatch().getAwayTeam() != null ? stats.getMatch().getAwayTeam().getId() : null;
        String homeTeamName = stats.getMatch().getHomeTeam() != null ? stats.getMatch().getHomeTeam().getNom() : "Home team";
        String awayTeamName = stats.getMatch().getAwayTeam() != null ? stats.getMatch().getAwayTeam().getNom() : "Away team";

        if (playerTeamId != null && playerTeamId.equals(homeTeamId)) {
            dto.setOpponent(awayTeamName);
        } else if (playerTeamId != null && playerTeamId.equals(awayTeamId)) {
            dto.setOpponent(homeTeamName);
        } else {
            dto.setOpponent("Unknown opponent");
        }
        
        dto.setGoals(stats.getGoals());
        dto.setAssists(stats.getAssists());
        dto.setMinutesPlayed(stats.getMinutesPlayed());
        dto.setPerformanceRating(stats.getPerformanceRating());
        dto.setTackles(stats.getTackles());
        dto.setInterceptions(stats.getInterceptions());
        dto.setPassesCompleted(stats.getPassesCompleted());
        dto.setDistanceCovered(stats.getDistanceCovered());
        dto.setAverageSpeed(stats.getAverageSpeed());
        
        return dto;
    }

    /**
     * Gets all player statistics as DTOs.
     * 
     * @param playerId The player ID
     * @return List of PlayerStatsDTO
     */
    public List<PlayerStatsDTO> getPlayerStatsAsDTOs(Long playerId) {
        return getPlayerStatistics(playerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets all player statistics as DTOs.
     *
     * @return List of PlayerStatsDTO records
     */
    public List<PlayerStatsDTO> getAllPlayerStatsAsDTOs() {
        return getAllPlayerStats().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets all player statistics as DTOs from a list of PlayerStats.
     * 
     * @param stats List of PlayerStats entities
     * @return List of PlayerStatsDTO
     */
    public List<PlayerStatsDTO> getPlayerStatsAsDTOs(List<PlayerStats> stats) {
        return stats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific player stats record by ID.
     * 
     * @param id The player stats ID
     * @return PlayerStats record
     */
    public PlayerStats getPlayerStatsById(Long id) {
        return playerStatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player stats not found with id: %d", id)
                ));
    }

    /**
     * Updates a player stats record.
     * 
     * @param id The player stats ID
     * @param updatedStats Updated statistics
     * @return Updated PlayerStats
     */
    public PlayerStats updatePlayerStats(Long id, PlayerStats updatedStats) {
        PlayerStats stats = getPlayerStatsById(id);

        if (updatedStats.getJoueur() != null && updatedStats.getJoueur().getId() != null) {
            stats.setJoueur(resolvePlayer(updatedStats.getJoueur()));
        }
        if (updatedStats.getMatch() != null && updatedStats.getMatch().getId() != null) {
            stats.setMatch(resolveMatch(updatedStats.getMatch()));
        }
        
        if (updatedStats.getGoals() >= 0) {
            stats.setGoals(updatedStats.getGoals());
        }
        if (updatedStats.getAssists() >= 0) {
            stats.setAssists(updatedStats.getAssists());
        }
        if (updatedStats.getMinutesPlayed() >= 0) {
            stats.setMinutesPlayed(updatedStats.getMinutesPlayed());
        }
        if (updatedStats.getPerformanceRating() >= 0) {
            stats.setPerformanceRating(updatedStats.getPerformanceRating());
        }
        
        return playerStatsRepository.save(stats);
    }

    /**
     * Deletes a player stats record.
     * 
     * @param id The player stats ID
     */
    public void deletePlayerStats(Long id) {
        PlayerStats stats = getPlayerStatsById(id);
        playerStatsRepository.delete(stats);
    }

    /**
     * Gets top goal scorers in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of top players to return
     * @return List of top scorers
     */
    public List<PlayerStatsDTO> getTopScorers(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate,
            int limit) {
        List<PlayerStats> allStats = playerStatsRepository.findAll();
        return allStats.stream()
                .filter(s -> !s.getMatch().getMatchDate().isBefore(startDate) && 
                           !s.getMatch().getMatchDate().isAfter(endDate))
                .sorted((s1, s2) -> Integer.compare(s2.getGoals(), s1.getGoals()))
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets top assist makers in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of top players to return
     * @return List of top assist makers
     */
    public List<PlayerStatsDTO> getTopAssistants(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate,
            int limit) {
        List<PlayerStats> allStats = playerStatsRepository.findAll();
        return allStats.stream()
                .filter(s -> !s.getMatch().getMatchDate().isBefore(startDate) && 
                           !s.getMatch().getMatchDate().isAfter(endDate))
                .sorted((s1, s2) -> Integer.compare(s2.getAssists(), s1.getAssists()))
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets best rated players in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of top players to return
     * @return List of best rated players
     */
    public List<PlayerStatsDTO> getTopRatedPlayers(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate,
            int limit) {
        List<PlayerStats> allStats = playerStatsRepository.findAll();
        return allStats.stream()
                .filter(s -> !s.getMatch().getMatchDate().isBefore(startDate) && 
                           !s.getMatch().getMatchDate().isAfter(endDate))
                .sorted((s1, s2) -> Double.compare(s2.getPerformanceRating(), s1.getPerformanceRating()))
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Joueur resolvePlayer(Joueur joueur) {
        if (joueur == null || joueur.getId() == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        return joueurRepository.findById(joueur.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", joueur.getId())
                ));
    }

    private Match resolveMatch(Match match) {
        if (match == null || match.getId() == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }

        return matchRepository.findById(match.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Match not found with id: %d", match.getId())
                ));
    }
}
