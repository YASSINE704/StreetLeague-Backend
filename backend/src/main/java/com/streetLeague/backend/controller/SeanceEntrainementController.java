package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.SeanceEntrainementDTO;
import com.streetLeague.backend.security.AuthenticatedUserResolver;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.SeanceEntrainementService;
import com.streetLeague.backend.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeanceEntrainementController {

    private final SeanceEntrainementService seanceService;
    private final CoachingRoleService roleService;
    private final WeatherService weatherService;
    private final AuthenticatedUserResolver userResolver;

    /* ── CREATE : COACH ou ADMIN uniquement ── */
    @PostMapping
    public ResponseEntity<SeanceEntrainementDTO.Response> create(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @Valid @RequestBody SeanceEntrainementDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(seanceService.create(dto));
    }

    /* ── READ : tout utilisateur authentifié ── */
    @GetMapping
    public ResponseEntity<List<SeanceEntrainementDTO.Response>> getAll() {
        return ResponseEntity.ok(seanceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceEntrainementDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(seanceService.getById(id));
    }

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<List<SeanceEntrainementDTO.Response>> getByProgramme(
            @PathVariable Integer programmeId) {
        return ResponseEntity.ok(seanceService.getByProgramme(programmeId));
    }

    /* ── UPDATE : COACH ou ADMIN uniquement ── */
    @PutMapping("/{id}")
    public ResponseEntity<SeanceEntrainementDTO.Response> update(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id, @Valid @RequestBody SeanceEntrainementDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(seanceService.update(id, dto));
    }

    /* ── DELETE : COACH ou ADMIN uniquement ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        seanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* ── Step 4 : MÉTÉO pour une séance en plein air ── */
    @GetMapping("/{id}/meteo")
    public ResponseEntity<?> getWeather(@PathVariable Integer id) {
        SeanceEntrainementDTO.Response seance = seanceService.getById(id);
        if (seance.getEnPleinAir() == null || !seance.getEnPleinAir()) {
            return ResponseEntity.ok(Map.of("message", "Cette séance n'est pas en plein air"));
        }
        if (seance.getSousEspaceId() == null) {
            return ResponseEntity.ok(Map.of("message", "Aucun lieu défini pour cette séance"));
        }
        // Récupérer les coordonnées du lieu via l'endroit parent
        // Pour l'instant retourner un message informatif
        WeatherService.WeatherInfo info = weatherService.getWeatherForecast(36.8, 10.18); // Tunis par défaut
        if (info == null) {
            return ResponseEntity.ok(Map.of("message", "Service météo indisponible ou désactivé"));
        }
        String recommendation = weatherService.getWeatherRecommendation(info);
        return ResponseEntity.ok(Map.of(
                "condition", info.condition(),
                "description", info.description(),
                "temperature", info.temperature(),
                "mauvaisTemps", info.isBadWeather(),
                "recommandation", recommendation != null ? recommendation : "Conditions favorables pour la séance"
        ));
    }
}
