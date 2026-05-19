package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.PlayerStatsDTO;
import com.streetLeague.backend.dto.PlayerPerformanceRequestDTO;
import com.streetLeague.backend.entity.PlayerStats;
import com.streetLeague.backend.service.PlayerStatsService;
import com.streetLeague.backend.service.PlayerStatsSeedService;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing Player Statistics.
 * 
 * Provides endpoints for viewing and recording player performance metrics per match.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestController
@RequestMapping("/player-stats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlayerStatsController {

    @Autowired
    private PlayerStatsService playerStatsService;

    @Autowired
    private PlayerStatsSeedService playerStatsSeedService;

    /**
     * Retrieves all player statistics.
     * 
     * @return List of all player statistics records
     */
    @GetMapping
    public ResponseEntity<List<PlayerStatsDTO>> getAllPlayerStats() {
        List<PlayerStatsDTO> allStats = playerStatsService.getAllPlayerStatsAsDTOs();
        return ResponseEntity.ok(allStats);
    }

    /**
     * Retrieves a specific player stats record.
     * 
     * @param id The player stats ID
     * @return Player statistics record as DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerStatsById(@PathVariable Long id) {
        try {
            PlayerStats stats = playerStatsService.getPlayerStatsById(id);
            return ResponseEntity.ok(playerStatsService.convertToDTO(stats));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Records player performance for a match.
     * 
     * @param playerStats The performance record to create
     * @return Created player stats
     */
    @PostMapping
    public ResponseEntity<?> recordPlayerStats(
            @Valid @RequestBody PlayerStats playerStats) {
        try {
            PlayerStats savedStats = playerStatsService.savePlayerStats(playerStats);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(playerStatsService.convertToDTO(savedStats));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player statistics could not be recorded. Please check all fields are filled correctly.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Saves a custom AI prediction scenario as a player statistics snapshot.
     * This is not attached to an official match; it appears as "Simulation IA".
     */
    @PostMapping("/simulation")
    public ResponseEntity<?> saveSimulationStats(@RequestBody PlayerPerformanceRequestDTO request) {
        try {
            if (request.getPlayerId() == null || request.getPlayerId() <= 0) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Player ID requis et doit être positif"));
            }

            PlayerStats savedStats = playerStatsService.saveSimulationStats(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(playerStatsService.convertToDTO(savedStats));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The AI simulation could not be saved as player statistics.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Updates a player stats record (for corrections).
     * 
     * @param id The player stats ID
     * @param playerStats Updated statistics
     * @return Updated player stats as DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayerStats(
            @PathVariable Long id,
            @Valid @RequestBody PlayerStats playerStats) {
        try {
            PlayerStats updatedStats = playerStatsService.updatePlayerStats(id, playerStats);
            return ResponseEntity.ok(playerStatsService.convertToDTO(updatedStats));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player statistics could not be updated.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Deletes a player stats record.
     * 
     * @param id The player stats ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayerStats(@PathVariable Long id) {
        try {
            playerStatsService.deletePlayerStats(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The player statistics could not be deleted.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets all statistics for a specific player.
     * 
     * @param playerId The player ID
     * @return List of player's career statistics
     */
    @GetMapping("/search/player/{playerId}")
    public ResponseEntity<?> getPlayerStatistics(@PathVariable Long playerId) {
        try {
            List<PlayerStats> playerStats = playerStatsService.getPlayerStatistics(playerId);
            List<PlayerStatsDTO> dtos = playerStatsService.getPlayerStatsAsDTOs(playerStats);
            
            int totalGoals = dtos.stream().mapToInt(PlayerStatsDTO::getGoals).sum();
            int totalAssists = dtos.stream().mapToInt(PlayerStatsDTO::getAssists).sum();
            int totalMinutes = dtos.stream().mapToInt(PlayerStatsDTO::getMinutesPlayed).sum();
            double avgRating = dtos.stream()
                    .mapToDouble(PlayerStatsDTO::getPerformanceRating)
                    .average()
                    .orElse(0.0);
            
            Map<String, Object> response = new HashMap<>();
            response.put("playerId", playerId);
            response.put("totalMatches", dtos.size());
            response.put("totalGoals", totalGoals);
            response.put("totalAssists", totalAssists);
            response.put("totalMinutesPlayed", totalMinutes);
            response.put("averageRating", avgRating);
            response.put("statistics", dtos);
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Gets all player statistics for a specific match.
     * 
     * @param matchId The match ID
     * @return List of performance records from that match
     */
    @GetMapping("/search/match/{matchId}")
    public ResponseEntity<?> getMatchStatistics(@PathVariable Long matchId) {
        try {
            List<PlayerStats> matchStats = playerStatsService.getMatchStatistics(matchId);
            List<PlayerStatsDTO> dtos = playerStatsService.getPlayerStatsAsDTOs(matchStats);
            return ResponseEntity.ok(dtos);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Records player performance for multiple players in a match.
     * 
     * @param statsRecords List of performance records
     * @return List of created records as DTOs
     */
    @PostMapping("/batch")
    public ResponseEntity<?> recordBatchPlayerStats(
            @Valid @RequestBody List<PlayerStats> statsRecords) {
        try {
            List<PlayerStats> savedStats = statsRecords.stream()
                    .map(playerStatsService::savePlayerStats)
                    .toList();
            List<PlayerStatsDTO> dtos = playerStatsService.getPlayerStatsAsDTOs(savedStats);
            return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Batch player statistics could not be recorded. Please check all fields are filled correctly.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets top performers based on goals in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of top players to return
     * @return Top performers
     */
    @GetMapping("/toppers/goals")
    public ResponseEntity<?> getTopScorers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<PlayerStatsDTO> topScorers = playerStatsService.getTopScorers(startDate, endDate, limit);
            return ResponseEntity.ok(topScorers);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Could not retrieve top scorers.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets top performers based on assists in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of top players to return
     * @return Top assist makers
     */
    @GetMapping("/toppers/assists")
    public ResponseEntity<?> getTopAssistants(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<PlayerStatsDTO> topAssists = playerStatsService.getTopAssistants(startDate, endDate, limit);
            return ResponseEntity.ok(topAssists);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Could not retrieve top assist makers.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets best rated players in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of top players to return
     * @return Best rated players
     */
    @GetMapping("/toppers/rating")
    public ResponseEntity<?> getTopRatedPlayers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<PlayerStatsDTO> topRated = playerStatsService.getTopRatedPlayers(startDate, endDate, limit);
            return ResponseEntity.ok(topRated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Could not retrieve top rated players.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * ADMIN ENDPOINT: Reseeds comprehensive test data for AI predictions testing.
     * This endpoint allows manual triggering of test data generation with 8+ matches per player.
     * 
     * Useful for:
     * - Testing AI prediction features with sufficient historical data
     * - Trend analysis (IMPROVING, STABLE, DECLINING)
     * - Complete re-initialization of test data
     * 
     * @return Status message and number of records created
     */
    @PostMapping("/admin/reseed-test-data")
    public ResponseEntity<?> reseedTestData() {
        try {
            playerStatsSeedService.run(); // Trigger seeding
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "✅ Test data reseeded successfully!");
            response.put("note", "📊 AI predictions now have comprehensive historical data with trend analysis");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Test data reseeding failed");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
