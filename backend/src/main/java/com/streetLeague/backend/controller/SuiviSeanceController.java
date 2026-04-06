package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.SuiviSeanceDTO;
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

    @PostMapping
    public ResponseEntity<SuiviSeanceDTO.Response> create(@Valid @RequestBody SuiviSeanceDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(suiviService.create(dto));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<SuiviSeanceDTO.Response> update(
            @PathVariable Integer id, @Valid @RequestBody SuiviSeanceDTO.Request dto) {
        return ResponseEntity.ok(suiviService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        suiviService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
