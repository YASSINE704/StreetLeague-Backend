package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.PlayerPerformanceAIPredictionDTO;
import com.streetLeague.backend.dto.PlayerPerformanceRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

/**
 * Service for calling the Python AI service for player performance predictions.
 * 
 * This service communicates with the Flask-based AI service to provide
 * advanced ML-powered predictions of player future performance based on
 * historical statistics and trends.
 * 
 * Architecture:
 * - Backend (Java) calls this service
 * - Service calls Python AI Flask app at /api/ai/predict-player-performance
 * - Python service uses trained ML models (Random Forest / Gradient Boosting)
 * - Predictions include performance rating, category, and analysis
 * 
 * Fallback Strategy:
 * - If Python service is unavailable, basic prediction using existing service
 * - HTTP timeout: 5 seconds per request
 * - Automatic fallback for network errors
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerPerformanceAIService {

    private static final String AI_SERVICE_BASE_URL = "http://localhost:5000/api/ai";
    private static final long REQUEST_TIMEOUT_MS = 5000;

    private final RestTemplate restTemplate;
    private final PlayerPerformancePredictionService basicPredictionService;

    /**
     * Predicts player performance using the Python AI service.
     * Automatically falls back to basic prediction if AI service is unavailable.
     * 
     * @param request Player statistics and performance data
     * @return AI prediction with rating, category, and analysis
     */
    @SuppressWarnings("unchecked")
    public PlayerPerformanceAIPredictionDTO predictPlayerPerformanceAI(PlayerPerformanceRequestDTO request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(toAiPayload(request), headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    AI_SERVICE_BASE_URL + "/predict-player-performance",
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                
                // Extract prediction from response
                if ("ok".equals(body.get("status"))) {
                    Map<String, Object> prediction = (Map<String, Object>) body.get("prediction");
                    if (prediction != null) {
                        return mapPredictionResponse(prediction);
                    }
                }
            }
            
            log.warn("Invalid response from AI service, using fallback prediction");
            return buildFallbackPrediction(request.getPlayerId());
            
        } catch (Exception e) {
            log.warn("AI service unavailable ({}), using fallback prediction: {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            return buildFallbackPrediction(request.getPlayerId());
        }
    }

    /**
     * Predicts performance for multiple players in batch.
     * More efficient than calling single prediction endpoint multiple times.
     * 
     * @param requests List of player statistics
     * @return List of AI predictions
     */
    @SuppressWarnings("unchecked")
    public List<PlayerPerformanceAIPredictionDTO> predictBatch(List<PlayerPerformanceRequestDTO> requests) {
        try {
            Map<String, Object> batchRequest = new HashMap<>();
            batchRequest.put("players", requests.stream()
                    .map(this::toAiPayload)
                    .toList());
            
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    AI_SERVICE_BASE_URL + "/predict-batch",
                    batchRequest,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                
                if ("ok".equals(body.get("status"))) {
                    List<Map<String, Object>> predictions = (List<Map<String, Object>>) body.get("predictions");
                    if (predictions != null) {
                        return predictions.stream()
                                .map(this::mapPredictionResponse)
                                .toList();
                    }
                }
            }
            
            log.warn("Invalid response from batch prediction service");
            return Collections.emptyList();
            
        } catch (Exception e) {
            log.warn("Batch prediction failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Checks if the AI service is available and healthy.
     * 
     * @return true if service is available, false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean isAIServiceAvailable() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    AI_SERVICE_BASE_URL + "/health",
                    Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (Boolean) response.getBody().getOrDefault("player_prediction_model_loaded", false);
            }
            return false;
        } catch (Exception e) {
            log.warn("AI service health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Maps the AI service prediction response to our DTO.
     * 
     * @param predictionMap Raw prediction map from AI service
     * @return Mapped PlayerPerformanceAIPredictionDTO
     */
    @SuppressWarnings("unchecked")
    private PlayerPerformanceAIPredictionDTO mapPredictionResponse(Map<String, Object> predictionMap) {
        PlayerPerformanceAIPredictionDTO dto = new PlayerPerformanceAIPredictionDTO();
        
        if (predictionMap != null) {
            dto.setPlayerId(getLongValue(predictionMap, "player_id"));
            dto.setPredictedPerformanceRating(getDoubleValue(predictionMap, "predicted_performance_rating"));
            dto.setPerformanceCategory(getStringValue(predictionMap, "performance_category"));
            dto.setInterpretation(getStringValue(predictionMap, "interpretation"));
            dto.setStrengths((List<String>) predictionMap.getOrDefault("strengths", Collections.emptyList()));
            dto.setWeaknesses((List<String>) predictionMap.getOrDefault("weaknesses", Collections.emptyList()));
            dto.setConfidence(getStringValue(predictionMap, "confidence"));
            dto.setAlgorithm(getStringValue(predictionMap, "algorithm"));
        }
        
        return dto;
    }

    /**
     * Builds a fallback prediction when AI service is unavailable.
     * Uses the basic prediction service instead.
     * 
     * @param playerId Player ID
     * @return Basic prediction as fallback
     */
    private PlayerPerformanceAIPredictionDTO buildFallbackPrediction(Long playerId) {
        PlayerPerformanceAIPredictionDTO fallback = new PlayerPerformanceAIPredictionDTO();
        fallback.setPlayerId(playerId);
        fallback.setPredictedPerformanceRating(0);
        fallback.setPerformanceCategory("UNAVAILABLE");
        fallback.setInterpretation("Service IA indisponible — prédiction non disponible");
        fallback.setStrengths(Collections.emptyList());
        fallback.setWeaknesses(Collections.emptyList());
        fallback.setConfidence("LOW");
        fallback.setAlgorithm("FALLBACK");
        
        return fallback;
    }

    private Map<String, Object> toAiPayload(PlayerPerformanceRequestDTO request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("player_id", request.getPlayerId());
        payload.put("goals", request.getGoals());
        payload.put("assists", request.getAssists());
        payload.put("tackles", request.getTackles());
        payload.put("interceptions", request.getInterceptions());
        payload.put("passes_completed", request.getPassesCompleted());
        payload.put("pass_accuracy", request.getPassAccuracy());
        payload.put("distance_covered_km", request.getDistanceCoveredKm());
        payload.put("average_speed_kmh", request.getAverageSpeedKmh());
        payload.put("ball_possession_percent", request.getBallPossessionPercent());
        payload.put("fouls_committed", request.getFoulsCommitted());
        payload.put("shots_on_target", request.getShotsOnTarget());
        return payload;
    }

    // Helper methods for safe value extraction
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    private Double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }

}
