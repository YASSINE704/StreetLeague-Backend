package com.streetLeague.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {

    private static final String AI_SERVICE_BASE_URL = "http://localhost:5000/api/ai";

    private final RestTemplate restTemplate;

    /**
     * Appelle le service Python AI pour obtenir des recommandations d'exercices.
     * En cas d'indisponibilité du service, retourne un fallback vide.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRecommendations(Map<String, Object> context) {
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    AI_SERVICE_BASE_URL + "/recommend",
                    context,
                    Map.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            return buildFallbackResponse("Réponse invalide du service AI");
        } catch (Exception e) {
            log.warn("Service AI indisponible, utilisation du fallback : {}", e.getMessage());
            return buildFallbackResponse("Service AI temporairement indisponible");
        }
    }

    /**
     * Vérifie si le service Python AI est disponible.
     */
    public boolean isAvailable() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    AI_SERVICE_BASE_URL + "/health",
                    Map.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("Health check AI échoué : {}", e.getMessage());
            return false;
        }
    }

    private Map<String, Object> buildFallbackResponse(String message) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("status", "fallback");
        fallback.put("message", message);
        fallback.put("nbRecommandations", 0);
        fallback.put("recommandations", Collections.emptyList());
        return fallback;
    }
}
