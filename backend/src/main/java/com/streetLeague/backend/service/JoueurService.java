package com.streetLeague.backend.service;

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

/**
 * Service class for managing Joueur (Player) entities.
 * 
 * Provides business logic operations for players including:
 * creating, retrieving, updating, deleting player records,
 * and retrieving player performance statistics.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class JoueurService {

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    /**
     * Retrieves all players from the database.
     * 
     * @return List of all Joueur entities
     */
    public List<Joueur> getAllJoueurs() {
        return joueurRepository.findAll();
    }

    /**
     * Retrieves a specific player by their ID.
     * 
     * @param id The unique identifier of the player
     * @return The Joueur entity with the specified ID
     * @throws ResourceNotFoundException if player is not found
     */
    public Joueur getJoueurById(Long id) {
        return joueurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", id)
                ));
    }

    /**
     * Retrieves player with full details as DTO.
     * 
     * @param id The player ID
     * @return PlayerDTO with complete player information
     */
    public PlayerDTO getPlayerDetails(Long id) {
        Joueur joueur = getJoueurById(id);
        return convertToDTO(joueur);
    }

    /**
     * Creates and saves a new player to the database.
     * 
     * @param joueur The Joueur entity to save
     * @return The saved Joueur entity with generated ID
     */
    public Joueur saveJoueur(Joueur joueur) {
        joueur.setEquipe(resolveEquipe(joueur.getEquipe()));
        return joueurRepository.save(joueur);
    }

    /**
     * Updates an existing player with new details.
     * 
     * @param id The unique identifier of the player to update
     * @param joueurDetails The new player details
     * @return The updated Joueur entity
     * @throws ResourceNotFoundException if player is not found
     */
    public Joueur updateJoueur(Long id, Joueur joueurDetails) {
        Joueur joueur = getJoueurById(id);
        joueur.setNom(joueurDetails.getNom());
        joueur.setAge(joueurDetails.getAge());
        joueur.setNiveau(joueurDetails.getNiveau());
        joueur.setPosition(joueurDetails.getPosition());
        if (joueurDetails.getProfilePicture() != null) {
            joueur.setProfilePicture(joueurDetails.getProfilePicture());
        }

        if (joueurDetails.getEquipe() != null) {
            joueur.setEquipe(resolveEquipe(joueurDetails.getEquipe()));
        }
        return joueurRepository.save(joueur);
    }

    /**
     * Deletes a player from the database.
     * 
     * @param id The unique identifier of the player to delete
     * @throws ResourceNotFoundException if player is not found
     */
    public void deleteJoueur(Long id) {
        Joueur joueur = getJoueurById(id);
        joueurRepository.delete(joueur);
    }

    /**
     * Retrieves player performance statistics.
     * 
     * @param playerId The player ID
     * @return PlayerDTO with aggregated performance statistics
     */
    public PlayerDTO getPlayerStatistics(Long playerId) {
        return getPlayerDetails(playerId);
    }

    public PlayerDTO getCurrentPlayerProfile(String email) {
        Joueur joueur = joueurRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Player profile not found for user: %s", email)
                ));
        return convertToDTO(joueur);
    }

    /**
     * Gets all players as DTOs with statistics.
     * 
     * @return List of PlayerDTOs
     */
    public List<PlayerDTO> getAllPlayersAsDTO() {
        return getAllJoueurs().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts Joueur entity to PlayerDTO with statistics.
     * 
     * @param joueur The player entity
     * @return PlayerDTO with player information
     */
    public PlayerDTO convertToDTO(Joueur joueur) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(joueur.getId());
        dto.setNom(joueur.getNom());
        dto.setAge(joueur.getAge());
        dto.setNiveau(joueur.getNiveau());
        dto.setPosition(joueur.getPosition());
        dto.setProfilePicture(joueur.getProfilePicture());

        if (joueur.getUser() != null) {
            dto.setUserId(joueur.getUser().getIdUser());
            dto.setEmail(joueur.getUser().getEmail());
        }
        
        if (joueur.getEquipe() != null) {
            dto.setEquipeId(joueur.getEquipe().getId());
            dto.setEquipeName(joueur.getEquipe().getNom());
        }
        
        List<PlayerStats> stats = playerStatsRepository.findByJoueurId(joueur.getId());
        if (!stats.isEmpty()) {
            int totalGoals = stats.stream().mapToInt(PlayerStats::getGoals).sum();
            int totalAssists = stats.stream().mapToInt(PlayerStats::getAssists).sum();
            double avgRating = stats.stream()
                    .mapToDouble(PlayerStats::getPerformanceRating)
                    .average()
                    .orElse(0.0);
            
            dto.setTotalGoals(totalGoals);
            dto.setTotalAssists(totalAssists);
            dto.setMatchesPlayed(stats.size());
            dto.setAverageRating(avgRating);
        }
        
        return dto;
    }

    private Equipe resolveEquipe(Equipe equipe) {
        if (equipe == null || equipe.getId() == null) {
            return null;
        }

        return equipeRepository.findById(equipe.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Team not found with id: %d", equipe.getId())
                ));
    }
}
