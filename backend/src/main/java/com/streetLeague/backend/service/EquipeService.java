package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.TeamDTO;
import com.streetLeague.backend.dto.TeamStatisticsDTO;
import com.streetLeague.backend.dto.PlayerDTO;
import com.streetLeague.backend.entity.Equipe;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.entity.PlayerStats;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.EquipeRepository;
import com.streetLeague.backend.repository.JoueurRepository;
import com.streetLeague.backend.repository.PlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * Service class for managing Equipe (Team) entities.
 * 
 * Provides business logic operations for teams including:
 * creating, retrieving, updating, deleting teams, managing players,
 * and calculating team statistics.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    /**
     * Retrieves all teams from the database.
     * 
     * @return List of all Equipe entities
     */
    public List<Equipe> getAllEquipes() {
        return equipeRepository.findAll();
    }

    /**
     * Gets all teams as DTOs for API responses.
     *
     * @return List of TeamDTO objects
     */
    public List<TeamDTO> getAllTeamsAsDTO() {
        return getAllEquipes().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific team by its ID.
     * 
     * @param id The unique identifier of the team
     * @return The Equipe entity with the specified ID
     * @throws ResourceNotFoundException if team is not found
     */
    public Equipe getEquipeById(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Team not found with id: %d", id)
                ));
    }

    /**
     * Retrieves a team with full DTO including statistics and players.
     * 
     * @param id The team ID
     * @return TeamDTO with complete team information
     */
    public TeamDTO getTeamWithDetails(Long id) {
        Equipe equipe = getEquipeById(id);
        return convertToDTO(equipe);
    }

    /**
     * Creates and saves a new team to the database.
     * 
     * @param equipe The Equipe entity to save
     * @return The saved Equipe entity with generated ID
     */
    public Equipe saveEquipe(Equipe equipe) {
        // Initialize statistics
        if (equipe.getWins() == 0 && equipe.getLosses() == 0) {
            equipe.setWins(0);
            equipe.setLosses(0);
            equipe.setDraws(0);
            equipe.setGoalsFor(0);
            equipe.setGoalsAgainst(0);
        }
        return equipeRepository.save(equipe);
    }

    /**
     * Updates an existing team with new details.
     * 
     * @param id The unique identifier of the team to update
     * @param equipeDetails The new team details
     * @return The updated Equipe entity
     * @throws ResourceNotFoundException if team is not found
     */
    public Equipe updateEquipe(Long id, Equipe equipeDetails) {
        Equipe equipe = getEquipeById(id);
        equipe.setNom(equipeDetails.getNom());
        equipe.setTypeSport(equipeDetails.getTypeSport());
        return equipeRepository.save(equipe);
    }

    /**
     * Deletes a team from the database.
     * 
     * @param id The unique identifier of the team to delete
     * @throws ResourceNotFoundException if team is not found
     */
    public void deleteEquipe(Long id) {
        Equipe equipe = getEquipeById(id);
        equipeRepository.delete(equipe);
    }

    /**
     * Adds a player to a team.
     * 
     * @param teamId The team ID
     * @param playerId The player ID to add
     * @return Updated team
     */
    public Equipe addPlayerToTeam(Long teamId, Long playerId) {
        Equipe equipe = getEquipeById(teamId);
        Joueur joueur = joueurRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", playerId)
                ));
        
        joueur.setEquipe(equipe);
        joueurRepository.save(joueur);
        return equipeRepository.save(equipe);
    }

    /**
     * Removes a player from a team.
     * 
     * @param teamId The team ID
     * @param playerId The player ID to remove
     * @return Updated team
     */
    public Equipe removePlayerFromTeam(Long teamId, Long playerId) {
        Equipe equipe = getEquipeById(teamId);
        Joueur joueur = joueurRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", playerId)
                ));
        
        joueur.setEquipe(null);
        joueurRepository.save(joueur);
        return equipeRepository.save(equipe);
    }

    /**
     * Gets the current lineup for a team (all assigned players).
     * 
     * @param teamId The team ID
     * @return List of players in the team
     */
    public List<Joueur> getTeamLineup(Long teamId) {
        getEquipeById(teamId);
        return joueurRepository.findByEquipeId(teamId);
    }

    /**
     * Gets the current team lineup as DTOs.
     *
     * @param teamId The team ID
     * @return List of player DTOs in the team
     */
    public List<PlayerDTO> getTeamLineupAsDTO(Long teamId) {
        return getTeamLineup(teamId).stream()
                .map(this::convertPlayerToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves team statistics as a DTO.
     * 
     * @param teamId The team ID
     * @return TeamStatisticsDTO with complete statistics
     */
    public TeamStatisticsDTO getTeamStatistics(Long teamId) {
        Equipe equipe = getEquipeById(teamId);
        return buildTeamStatistics(equipe);
    }

    /**
     * Updates team statistics after a match result is recorded.
     * Called internally when a match is completed.
     * 
     * @param teamId The team ID
     * @param goalsFor Goals scored by the team
     * @param goalsAgainst Goals conceded by the team
     * @param result "WIN", "LOSS", or "DRAW"
     */
    public void updateTeamStats(Long teamId, int goalsFor, int goalsAgainst, String result) {
        int winDelta = 0;
        int lossDelta = 0;
        int drawDelta = 0;

        switch (result.toUpperCase()) {
            case "WIN":
                winDelta = 1;
                break;
            case "LOSS":
                lossDelta = 1;
                break;
            case "DRAW":
                drawDelta = 1;
                break;
        }

        adjustTeamStats(teamId, goalsFor, goalsAgainst, winDelta, lossDelta, drawDelta);
    }

    /**
     * Adjusts team statistics by the provided deltas.
     *
     * @param teamId the team id
     * @param goalsForDelta delta for goals scored
     * @param goalsAgainstDelta delta for goals conceded
     * @param winsDelta delta for wins
     * @param lossesDelta delta for losses
     * @param drawsDelta delta for draws
     */
    public void adjustTeamStats(Long teamId, int goalsForDelta, int goalsAgainstDelta, int winsDelta, int lossesDelta, int drawsDelta) {
        Equipe equipe = getEquipeById(teamId);

        equipe.setGoalsFor(Math.max(0, equipe.getGoalsFor() + goalsForDelta));
        equipe.setGoalsAgainst(Math.max(0, equipe.getGoalsAgainst() + goalsAgainstDelta));
        equipe.setWins(Math.max(0, equipe.getWins() + winsDelta));
        equipe.setLosses(Math.max(0, equipe.getLosses() + lossesDelta));
        equipe.setDraws(Math.max(0, equipe.getDraws() + drawsDelta));

        equipeRepository.save(equipe);
    }

    /**
     * Converts Equipe entity to TeamDTO with all related information.
     * 
     * @param equipe The team entity
     * @return TeamDTO with complete information
     */
    private TeamDTO convertToDTO(Equipe equipe) {
        TeamDTO dto = new TeamDTO();
        dto.setId(equipe.getId());
        dto.setNom(equipe.getNom());
        dto.setTypeSport(equipe.getTypeSport());
        
        // Convert players to DTOs
        List<Joueur> joueurs = equipe.getJoueurs() != null ? equipe.getJoueurs() : Collections.emptyList();
        List<PlayerDTO> playerDTOs = joueurs.stream()
                .map(this::convertPlayerToDTO)
                .collect(Collectors.toList());
        dto.setJoueurs(playerDTOs);
        dto.setTotalPlayers(joueurs.size());
        
        // Add statistics
        dto.setStatistics(buildTeamStatistics(equipe));
        
        return dto;
    }

    /**
     * Converts Joueur entity to PlayerDTO with statistics.
     * 
     * @param joueur The player entity
     * @return PlayerDTO with player information
     */
    private PlayerDTO convertPlayerToDTO(Joueur joueur) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(joueur.getId());
        dto.setNom(joueur.getNom());
        dto.setAge(joueur.getAge());
        dto.setNiveau(joueur.getNiveau());
        dto.setPosition(joueur.getPosition());
        
        if (joueur.getEquipe() != null) {
            dto.setEquipeId(joueur.getEquipe().getId());
            dto.setEquipeName(joueur.getEquipe().getNom());
        }
        
        // Calculate aggregate stats
        if (joueur.getPlayerStats() != null) {
            int totalGoals = joueur.getPlayerStats().stream().mapToInt(PlayerStats::getGoals).sum();
            int totalAssists = joueur.getPlayerStats().stream().mapToInt(PlayerStats::getAssists).sum();
            double avgRating = joueur.getPlayerStats().stream()
                    .mapToDouble(PlayerStats::getPerformanceRating)
                    .average()
                    .orElse(0.0);
            
            dto.setTotalGoals(totalGoals);
            dto.setTotalAssists(totalAssists);
            dto.setMatchesPlayed(joueur.getPlayerStats().size());
            dto.setAverageRating(avgRating);
        }
        
        return dto;
    }

    /**
     * Builds team statistics from team entity.
     * 
     * @param equipe The team entity
     * @return TeamStatisticsDTO with calculated metrics
     */
    private TeamStatisticsDTO buildTeamStatistics(Equipe equipe) {
        TeamStatisticsDTO stats = new TeamStatisticsDTO();
        stats.setTeamId(equipe.getId());
        stats.setTeamName(equipe.getNom());
        stats.setWins(equipe.getWins());
        stats.setLosses(equipe.getLosses());
        stats.setDraws(equipe.getDraws());
        stats.setTotalMatches(equipe.getWins() + equipe.getLosses() + equipe.getDraws());
        stats.setGoalsFor(equipe.getGoalsFor());
        stats.setGoalsAgainst(equipe.getGoalsAgainst());
        stats.setGoalDifference(equipe.getGoalsFor() - equipe.getGoalsAgainst());
        stats.setPoints(equipe.getWins() * 3 + equipe.getDraws());
        
        if (stats.getTotalMatches() > 0) {
            stats.setWinPercentage((double) equipe.getWins() / stats.getTotalMatches() * 100);
        } else {
            stats.setWinPercentage(0.0);
        }
        
        return stats;
    }
}

