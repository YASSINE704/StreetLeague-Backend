package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.TypeSport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Team (Equipe) information.
 * 
 * Used for API responses to provide team data including players
 * and statistics without exposing unnecessary internal relationships.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    
    private Long id;
    private String nom;
    private TypeSport typeSport;
    private List<PlayerDTO> joueurs;
    private TeamStatisticsDTO statistics;
    private int totalPlayers;
    
}
