package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.PlayerPerformanceAIPredictionDTO;
import com.streetLeague.backend.dto.PlayerPerformanceRequestDTO;
import com.streetLeague.backend.dto.PlayerPredictionDTO;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.service.PlayerPerformanceAIService;
import com.streetLeague.backend.service.PlayerPerformancePredictionService;
import com.streetLeague.backend.service.CoachingRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Player Performance Prediction feature.
 * 
 * Provides endpoints for predicting player performance based on:
 * 1. Historical data analysis (Linear Regression)
 * 2. ML-powered predictions (Random Forest / Gradient Boosting)
 * 
 * Endpoints:
 * - GET  /{playerId}/prediction              : Basic prediction from history
 * - GET  /{playerId}/ai-prediction           : Advanced AI prediction
 * - POST /ai-predict-custom                  : Custom AI prediction with provided stats
 * - GET  /ai/health                          : AI service availability
 * 
 * Architecture:
 * - Controller validates requests
 * - Calls PlayerPerformancePredictionService for basic predictions
 * - Calls PlayerPerformanceAIService for advanced ML predictions
 * - Both services are independent and can work separately
 * 
 * Security:
 * - Most endpoints accessible to all users (read-only)
 * - No special role required for predictions
 * - Rate limiting should be configured at proxy level
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PlayerPerformancePredictionController {

    private final PlayerPerformancePredictionService basicPredictionService;
    private final PlayerPerformanceAIService aiPredictionService;
    private final CoachingRoleService roleService;

    /**
     * Get basic player performance prediction based on historical data.
     * 
     * Uses linear regression with weighted recent form to predict next match performance.
     * Requires minimum 3 matches for reliable predictions.
     * 
     * @param playerId Player ID
     * @return PlayerPredictionDTO with historical averages, trend, and predicted metrics
     */
    @GetMapping("/{playerId}/prediction")
    public ResponseEntity<?> getPrediction(@PathVariable Long playerId) {
        try {
            log.info("[Prediction] Basic prediction requested for player {}", playerId);
            
            PlayerPredictionDTO prediction = basicPredictionService.predictPlayerPerformance(playerId);
            
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "type", "BASIC_PREDICTION",
                    "algorithm", "Linear Regression with Weighted Recent Form",
                    "prediction", prediction
            ));
        } catch (ResourceNotFoundException e) {
            log.warn("[Prediction] Player not found: {}", playerId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("[Prediction] Error during basic prediction", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Prediction failed: " + e.getMessage()));
        }
    }

    /**
     * Get advanced AI-powered player performance prediction.
     * 
     * Calls Python AI service for ML-based prediction using player statistics.
     * Returns performance rating, category, strengths/weaknesses analysis.
     * 
     * @param playerId Player ID
     * @return PlayerPerformanceAIPredictionDTO with AI predictions
     */
    @GetMapping("/{playerId}/ai-prediction")
    public ResponseEntity<?> getAIPrediction(@PathVariable Long playerId) {
        try {
            log.info("[AI Prediction] AI prediction requested for player {}", playerId);
            
            // First get basic prediction to extract player stats
            PlayerPredictionDTO basicPrediction = basicPredictionService.predictPlayerPerformance(playerId);
            
            // Create request for AI service using predicted metrics
            PlayerPerformanceRequestDTO aiRequest = new PlayerPerformanceRequestDTO();
            aiRequest.setPlayerId(playerId);
            aiRequest.setGoals((int) basicPrediction.getAverageGoals());
            aiRequest.setAssists((int) basicPrediction.getAverageAssists());
            aiRequest.setTackles((int) basicPrediction.getPredictedTackles());
            aiRequest.setInterceptions((int) basicPrediction.getPredictedInterceptions());
            aiRequest.setPassesCompleted(50); // Default value
            aiRequest.setPassAccuracy(75.0); // Default value
            aiRequest.setDistanceCoveredKm(10.0); // Default value
            aiRequest.setAverageSpeedKmh(25.0); // Default value
            aiRequest.setBallPossessionPercent(50.0); // Default value
            aiRequest.setFoulsCommitted(2); // Default value
            aiRequest.setShotsOnTarget((int) basicPrediction.getPredictedGoals());
            
            // Get AI prediction
            PlayerPerformanceAIPredictionDTO aiPrediction = aiPredictionService.predictPlayerPerformanceAI(aiRequest);
            
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "type", "AI_PREDICTION",
                    "algorithm", aiPrediction.getAlgorithm(),
                    "confidence", aiPrediction.getConfidence(),
                    "prediction", aiPrediction,
                    "basicPrediction", basicPrediction
            ));
        } catch (ResourceNotFoundException e) {
            log.warn("[AI Prediction] Player not found: {}", playerId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("[AI Prediction] Error during AI prediction", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "AI prediction failed: " + e.getMessage()));
        }
    }

    /**
     * Predict player performance with custom statistics.
     * 
     * Allows sending custom player statistics for immediate prediction
     * without querying the database.
     * 
     * Useful for:
     * - What-if scenarios (e.g., "if player improves by X goals")
     * - Quick predictions without player ID lookup
     * - Performance analysis dashboard
     * 
     * Example Request Body:
     * {
     *   "playerId": 1,
     *   "goals": 3,
     *   "assists": 2,
     *   "tackles": 5,
     *   "interceptions": 2,
     *   "passesCompleted": 60,
     *   "passAccuracy": 85.5,
     *   "distanceCoveredKm": 10.5,
     *   "averageSpeedKmh": 26.2,
     *   "ballPossessionPercent": 55,
     *   "foulsCommitted": 1,
     *   "shotsOnTarget": 4
     * }
     * 
     * @param request Custom player statistics
     * @return AI prediction based on provided stats
     */
    @PostMapping("/ai-predict-custom")
    public ResponseEntity<?> predictWithCustomStats(@RequestBody PlayerPerformanceRequestDTO request) {
        try {
            if (request.getPlayerId() == null || request.getPlayerId() <= 0) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Player ID requis et doit être positif"));
            }
            
            log.info("[AI Prediction] Custom prediction for player {} with stats: G={}, A={}, T={}",
                    request.getPlayerId(), request.getGoals(), request.getAssists(), request.getTackles());
            
            PlayerPerformanceAIPredictionDTO prediction = aiPredictionService.predictPlayerPerformanceAI(request);
            
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "type", "CUSTOM_AI_PREDICTION",
                    "prediction", prediction
            ));
        } catch (Exception e) {
            log.error("[AI Prediction] Error during custom prediction", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Custom prediction failed: " + e.getMessage()));
        }
    }

    /**
     * Check AI service availability and model status.
     * 
     * @return Service health and model availability status
     */
    @GetMapping("/ai/health")
    public ResponseEntity<?> checkAIHealth() {
        boolean available = aiPredictionService.isAIServiceAvailable();
        
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "ai_service_available", available,
                "message", available ? 
                        "Service AI opérationnel" : 
                        "Service AI indisponible (exécutez train_player_prediction_model.py)"
        ));
    }

    /**
     * Batch prediction endpoint (future enhancement).
     * 
     * Allows predicting performance for multiple players in one request.
     * More efficient than calling single prediction endpoint multiple times.
     * 
     * @param requests List of player statistics
     * @return List of predictions
     */
    @PostMapping("/ai-predict-batch")
    public ResponseEntity<?> batchPredict(@RequestBody List<PlayerPerformanceRequestDTO> requests) {
        try {
            if (requests == null || requests.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Liste de joueurs requise"));
            }
            
            log.info("[AI Prediction] Batch prediction for {} players", requests.size());
            
            List<PlayerPerformanceAIPredictionDTO> predictions = aiPredictionService.predictBatch(requests);
            
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "type", "BATCH_PREDICTION",
                    "total", predictions.size(),
                    "predictions", predictions
            ));
        } catch (Exception e) {
            log.error("[AI Prediction] Error during batch prediction", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Batch prediction failed: " + e.getMessage()));
        }
    }

}
