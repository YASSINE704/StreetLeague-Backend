package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ProgrammeEntrainementDTO;
import com.streetLeague.backend.enums.StatutProgramme;
import com.streetLeague.backend.security.AuthenticatedUserResolver;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.ProgrammeEntrainementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programmes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgrammeEntrainementController {

    private final ProgrammeEntrainementService programmeService;
    private final CoachingRoleService roleService;
    private final AuthenticatedUserResolver userResolver;

    /* ── CREATE : COACH ou ADMIN uniquement ── */
    @PostMapping
    public ResponseEntity<ProgrammeEntrainementDTO.Response> create(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @Valid @RequestBody ProgrammeEntrainementDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(programmeService.create(dto));
    }

    /* ── READ : tout utilisateur authentifié ── */
    @GetMapping
    public ResponseEntity<List<ProgrammeEntrainementDTO.Response>> getAll() {
        return ResponseEntity.ok(programmeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgrammeEntrainementDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(programmeService.getById(id));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ProgrammeEntrainementDTO.Response>> getByStatut(
            @PathVariable StatutProgramme statut) {
        return ResponseEntity.ok(programmeService.getByStatut(statut));
    }

    /* ── UPDATE : COACH ou ADMIN uniquement ── */
    @PutMapping("/{id}")
    public ResponseEntity<ProgrammeEntrainementDTO.Response> update(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id, @Valid @RequestBody ProgrammeEntrainementDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(programmeService.update(id, dto));
    }

    /* ── DELETE : COACH ou ADMIN uniquement ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        programmeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
