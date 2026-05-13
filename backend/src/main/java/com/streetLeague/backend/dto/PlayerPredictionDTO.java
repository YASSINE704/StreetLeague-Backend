package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Player Performance Predictions.
 * 
 * Contains predicted performance metrics for a player's next match based on
 * historical performance analysis using linear regression.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPredictionDTO {

    /**
     * The player ID being predicted.
     */
    private Long playerId;

    /**
     * Predicted overall performance rating (0-100 scale).
     * Calculated from historical averages, trend, and recent form.
     */
    private double predictedPerformanceRating;

    /**
     * Predicted goals for the next match.
     * Based on average goals scored with trend adjustment.
     */
    private double predictedGoals;

    /**
     * Predicted assists for the next match.
     * Based on average assists with trend adjustment.
     */
    private double predictedAssists;

    /**
     * Predicted tackles for the next match.
     */
    private double predictedTackles;

    /**
     * Predicted interceptions for the next match.
     */
    private double predictedInterceptions;

    /**
     * Historical average performance rating (0-10 scale).
     * Used for comparison with prediction.
     */
    private double averageRating;

    /**
     * Historical average goals per match.
     */
    private double averageGoals;

    /**
     * Historical average assists per match.
     */
    private double averageAssists;

    /**
     * Total number of matches analyzed for this prediction.
     */
    private int totalMatchesAnalyzed;

    /**
     * Performance category based on predicted rating.
     * Values: VERY_BAD (0-20), BAD (21-40), AVERAGE (41-60),
     *         GOOD (61-75), EXCELLENT (76-85), LEGEND (86-100)
     */
    private String performanceCategory;

    /**
     * Reliability of the prediction as a percentage (0-100).
     * Higher = more matches analyzed = more reliable.
     */
    private double reliability;

    /**
     * Trend direction based on performance history.
     * Values: IMPROVING, STABLE, DECLINING, INSUFFICIENT_DATA, UNKNOWN
     */
    private String trendDirection;

}
