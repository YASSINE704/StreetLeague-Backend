package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.AffectationProgrammeDTO;
import com.streetLeague.backend.service.AffectationProgrammeService;
import com.streetLeague.backend.service.CoachingRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affectations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AffectationProgrammeController {

    private final AffectationProgrammeService affectationService;
    private final CoachingRoleService roleService;

    /* ── CREATE : COACH ou ADMIN (le coach affecte des utilisateurs) ── */
    @PostMapping
    public ResponseEntity<AffectationProgrammeDTO.Response> create(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @Valid @RequestBody AffectationProgrammeDTO.Request dto) {
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(affectationService.create(dto));
    }

    /* ── READ : tout utilisateur ── */
    @GetMapping
    public ResponseEntity<List<AffectationProgrammeDTO.Response>> getAll() {
        return ResponseEntity.ok(affectationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AffectationProgrammeDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(affectationService.getById(id));
    }

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<List<AffectationProgrammeDTO.Response>> getByProgramme(
            @PathVariable Integer programmeId) {
        return ResponseEntity.ok(affectationService.getByProgramme(programmeId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AffectationProgrammeDTO.Response>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(affectationService.getByUser(userId));
    }

    /* ── UPDATE : COACH ou ADMIN ── */
    @PutMapping("/{id}")
    public ResponseEntity<AffectationProgrammeDTO.Response> update(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @PathVariable Integer id,
            @Valid @RequestBody AffectationProgrammeDTO.Request dto) {
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(affectationService.update(id, dto));
    }

    /* ── DELETE : COACH ou ADMIN ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @PathVariable Integer id) {
        roleService.requireCoachOrAdmin(userId);
        affectationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
