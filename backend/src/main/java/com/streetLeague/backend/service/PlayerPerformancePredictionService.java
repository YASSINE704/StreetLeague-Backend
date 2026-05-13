package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.PlayerPredictionDTO;
import com.streetLeague.backend.entity.PlayerStats;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.PlayerStatsRepository;
import com.streetLeague.backend.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for predicting player performance based on historical statistics.
 * 
 * Uses linear regression with weighted recent form to predict:
 * - Overall performance rating (0-100)
 * - Specific metrics (goals, assists)
 * - Performance categories (poor, average, good, excellent, legendary)
 * 
 * Algorithm:
 * Future Performance = Base Average + (Trend × Match Count) + (Recent Form Weight × Recent Performance)
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class PlayerPerformancePredictionService {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private JoueurRepository joueurRepository;

    private static final int MINIMUM_MATCHES = 3; // Minimum matches for prediction
    private static final double RECENT_FORM_WEIGHT = 0.6; // How much to weight recent matches
    private static final double BASE_AVERAGE_WEIGHT = 0.4; // How much to weight historical average

    /**
     * Predicts player performance for the next match.
     * 
     * @param playerId The player ID
     * @return PlayerPredictionDTO with predicted performance and rating
     * @throws ResourceNotFoundException if player not found
     */
    public PlayerPredictionDTO predictPlayerPerformance(Long playerId) {
        // Verify player exists
        joueurRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Player not found with id: %d", playerId)
                ));

        List<PlayerStats> playerHistory = playerStatsRepository.findByJoueurId(playerId);

        if (playerHistory.isEmpty()) {
            return createZeroPrediction(playerId);
        }

        if (playerHistory.size() < MINIMUM_MATCHES) {
            return createInsufficientDataPrediction(playerId, playerHistory);
        }

        // Sort by match date (oldest first)
        playerHistory.sort((a, b) -> a.getMatch().getMatchDate()
                .compareTo(b.getMatch().getMatchDate()));

        return calculatePrediction(playerId, playerHistory);
    }

    /**
     * Calculates the performance prediction using linear regression.
     * 
     * @param playerId The player ID
     * @param playerHistory Sorted list of player statistics
     * @return PlayerPredictionDTO with predicted metrics
     */
    private PlayerPredictionDTO calculatePrediction(Long playerId, List<PlayerStats> playerHistory) {
        PlayerPredictionDTO prediction = new PlayerPredictionDTO();
        prediction.setPlayerId(playerId);
        prediction.setTotalMatchesAnalyzed(playerHistory.size());

        // Calculate averages
        double avgRating = playerHistory.stream()
                .mapToDouble(PlayerStats::getPerformanceRating)
                .average()
                .orElse(0.0);

        double avgGoals = playerHistory.stream()
                .mapToDouble(PlayerStats::getGoals)
                .average()
                .orElse(0.0);

        double avgAssists = playerHistory.stream()
                .mapToDouble(PlayerStats::getAssists)
                .average()
                .orElse(0.0);

        double avgTackles = playerHistory.stream()
                .mapToDouble(PlayerStats::getTackles)
                .average()
                .orElse(0.0);

        double avgInterceptions = playerHistory.stream()
                .mapToDouble(PlayerStats::getInterceptions)
                .average()
                .orElse(0.0);

        double avgPasses = playerHistory.stream()
                .mapToDouble(PlayerStats::getPassesCompleted)
                .average()
                .orElse(0.0);

        double avgDistance = playerHistory.stream()
                .mapToDouble(PlayerStats::getDistanceCovered)
                .average()
                .orElse(0.0);

        double avgSpeed = playerHistory.stream()
                .mapToDouble(PlayerStats::getAverageSpeed)
                .average()
                .orElse(0.0);

        // Calculate trend (linear regression slope)
        double trendRating = calculateTrend(playerHistory, stats -> stats.getPerformanceRating());
        double trendGoals = calculateTrend(playerHistory, stats -> stats.getGoals());
        double trendAssists = calculateTrend(playerHistory, stats -> stats.getAssists());

        // Get recent form (last 3 matches weighted)
        double recentFormRating = calculateRecentForm(playerHistory, stats -> stats.getPerformanceRating());

        // Predict next performance using weighted formula
        double predictedRating = (avgRating * BASE_AVERAGE_WEIGHT)
                + (recentFormRating * RECENT_FORM_WEIGHT)
                + (trendRating * 0.2);

        // Ensure rating is within 0-10 scale
        predictedRating = Math.max(0, Math.min(10, predictedRating));

        // Convert to 0-100 scale
        double predictedPerformance = predictedRating * 10;

        // Predict specific metrics with trend adjustment
        double predictedGoals = Math.max(0, avgGoals + (trendGoals * 0.1));
        double predictedAssists = Math.max(0, avgAssists + (trendAssists * 0.1));
        double predictedTackles = Math.max(0, avgTackles + (calculateTrend(playerHistory, stats -> stats.getTackles()) * 0.1));
        double predictedInterceptions = Math.max(0, avgInterceptions + (calculateTrend(playerHistory, stats -> stats.getInterceptions()) * 0.1));

        // Set predictions
        prediction.setPredictedPerformanceRating(Math.round(predictedPerformance * 10.0) / 10.0);
        prediction.setPredictedGoals(Math.round(predictedGoals * 10.0) / 10.0);
        prediction.setPredictedAssists(Math.round(predictedAssists * 10.0) / 10.0);
        prediction.setPredictedTackles(Math.round(predictedTackles * 10.0) / 10.0);
        prediction.setPredictedInterceptions(Math.round(predictedInterceptions * 10.0) / 10.0);

        // Set historical averages
        prediction.setAverageRating(Math.round(avgRating * 10.0) / 10.0);
        prediction.setAverageGoals(Math.round(avgGoals * 10.0) / 10.0);
        prediction.setAverageAssists(Math.round(avgAssists * 10.0) / 10.0);

        // Determine performance category
        prediction.setPerformanceCategory(getPerformanceCategory(predictedPerformance));
        prediction.setReliability(calculateReliability(playerHistory.size()));
        prediction.setTrendDirection(getTrendDirection(trendRating));

        return prediction;
    }

    /**
     * Calculates trend using simple linear regression.
     * 
     * @param playerHistory List of player statistics
     * @param valueExtractor Function to extract the value to analyze
     * @return Trend coefficient (positive = improving, negative = declining)
     */
    private double calculateTrend(List<PlayerStats> playerHistory,
            java.util.function.Function<PlayerStats, Number> valueExtractor) {
        
        if (playerHistory.size() < 2) return 0;

        double n = playerHistory.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < playerHistory.size(); i++) {
            double x = i;
            double y = valueExtractor.apply(playerHistory.get(i)).doubleValue();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        // Linear regression: slope = (n*sumXY - sumX*sumY) / (n*sumX2 - sumX*sumX)
        double denominator = (n * sumX2) - (sumX * sumX);
        if (denominator == 0) return 0;

        return ((n * sumXY) - (sumX * sumY)) / denominator;
    }

    /**
     * Calculates recent form (weighted average of last 3 matches).
     * 
     * @param playerHistory List of player statistics
     * @param valueExtractor Function to extract the value to analyze
     * @return Weighted average of recent form
     */
    private double calculateRecentForm(List<PlayerStats> playerHistory,
            java.util.function.Function<PlayerStats, Number> valueExtractor) {
        
        int recentCount = Math.min(3, playerHistory.size());
        double sum = 0;
        double weights = 0;

        for (int i = 0; i < recentCount; i++) {
            int matchIndex = playerHistory.size() - 1 - i;
            double weight = (i + 1); // Most recent match has highest weight
            sum += valueExtractor.apply(playerHistory.get(matchIndex)).doubleValue() * weight;
            weights += weight;
        }

        return weights > 0 ? sum / weights : 0;
    }

    /**
     * Determines performance category based on predicted rating.
     * 
     * Categories:
     * 0-20: Very Bad
     * 21-40: Bad
     * 41-60: Average
     * 61-75: Good
     * 76-85: Excellent
     * 86-100: Legend/Pro
     * 
     * @param performanceRating Predicted performance (0-100)
     * @return Performance category
     */
    private String getPerformanceCategory(double performanceRating) {
        if (performanceRating <= 20) return "VERY_BAD";
        if (performanceRating <= 40) return "BAD";
        if (performanceRating <= 60) return "AVERAGE";
        if (performanceRating <= 75) return "GOOD";
        if (performanceRating <= 85) return "EXCELLENT";
        return "LEGEND";
    }

    /**
     * Calculates prediction reliability based on number of matches.
     * More matches = higher reliability.
     * 
     * @param matchCount Number of matches analyzed
     * @return Reliability percentage (0-100)
     */
    private double calculateReliability(int matchCount) {
        // Min 3 matches (30%), Max 15+ matches (95%)
        return Math.min(95, 30 + (matchCount * 5));
    }

    /**
     * Determines trend direction string.
     * 
     * @param trend Trend coefficient
     * @return "IMPROVING", "STABLE", or "DECLINING"
     */
    private String getTrendDirection(double trend) {
        if (trend > 0.2) return "IMPROVING";
        if (trend < -0.2) return "DECLINING";
        return "STABLE";
    }

    /**
     * Creates prediction for player with no match history.
     * 
     * @param playerId The player ID
     * @return Empty prediction DTO
     */
    private PlayerPredictionDTO createZeroPrediction(Long playerId) {
        PlayerPredictionDTO prediction = new PlayerPredictionDTO();
        prediction.setPlayerId(playerId);
        prediction.setTotalMatchesAnalyzed(0);
        prediction.setPerformanceCategory("NO_DATA");
        prediction.setReliability(0);
        prediction.setTrendDirection("UNKNOWN");
        return prediction;
    }

    /**
     * Creates prediction for player with insufficient match history.
     * 
     * @param playerId The player ID
     * @param playerHistory Limited match history
     * @return Prediction with low reliability
     */
    private PlayerPredictionDTO createInsufficientDataPrediction(Long playerId, List<PlayerStats> playerHistory) {
        PlayerPredictionDTO prediction = new PlayerPredictionDTO();
        prediction.setPlayerId(playerId);
        prediction.setTotalMatchesAnalyzed(playerHistory.size());
        
        double avgRating = playerHistory.stream()
                .mapToDouble(PlayerStats::getPerformanceRating)
                .average()
                .orElse(0.0) * 10;

        prediction.setPredictedPerformanceRating(avgRating);
        prediction.setPerformanceCategory(getPerformanceCategory(avgRating));
        prediction.setReliability(10 + (playerHistory.size() * 5));
        prediction.setTrendDirection("INSUFFICIENT_DATA");
        
        return prediction;
    }
}
