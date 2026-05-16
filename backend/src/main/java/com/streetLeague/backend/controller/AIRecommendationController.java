package com.streetLeague.backend.controller;

import com.streetLeague.backend.security.AuthenticatedUserResolver;
import com.streetLeague.backend.service.AIRecommendationService;
import com.streetLeague.backend.service.CoachingRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coaching/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIRecommendationController {

    private final AIRecommendationService aiRecommendationService;
    private final CoachingRoleService coachingRoleService;
    private final AuthenticatedUserResolver userResolver;

    /**
     * POST /api/coaching/ai/recommend
     * Requiert un utilisateur COACH ou ADMIN.
     */
    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> recommend(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @RequestBody Map<String, Object> context) {

        Integer userId = userResolver.resolveUserId(headerUserId);
        if (userId != null) {
            coachingRoleService.requireCoachOrAdmin(userId);
        }

        Map<String, Object> recommendations = aiRecommendationService.getRecommendations(context);
        return ResponseEntity.ok(recommendations);
    }

    /**
     * GET /api/coaching/ai/health
     * Vérifie si le service Python AI est disponible.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        boolean available = aiRecommendationService.isAvailable();
        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        response.put("message", available ? "Service AI opérationnel" : "Service AI indisponible");
        return ResponseEntity.ok(response);
    }
}
