package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Team Statistics.
 * 
 * Provides aggregate statistics for a team including wins, losses,
 * goals scored and conceded, and calculated metrics.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamStatisticsDTO {
    
    private Long teamId;
    private String teamName;
    private int wins;
    private int losses;
    private int draws;
    private int totalMatches;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private double winPercentage;
    private int points; // wins * 3 + draws
    
}
