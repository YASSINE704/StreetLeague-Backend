package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.TerrainDTO;
import com.streetLeague.backend.entity.Terrain;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.TerrainRepository;
import com.streetLeague.backend.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Terrain (Field/Court) entities.
 * 
 * Provides business logic operations for terrains including:
 * creating, retrieving, updating, deleting terrains,
 * checking availability, and managing bookings.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class TerrainService {

    @Autowired
    private TerrainRepository terrainRepository;

    @Autowired
    private MatchRepository matchRepository;

    /**
     * Retrieves all terrains from the database.
     * 
     * @return List of all Terrain entities
     */
    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    /**
     * Retrieves all terrains as DTOs for API responses.
     *
     * @return List of terrain DTOs
     */
    public List<TerrainDTO> getAllTerrainsAsDTO() {
        return getAllTerrains().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific terrain by its ID.
     * 
     * @param id The unique identifier of the terrain
     * @return The Terrain entity with the specified ID
     * @throws ResourceNotFoundException if terrain is not found
     */
    public Terrain getTerrainById(Long id) {
        return terrainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Terrain not found with id: %d", id)
                ));
    }

    /**
     * Retrieves a terrain as DTO.
     *
     * @param id The terrain ID
     * @return Terrain DTO
     */
    public TerrainDTO getTerrainDetails(Long id) {
        return convertToDTO(getTerrainById(id));
    }

    /**
     * Creates and saves a new terrain to the database.
     * 
     * @param terrain The Terrain entity to save
     * @return The saved Terrain entity with generated ID
     */
    public Terrain saveTerrain(Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    /**
     * Updates an existing terrain with new details.
     * 
     * @param id The unique identifier of the terrain to update
     * @param terrainDetails The new terrain details
     * @return The updated Terrain entity
     * @throws ResourceNotFoundException if terrain is not found
     */
    public Terrain updateTerrain(Long id, Terrain terrainDetails) {
        Terrain terrain = getTerrainById(id);
        terrain.setNom(terrainDetails.getNom());
        terrain.setTypeSport(terrainDetails.getTypeSport());
        terrain.setLocation(terrainDetails.getLocation());
        terrain.setAddress(terrainDetails.getAddress());
        terrain.setAvailabilityStart(terrainDetails.getAvailabilityStart());
        terrain.setAvailabilityEnd(terrainDetails.getAvailabilityEnd());
        return terrainRepository.save(terrain);
    }

    /**
     * Deletes a terrain from the database.
     * 
     * @param id The unique identifier of the terrain to delete
     * @throws ResourceNotFoundException if terrain is not found
     */
    public void deleteTerrain(Long id) {
        Terrain terrain = getTerrainById(id);
        terrainRepository.delete(terrain);
    }

    /**
     * Checks if a terrain is available at a specific date/time.
     * 
     * @param terrainId The terrain ID
     * @param dateTime The date and time to check
     * @return true if available, false if booked
     */
    public boolean isTerrainAvailable(Long terrainId, LocalDateTime dateTime) {
        Terrain terrain = getTerrainById(terrainId);
        
        if (terrain.getAvailabilityStart() != null && terrain.getAvailabilityEnd() != null) {
            LocalDateTime start = terrain.getAvailabilityStart().withYear(dateTime.getYear())
                    .withMonth(dateTime.getMonthValue()).withDayOfMonth(dateTime.getDayOfMonth());
            LocalDateTime end = terrain.getAvailabilityEnd().withYear(dateTime.getYear())
                    .withMonth(dateTime.getMonthValue()).withDayOfMonth(dateTime.getDayOfMonth());
            
            if (dateTime.isBefore(start) || dateTime.isAfter(end)) {
                return false;
            }
        }
        
        List<Match> matches = matchRepository.findByTerrainId(terrainId);
        for (Match match : matches) {
            LocalDateTime matchStart = match.getMatchDate();
            LocalDateTime matchEnd = match.getMatchDate().plusHours(2);
            
            if (!dateTime.isBefore(matchStart) && !dateTime.isAfter(matchEnd)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Gets all bookings (matches) for a terrain.
     * 
     * @param terrainId The terrain ID
     * @return List of matches booked on this terrain
     */
    public List<Match> getTerrainBookings(Long terrainId) {
        getTerrainById(terrainId);
        return matchRepository.findByTerrainId(terrainId);
    }

    /**
     * Checks availability of a terrain within a date range.
     * 
     * @param terrainId The terrain ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of available time slots (represented by LocalDateTime)
     */
    public List<LocalDateTime> checkAvailability(Long terrainId, LocalDateTime startDate, LocalDateTime endDate) {
        List<LocalDateTime> availableSlots = new java.util.ArrayList<>();
        
        LocalDateTime current = startDate;
        while (current.isBefore(endDate)) {
            if (isTerrainAvailable(terrainId, current)) {
                availableSlots.add(current);
            }
            current = current.plusHours(1);
        }
        
        return availableSlots;
    }

    /**
     * Converts a terrain entity to a DTO safe for API responses.
     *
     * @param terrain The terrain entity
     * @return Terrain DTO
     */
    public TerrainDTO convertToDTO(Terrain terrain) {
        TerrainDTO dto = new TerrainDTO();
        dto.setId(terrain.getId());
        dto.setNom(terrain.getNom());
        dto.setTypeSport(terrain.getTypeSport());
        dto.setLocation(terrain.getLocation());
        dto.setAddress(terrain.getAddress());
        dto.setAvailabilityStart(terrain.getAvailabilityStart());
        dto.setAvailabilityEnd(terrain.getAvailabilityEnd());
        return dto;
    }
}
