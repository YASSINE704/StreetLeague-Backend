package com.streetLeague.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Step 9 : Service qui communique avec le microservice Python AI
 * pour obtenir des recommandations d'exercices.
 *
 * Si le service Python est indisponible, retourne une liste vide (fallback).
 */
@Service
@Slf4j
public class AIRecommendationService {

    @Value("${ai.service.url:http://localhost:5000}")
    private String aiServiceUrl;

    @Value("${ai.service.enabled:true}")
    private boolean enabled;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Demande des recommandations d'exercices au service Python AI.
     *
     * @param context Map contenant : objectifProgramme, typeSeance, intensite,
     *                nbParticipants, niveauJoueurs, dureeSeanceMinutes, lieuType, enPleinAir
     * @return Map avec status et liste de recommandations, ou fallback si indisponible
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRecommendations(Map<String, Object> context) {
        if (!enabled) {
            return fallbackResponse("Service IA désactivé");
        }

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    aiServiceUrl + "/api/ai/recommend", context, Map.class);
            if (response != null) {
                log.info("Recommandations IA reçues : {} exercices", response.get("nbRecommandations"));
                return response;
            }
            return fallbackResponse("Réponse vide du service IA");
        } catch (Exception e) {
            log.warn("Service IA indisponible : {}", e.getMessage());
            return fallbackResponse("Service IA indisponible — ajoutez les exercices manuellement");
        }
    }

    /**
     * Vérifie si le service Python AI est en ligne.
     */
    public boolean isAvailable() {
        if (!enabled) return false;
        try {
            restTemplate.getForObject(aiServiceUrl + "/api/ai/health", Map.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> fallbackResponse(String message) {
        Map<String, Object> fallback = new LinkedHashMap<>();
        fallback.put("status", "fallback");
        fallback.put("message", message);
        fallback.put("nbRecommandations", 0);
        fallback.put("recommandations", Collections.emptyList());
        return fallback;
    }
}
