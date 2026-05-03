package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.SeanceExerciceDTO;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.SeanceExerciceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seance-exercices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeanceExerciceController {

    private final SeanceExerciceService seanceExerciceService;
    private final CoachingRoleService roleService;

    /* ── CREATE : COACH ou ADMIN (le coach ajoute des exercices à la séance) ── */
    @PostMapping
    public ResponseEntity<SeanceExerciceDTO.Response> create(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @Valid @RequestBody SeanceExerciceDTO.Request dto) {
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(seanceExerciceService.create(dto));
    }

    /* ── READ : tout utilisateur ── */
    @GetMapping
    public ResponseEntity<List<SeanceExerciceDTO.Response>> getAll() {
        return ResponseEntity.ok(seanceExerciceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceExerciceDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(seanceExerciceService.getById(id));
    }

    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<SeanceExerciceDTO.Response>> getBySeance(@PathVariable Integer seanceId) {
        return ResponseEntity.ok(seanceExerciceService.getBySeance(seanceId));
    }

    @GetMapping("/exercice/{exerciceId}")
    public ResponseEntity<List<SeanceExerciceDTO.Response>> getByExercice(@PathVariable Integer exerciceId) {
        return ResponseEntity.ok(seanceExerciceService.getByExercice(exerciceId));
    }

    /* ── UPDATE : COACH ou ADMIN ── */
    @PutMapping("/{id}")
    public ResponseEntity<SeanceExerciceDTO.Response> update(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @PathVariable Integer id, @Valid @RequestBody SeanceExerciceDTO.Request dto) {
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(seanceExerciceService.update(id, dto));
    }

    /* ── DELETE : COACH ou ADMIN ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @PathVariable Integer id) {
        roleService.requireCoachOrAdmin(userId);
        seanceExerciceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
