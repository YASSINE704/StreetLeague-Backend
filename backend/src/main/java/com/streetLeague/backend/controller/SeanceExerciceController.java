package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.SeanceExerciceDTO;
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

    @PostMapping
    public ResponseEntity<SeanceExerciceDTO.Response> create(
            @Valid @RequestBody SeanceExerciceDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seanceExerciceService.create(dto));
    }

    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<SeanceExerciceDTO.Response>> getBySeance(@PathVariable Integer seanceId) {
        return ResponseEntity.ok(seanceExerciceService.getBySeance(seanceId));
    }

    @GetMapping("/exercice/{exerciceId}")
    public ResponseEntity<List<SeanceExerciceDTO.Response>> getByExercice(@PathVariable Integer exerciceId) {
        return ResponseEntity.ok(seanceExerciceService.getByExercice(exerciceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeanceExerciceDTO.Response> update(
            @PathVariable Integer id, @Valid @RequestBody SeanceExerciceDTO.Request dto) {
        return ResponseEntity.ok(seanceExerciceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        seanceExerciceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
