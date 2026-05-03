package com.streetLeague.backend.controller;

import com.streetLeague.backend.service.CoachingStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Step 7 : Endpoints de statistiques pour le module coaching.
 */
@RestController
@RequestMapping("/api/coaching/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CoachingStatsController {

    private final CoachingStatsService statsService;

    /** Statistiques globales du module coaching */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGlobalStats() {
        return ResponseEntity.ok(statsService.getGlobalStats());
    }

    /** Statistiques d'une séance */
    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<Map<String, Object>> getSeanceStats(@PathVariable Integer seanceId) {
        return ResponseEntity.ok(statsService.getSeanceStats(seanceId));
    }

    /** Statistiques d'un programme */
    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<Map<String, Object>> getProgrammeStats(@PathVariable Integer programmeId) {
        return ResponseEntity.ok(statsService.getProgrammeStats(programmeId));
    }
}
