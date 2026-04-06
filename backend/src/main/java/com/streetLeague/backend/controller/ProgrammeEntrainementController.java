package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ProgrammeEntrainementDTO;
import com.streetLeague.backend.enums.StatutProgramme;
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

    @PostMapping
    public ResponseEntity<ProgrammeEntrainementDTO.Response> create(
            @Valid @RequestBody ProgrammeEntrainementDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programmeService.create(dto));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ProgrammeEntrainementDTO.Response> update(
            @PathVariable Integer id, @Valid @RequestBody ProgrammeEntrainementDTO.Request dto) {
        return ResponseEntity.ok(programmeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        programmeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
