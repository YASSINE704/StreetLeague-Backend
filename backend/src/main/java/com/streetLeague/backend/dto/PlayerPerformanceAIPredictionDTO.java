package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Response DTO for AI-powered Player Performance Prediction.
 * 
 * Contains the prediction results from the Python AI service,
 * including the predicted performance rating, category, and analysis.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPerformanceAIPredictionDTO {

    /**
     * Player ID for which the prediction was made.
     */
    private Long playerId;

    /**
     * Predicted performance rating (0-100 scale).
     * Based on ML model analysis of player statistics.
     */
    private double predictedPerformanceRating;

    /**
     * Performance category classification.
     * Values: VERY_BAD, BAD, AVERAGE, GOOD, EXCELLENT, LEGEND
     */
    private String performanceCategory;

    /**
     * Human-readable interpretation of the prediction.
     * Example: "Performance attendue excellente"
     */
    private String interpretation;

    /**
     * List of player strengths identified by the model.
     * Example: ["Attaque (buts)", "Endurance"]
     */
    private List<String> strengths;

    /**
     * List of player weaknesses identified by the model.
     * Example: ["Discipline (fautes)", "Précision"]
     */
    private List<String> weaknesses;

    /**
     * Confidence level of the prediction.
     * Values: HIGH, MEDIUM, LOW
     */
    private String confidence;

    /**
     * Algorithm used for prediction.
     * Example: "Gradient Boosting Regressor"
     */
    private String algorithm;

}
