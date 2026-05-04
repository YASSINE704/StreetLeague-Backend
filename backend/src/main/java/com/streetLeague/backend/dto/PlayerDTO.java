package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.Niveau;
import com.streetLeague.backend.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Player (Joueur) information.
 * 
 * Used for API responses to provide player data without exposing
 * unnecessary internal relationships or sensitive information.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    
    private Long id;
    private String nom;
    private int age;
    private Niveau niveau;
    private Position position;
    private Integer userId;
    private String email;
    private String profilePicture;
    private Long equipeId;
    private String equipeName;
    private int totalGoals;
    private int totalAssists;
    private int matchesPlayed;
    private double averageRating;
    
}
