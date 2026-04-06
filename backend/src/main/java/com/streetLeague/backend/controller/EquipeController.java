package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.TeamDTO;
import com.streetLeague.backend.dto.TeamStatisticsDTO;
import com.streetLeague.backend.dto.PlayerDTO;
import com.streetLeague.backend.entity.Equipe;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.service.EquipeService;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for managing Team (Equipe) operations.
 * 
 * Provides endpoints for team CRUD operations, player management,
 * statistics retrieval, and lineup management.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    /**
     * Retrieves all teams.
     * 
     * @return List of all teams
     */
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok(equipeService.getAllTeamsAsDTO());
    }

    /**
     * Retrieves a specific team with full details.
     * 
     * @param id The team ID
     * @return TeamDTO with team information and statistics
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamDetails(@PathVariable Long id) {
        return ResponseEntity.ok(equipeService.getTeamWithDetails(id));
    }

    /**
     * Creates a new team.
     * 
     * @param team The team to create
     * @return Created team
     */
    @PostMapping
    public ResponseEntity<?> createTeam(@Valid @RequestBody Equipe team) {
        try {
            Equipe createdTeam = equipeService.saveEquipe(team);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(equipeService.getTeamWithDetails(createdTeam.getId()));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The team could not be created. Please check all fields are filled correctly.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Updates an existing team.
     * 
     * @param id The team ID
     * @param teamDetails Updated team details
     * @return Updated team
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable Long id, @Valid @RequestBody Equipe teamDetails) {
        try {
            Equipe updatedTeam = equipeService.updateEquipe(id, teamDetails);
            return ResponseEntity.ok(equipeService.getTeamWithDetails(updatedTeam.getId()));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The team could not be updated.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Deletes a team.
     * 
     * @param id The team ID
     * @return 204 No Content or error response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        try {
            equipeService.deleteEquipe(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The team could not be deleted. The team may have players assigned or other constraints preventing deletion.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Adds a player to a team.
     * 
     * @param teamId The team ID
     * @param playerId The player ID to add
     * @return Updated team
     */
    @PostMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<?> addPlayerToTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        try {
            Equipe team = equipeService.addPlayerToTeam(teamId, playerId);
            return ResponseEntity.ok(equipeService.getTeamWithDetails(team.getId()));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player could not be added to the team.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Removes a player from a team.
     * 
     * @param teamId The team ID
     * @param playerId The player ID to remove
     * @return Updated team
     */
    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<?> removePlayerFromTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        try {
            Equipe team = equipeService.removePlayerFromTeam(teamId, playerId);
            return ResponseEntity.ok(equipeService.getTeamWithDetails(team.getId()));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player could not be removed from the team.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets the current team lineup.
     * 
     * @param teamId The team ID
     * @return List of players in the team
     */
    @GetMapping("/{teamId}/lineup")
    public ResponseEntity<?> getTeamLineup(@PathVariable Long teamId) {
        try {
            List<PlayerDTO> lineup = equipeService.getTeamLineupAsDTO(teamId);
            return ResponseEntity.ok(lineup);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Gets team statistics.
     * 
     * @param teamId The team ID
     * @return Team statistics DTO
     */
    @GetMapping("/{teamId}/statistics")
    public ResponseEntity<?> getTeamStatistics(@PathVariable Long teamId) {
        try {
            TeamStatisticsDTO stats = equipeService.getTeamStatistics(teamId);
            return ResponseEntity.ok(stats);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}