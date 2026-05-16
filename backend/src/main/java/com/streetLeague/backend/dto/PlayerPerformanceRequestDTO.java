package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for AI-powered Player Performance Prediction.
 * 
 * Contains current player statistics to be sent to the Python AI service
 * for advanced performance prediction using machine learning models.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPerformanceRequestDTO {

    /**
     * Player ID for which to predict performance.
     */
    private Long playerId;

    /**
     * Number of goals scored in the reference period.
     */
    private int goals;

    /**
     * Number of assists provided.
     */
    private int assists;

    /**
     * Number of tackles made.
     */
    private int tackles;

    /**
     * Number of interceptions made.
     */
    private int interceptions;

    /**
     * Number of completed passes.
     */
    private int passesCompleted;

    /**
     * Pass accuracy percentage (0-100).
     */
    private double passAccuracy;

    /**
     * Distance covered in kilometers.
     */
    private double distanceCoveredKm;

    /**
     * Average speed in kilometers per hour.
     */
    private double averageSpeedKmh;

    /**
     * Ball possession percentage (0-100).
     */
    private double ballPossessionPercent;

    /**
     * Number of fouls committed.
     */
    private int foulsCommitted;

    /**
     * Number of shots on target.
     */
    private int shotsOnTarget;

}
