package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.SeanceEntrainementDTO;
import com.streetLeague.backend.service.SeanceEntrainementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeanceEntrainementController {

    private final SeanceEntrainementService seanceService;

    @PostMapping
    public ResponseEntity<SeanceEntrainementDTO.Response> create(
            @Valid @RequestBody SeanceEntrainementDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seanceService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<SeanceEntrainementDTO.Response>> getAll() {
        return ResponseEntity.ok(seanceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceEntrainementDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(seanceService.getById(id));
    }

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<List<SeanceEntrainementDTO.Response>> getByProgramme(
            @PathVariable Integer programmeId) {
        return ResponseEntity.ok(seanceService.getByProgramme(programmeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeanceEntrainementDTO.Response> update(
            @PathVariable Integer id, @Valid @RequestBody SeanceEntrainementDTO.Request dto) {
        return ResponseEntity.ok(seanceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        seanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
