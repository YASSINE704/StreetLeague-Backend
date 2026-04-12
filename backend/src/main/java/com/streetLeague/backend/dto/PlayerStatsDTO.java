package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Player Statistics in a specific match.
 * 
 * Used for API responses to provide player performance data for a match
 * including goals, assists, minutes played, and performance rating.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsDTO {
    
    private Long id;
    private Long playerId;
    private String playerName;
    private Long matchId;
    private LocalDateTime matchDate;
    private String opponent;
    private int goals;
    private int assists;
    private int minutesPlayed;
    private double performanceRating;
    
}
