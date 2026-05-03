package com.streetLeague.backend.controller;

import com.streetLeague.backend.service.AIRecommendationService;
import com.streetLeague.backend.service.CoachingRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Step 9 : Endpoints pour les recommandations IA d'exercices.
 * Le coach demande des suggestions, l'IA propose, le coach valide.
 */
@RestController
@RequestMapping("/api/coaching/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIRecommendationController {

    private final AIRecommendationService aiService;
    private final CoachingRoleService roleService;

    /**
     * Demander des recommandations d'exercices à l'IA.
     * Seul le COACH ou ADMIN peut demander.
     *
     * Body JSON :
     * {
     *   "objectifProgramme": "renforcement musculaire",
     *   "typeSeance": "FORCE",
     *   "intensite": "MOYENNE",
     *   "nbParticipants": 4,
     *   "dureeSeanceMinutes": 60
     * }
     */
    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> recommend(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @RequestBody Map<String, Object> context) {
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(aiService.getRecommendations(context));
    }

    /** Vérifier si le service IA est disponible */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        boolean available = aiService.isAvailable();
        return ResponseEntity.ok(Map.of(
                "available", available,
                "message", available ? "Service IA opérationnel" : "Service IA indisponible"
        ));
    }
}
