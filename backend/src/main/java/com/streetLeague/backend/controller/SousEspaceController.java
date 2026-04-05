package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.DtoMapper;
import com.streetLeague.backend.dto.SousEspaceDTO;
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
    private final DtoMapper mapper;

    @GetMapping
    public ResponseEntity<List<SousEspaceDTO>> getAllSousEspaces() {
        return ResponseEntity.ok(sousEspaceService.getAllSousEspaces().stream().map(mapper::toSousEspaceDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousEspaceDTO> getSousEspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toSousEspaceDTO(sousEspaceService.getSousEspaceById(id)));
    }

    @PostMapping("/endroit/{endroitId}")
    public ResponseEntity<SousEspaceDTO> createSousEspace(@PathVariable Long endroitId, @RequestBody SousEspaceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toSousEspaceDTO(sousEspaceService.createSousEspace(endroitId, mapper.toSousEspace(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousEspaceDTO> updateSousEspace(@PathVariable Long id, @RequestBody SousEspaceDTO dto) {
        return ResponseEntity.ok(mapper.toSousEspaceDTO(sousEspaceService.updateSousEspace(id, mapper.toSousEspace(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSousEspace(@PathVariable Long id) {
        sousEspaceService.deleteSousEspace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/endroit/{endroitId}")
    public ResponseEntity<List<SousEspaceDTO>> getSousEspacesByEndroitId(@PathVariable Long endroitId) {
        return ResponseEntity.ok(sousEspaceService.getSousEspacesByEndroitId(endroitId).stream().map(mapper::toSousEspaceDTO).toList());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SousEspaceDTO>> getSousEspacesByType(@PathVariable TypeSousEspace type) {
        return ResponseEntity.ok(sousEspaceService.getSousEspacesByType(type).stream().map(mapper::toSousEspaceDTO).toList());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SousEspaceDTO>> getSousEspacesByStatut(@PathVariable StatutEndroit statut) {
        return ResponseEntity.ok(sousEspaceService.getSousEspacesByStatut(statut).stream().map(mapper::toSousEspaceDTO).toList());
    }
}
