package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ExerciceDTO;
import com.streetLeague.backend.enums.TypeExercice;
import com.streetLeague.backend.security.AuthenticatedUserResolver;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.ExerciceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExerciceController {

    private final ExerciceService exerciceService;
    private final CoachingRoleService roleService;
    private final AuthenticatedUserResolver userResolver;

    /* ── CREATE : COACH ou ADMIN uniquement ── */
    @PostMapping
    public ResponseEntity<ExerciceDTO.Response> create(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @Valid @RequestBody ExerciceDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciceService.create(dto));
    }

    /* ── READ : tout utilisateur authentifié ── */
    @GetMapping
    public ResponseEntity<List<ExerciceDTO.Response>> getAll() {
        return ResponseEntity.ok(exerciceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciceDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(exerciceService.getById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ExerciceDTO.Response>> getByType(@PathVariable TypeExercice type) {
        return ResponseEntity.ok(exerciceService.getByType(type));
    }

    /* ── UPDATE : COACH ou ADMIN uniquement ── */
    @PutMapping("/{id}")
    public ResponseEntity<ExerciceDTO.Response> update(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id, @Valid @RequestBody ExerciceDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(exerciceService.update(id, dto));
    }

    /* ── DELETE : COACH ou ADMIN uniquement ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        exerciceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
