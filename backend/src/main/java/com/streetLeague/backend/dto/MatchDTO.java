package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Match information.
 * 
 * Used for API responses to provide match data including teams, terrain,
 * scheduling, and results without exposing unnecessary internal relationships.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    
    private Long id;
    private Long homeTeamId;
    private String homeTeamName;
    private Long awayTeamId;
    private String awayTeamName;
    private Long terrainId;
    private String terrainName;
    private String terrainAddress;
    private LocalDateTime matchDate;
    private MatchStatus status;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private String result; // "Home Win", "Away Win", "Draw", "Not Played"
    
}
