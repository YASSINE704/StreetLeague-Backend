package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.SuiviSeanceDTO;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.SuiviSeanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suivis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuiviSeanceController {

    private final SuiviSeanceService suiviService;
    private final CoachingRoleService roleService;

    /* ── CREATE : SPORTIF, COACH ou ADMIN (le sportif donne son feedback) ── */
    @PostMapping
    public ResponseEntity<SuiviSeanceDTO.Response> create(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @Valid @RequestBody SuiviSeanceDTO.Request dto) {
        roleService.requireSportifOrCoachOrAdmin(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(suiviService.create(dto, userId));
    }

    /* ── READ : tout utilisateur ── */
    @GetMapping
    public ResponseEntity<List<SuiviSeanceDTO.Response>> getAll() {
        return ResponseEntity.ok(suiviService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuiviSeanceDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(suiviService.getById(id));
    }

    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<SuiviSeanceDTO.Response> getBySeance(@PathVariable Integer seanceId) {
        return ResponseEntity.ok(suiviService.getBySeance(seanceId));
    }

    /* ── UPDATE : SPORTIF, COACH ou ADMIN ── */
    @PutMapping("/{id}")
    public ResponseEntity<SuiviSeanceDTO.Response> update(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @PathVariable Integer id, @Valid @RequestBody SuiviSeanceDTO.Request dto) {
        roleService.requireSportifOrCoachOrAdmin(userId);
        return ResponseEntity.ok(suiviService.update(id, dto));
    }

    /* ── DELETE : COACH ou ADMIN uniquement ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @PathVariable Integer id) {
        roleService.requireCoachOrAdmin(userId);
        suiviService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
