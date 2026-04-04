package com.streetLeague.backend.controller;

import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeSousEspace;
import com.streetLeague.backend.service.SousEspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-espaces")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SousEspaceController {
    private final SousEspaceService sousEspaceService;

    @GetMapping
    public ResponseEntity<List<SousEspace>> getAllSousEspaces() {
        return ResponseEntity.ok(sousEspaceService.getAllSousEspaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousEspace> getSousEspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(sousEspaceService.getSousEspaceById(id));
    }

    @PostMapping("/endroit/{endroitId}")
    public ResponseEntity<SousEspace> createSousEspace(@PathVariable Long endroitId, @RequestBody SousEspace sousEspace) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sousEspaceService.createSousEspace(endroitId, sousEspace));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousEspace> updateSousEspace(@PathVariable Long id, @RequestBody SousEspace sousEspace) {
        return ResponseEntity.ok(sousEspaceService.updateSousEspace(id, sousEspace));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSousEspace(@PathVariable Long id) {
        sousEspaceService.deleteSousEspace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/endroit/{endroitId}")
    public ResponseEntity<List<SousEspace>> getSousEspacesByEndroitId(@PathVariable Long endroitId) {
        return ResponseEntity.ok(sousEspaceService.getSousEspacesByEndroitId(endroitId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SousEspace>> getSousEspacesByType(@PathVariable TypeSousEspace type) {
        return ResponseEntity.ok(sousEspaceService.getSousEspacesByType(type));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SousEspace>> getSousEspacesByStatut(@PathVariable StatutEndroit statut) {
        return ResponseEntity.ok(sousEspaceService.getSousEspacesByStatut(statut));
    }
}
