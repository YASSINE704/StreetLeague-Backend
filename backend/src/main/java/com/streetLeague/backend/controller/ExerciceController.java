package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ExerciceDTO;
import com.streetLeague.backend.enums.TypeExercice;
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

    @PostMapping
    public ResponseEntity<ExerciceDTO.Response> create(@Valid @RequestBody ExerciceDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciceService.create(dto));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ExerciceDTO.Response> update(
            @PathVariable Integer id, @Valid @RequestBody ExerciceDTO.Request dto) {
        return ResponseEntity.ok(exerciceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        exerciceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
