package com.streetLeague.backend.controller;

import com.streetLeague.backend.entity.Equipement;
import com.streetLeague.backend.service.EquipementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EquipementController {
    private final EquipementService equipementService;

    @GetMapping
    public ResponseEntity<List<Equipement>> getAllEquipements() {
        return ResponseEntity.ok(equipementService.getAllEquipements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getEquipementById(@PathVariable Long id) {
        return ResponseEntity.ok(equipementService.getEquipementById(id));
    }

    @PostMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<Equipement> createEquipement(@PathVariable Long sousEspaceId, @RequestBody Equipement equipement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementService.createEquipement(sousEspaceId, equipement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipement> updateEquipement(@PathVariable Long id, @RequestBody Equipement equipement) {
        return ResponseEntity.ok(equipementService.updateEquipement(id, equipement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        equipementService.deleteEquipement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<List<Equipement>> getEquipementsBySousEspaceId(@PathVariable Long sousEspaceId) {
        return ResponseEntity.ok(equipementService.getEquipementsBySousEspaceId(sousEspaceId));
    }
}
