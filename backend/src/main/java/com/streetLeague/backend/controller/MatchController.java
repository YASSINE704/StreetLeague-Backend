package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.MatchDTO;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.service.MatchService;
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
import java.util.stream.Collectors;

/**
 * REST Controller for managing Match operations.
 * 
 * Provides endpoints for match scheduling, result recording, and match history.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * Retrieves all matches.
     * 
     * @return List of all matches
     */
    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatchesAsDTO());
    }

    /**
     * Retrieves a specific match by ID.
     * 
     * @param id The match ID
     * @return Match details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMatchDetails(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(matchService.getMatchDetails(id));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Schedules a new match.
     * 
     * @param match The match to schedule
     * @return Created match
     */
    @PostMapping
    public ResponseEntity<?> scheduleMatch(@Valid @RequestBody Match match) {
        try {
            Match createdMatch = matchService.saveMatch(match);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(matchService.convertToDTO(createdMatch));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The match could not be scheduled. Please check all fields are filled correctly.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Updates a match.
     * 
     * @param id The match ID
     * @param matchDetails Updated match details
     * @return Updated match
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable Long id, @Valid @RequestBody Match matchDetails) {
        try {
            Match updatedMatch = matchService.updateMatch(id, matchDetails);
            return ResponseEntity.ok(matchService.convertToDTO(updatedMatch));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The match could not be updated.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Records the result of a completed match.
     * 
     * @param id The match ID
     * @param homeTeamScore Score for home team
     * @param awayTeamScore Score for away team
     * @return Updated match with result
     */
    @PutMapping("/{id}/result")
    public ResponseEntity<?> recordMatchResult(
            @PathVariable Long id,
            @RequestParam int homeTeamScore,
            @RequestParam int awayTeamScore) {
        try {
            Match match = matchService.recordMatchResult(id, homeTeamScore, awayTeamScore);
            return ResponseEntity.ok(matchService.convertToDTO(match));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The match result could not be recorded.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Deletes a match.
     * 
     * @param id The match ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable Long id) {
        try {
            matchService.deleteMatch(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The match could not be deleted.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Gets all scheduled matches.
     * 
     * @return List of scheduled matches
     */
    @GetMapping("/search/scheduled")
    public ResponseEntity<List<MatchDTO>> getScheduledMatches() {
        return ResponseEntity.ok(
            matchService.getScheduledMatches().stream()
                    .map(matchService::convertToDTO)
                    .collect(Collectors.toList())
        );
    }

    /**
     * Gets match history (completed matches).
     * 
     * @return List of completed matches
     */
    @GetMapping("/search/history")
    public ResponseEntity<List<MatchDTO>> getMatchHistory() {
        return ResponseEntity.ok(
            matchService.getMatchHistory().stream()
                    .map(matchService::convertToDTO)
                    .collect(Collectors.toList())
        );
    }

    /**
     * Gets matches for a specific team.
     * 
     * @param teamId The team ID
     * @return List of matches involving this team
     */
    @GetMapping("/search/team/{teamId}")
    public ResponseEntity<List<MatchDTO>> getTeamMatches(@PathVariable Long teamId) {
        return ResponseEntity.ok(
            matchService.getTeamMatches(teamId).stream()
                    .map(matchService::convertToDTO)
                    .collect(Collectors.toList())
        );
    }

    /**
     * Gets matches played on a specific terrain.
     * 
     * @param terrainId The terrain ID
     * @return List of matches on this terrain
     */
    @GetMapping("/search/terrain/{terrainId}")
    public ResponseEntity<List<MatchDTO>> getTerrainMatches(@PathVariable Long terrainId) {
        return ResponseEntity.ok(
            matchService.getTerrainMatches(terrainId).stream()
                    .map(matchService::convertToDTO)
                    .collect(Collectors.toList())
        );
    }

    /**
     * Gets matches within a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of matches in the range
     */
    @GetMapping("/search/range")
    public ResponseEntity<List<MatchDTO>> getMatchesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(
            matchService.getMatchesByDateRange(startDate, endDate).stream()
                    .map(matchService::convertToDTO)
                    .collect(Collectors.toList())
        );
    }
}
