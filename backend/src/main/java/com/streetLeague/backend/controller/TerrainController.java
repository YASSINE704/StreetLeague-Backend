package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.TerrainDTO;
import com.streetLeague.backend.entity.Terrain;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.dto.MatchDTO;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.service.MatchService;
import com.streetLeague.backend.service.TerrainService;
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
 * REST Controller for managing Terrain (Field/Court) operations.
 * 
 * Provides endpoints for terrain management, availability checking, and booking.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestController
@RequestMapping("/terrains")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TerrainController {

    @Autowired
    private TerrainService terrainService;

    @Autowired
    private MatchService matchService;

    /**
     * Retrieves all terrains.
     * 
     * @return List of all terrains
     */
    @GetMapping
    public ResponseEntity<List<TerrainDTO>> getAllTerrains() {
        return ResponseEntity.ok(terrainService.getAllTerrainsAsDTO());
    }

    /**
     * Retrieves a specific terrain.
     * 
     * @param id The terrain ID
     * @return Terrain details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTerrainDetails(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(terrainService.getTerrainDetails(id));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Creates a new terrain.
     * 
     * @param terrain The terrain to create
     * @return Created terrain
     */
    @PostMapping
    public ResponseEntity<?> createTerrain(@Valid @RequestBody Terrain terrain) {
        try {
            Terrain createdTerrain = terrainService.saveTerrain(terrain);
            return ResponseEntity.status(HttpStatus.CREATED).body(terrainService.convertToDTO(createdTerrain));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The terrain could not be created. Please check all fields are filled correctly.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Updates a terrain.
     * 
     * @param id The terrain ID
     * @param terrainDetails Updated details
     * @return Updated terrain
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTerrain(@PathVariable Long id, @Valid @RequestBody Terrain terrainDetails) {
        try {
            Terrain updatedTerrain = terrainService.updateTerrain(id, terrainDetails);
            return ResponseEntity.ok(terrainService.convertToDTO(updatedTerrain));
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The terrain could not be updated.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Deletes a terrain.
     * 
     * @param id The terrain ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerrain(@PathVariable Long id) {
        try {
            terrainService.deleteTerrain(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "The terrain could not be deleted.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Checks if a terrain is available at a specific date/time.
     * 
     * @param id The terrain ID
     * @param dateTime The date-time to check
     * @return Availability status
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<?> checkAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        try {
            Terrain terrain = terrainService.getTerrainById(id);
            boolean available = terrainService.isTerrainAvailable(id, dateTime);
            
            Map<String, Object> response = new HashMap<>();
            response.put("terrainId", id);
            response.put("terrainName", terrain.getNom());
            response.put("dateTime", dateTime);
            response.put("available", available);
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Gets all bookings (matches) for a terrain.
     * 
     * @param id The terrain ID
     * @return List of matches booked on this terrain
     */
    @GetMapping("/{id}/bookings")
    public ResponseEntity<?> getTerrainBookings(@PathVariable Long id) {
        try {
            List<Match> bookings = terrainService.getTerrainBookings(id);
            return ResponseEntity.ok(
                    bookings.stream()
                            .map(matchService::convertToDTO)
                            .toList()
            );
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Checks terrain availability within a date range.
     * 
     * @param id The terrain ID
     * @param startDate Start date
     * @param endDate End date
     * @return Available time slots
     */
    @GetMapping("/{id}/availability/range")
    public ResponseEntity<?> checkAvailabilityRange(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Terrain terrain = terrainService.getTerrainById(id);
            List<LocalDateTime> availableSlots = terrainService.checkAvailability(id, startDate, endDate);
            
            Map<String, Object> response = new HashMap<>();
            response.put("terrainId", id);
            response.put("terrainName", terrain.getNom());
            response.put("startDate", startDate);
            response.put("endDate", endDate);
            response.put("availableSlots", availableSlots);
            response.put("totalSlots", availableSlots.size());
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}

