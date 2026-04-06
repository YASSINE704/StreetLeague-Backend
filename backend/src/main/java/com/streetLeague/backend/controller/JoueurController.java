package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.PlayerDTO;
import com.streetLeague.backend.dto.PlayerStatsDTO;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.service.JoueurService;
import com.streetLeague.backend.service.PlayerStatsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing Player (Joueur) operations.
 * 
 * Provides endpoints for player CRUD and statistics retrieval.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "*", maxAge = 3600)
public class JoueurController {

    @Autowired
    private JoueurService joueurService;

    @Autowired
    private PlayerStatsService playerStatsService;

    /**
     * Retrieves all players.
     * 
     * @return List of all players
     */
    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return ResponseEntity.ok(joueurService.getAllPlayersAsDTO());
    }

    /**
     * Retrieves a specific player with details and statistics.
     * 
     * @param id The player ID
     * @return PlayerDTO with player information
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerDetails(@PathVariable Long id) {
        return ResponseEntity.ok(joueurService.getPlayerDetails(id));
    }

    /**
     * Creates a new player.
     * 
     * @param player The player to create
     * @return Created player
     */
    @PostMapping
    public ResponseEntity<?> createPlayer(@Valid @RequestBody Joueur player) {
        try {
            Joueur createdPlayer = joueurService.saveJoueur(player);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(joueurService.getPlayerDetails(createdPlayer.getId()));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Team not found. Please select a valid team.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player could not be created. Please check all fields are filled correctly.");
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Updates an existing player.
     * 
     * @param id The player ID
     * @param playerDetails Updated player details
     * @return Updated player
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @Valid @RequestBody Joueur playerDetails) {
        try {
            Joueur updatedPlayer = joueurService.updateJoueur(id, playerDetails);
            return ResponseEntity.ok(joueurService.getPlayerDetails(updatedPlayer.getId()));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Update failed");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Deletes a player.
     * 
     * @param id The player ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        try {
            joueurService.deleteJoueur(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player could not be deleted.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets player statistics and performance data.
     * 
     * @param playerId The player ID
     * @return Player statistics
     */
    @GetMapping("/{playerId}/statistics")
    public ResponseEntity<List<PlayerStatsDTO>> getPlayerStatistics(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerStatsService.getPlayerStatsAsDTOs(playerId));
    }

}