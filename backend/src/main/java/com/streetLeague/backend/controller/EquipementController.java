package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.DtoMapper;
import com.streetLeague.backend.dto.EquipementDTO;
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
    private final DtoMapper mapper;

    @GetMapping
    public ResponseEntity<List<EquipementDTO>> getAllEquipements() {
        return ResponseEntity.ok(equipementService.getAllEquipements().stream().map(mapper::toEquipementDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipementDTO> getEquipementById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toEquipementDTO(equipementService.getEquipementById(id)));
    }

    @PostMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<EquipementDTO> createEquipement(@PathVariable Long sousEspaceId, @RequestBody EquipementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toEquipementDTO(equipementService.createEquipement(sousEspaceId, mapper.toEquipement(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipementDTO> updateEquipement(@PathVariable Long id, @RequestBody EquipementDTO dto) {
        return ResponseEntity.ok(mapper.toEquipementDTO(equipementService.updateEquipement(id, mapper.toEquipement(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        equipementService.deleteEquipement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<List<EquipementDTO>> getEquipementsBySousEspaceId(@PathVariable Long sousEspaceId) {
        return ResponseEntity.ok(equipementService.getEquipementsBySousEspaceId(sousEspaceId).stream().map(mapper::toEquipementDTO).toList());
    }
}
