package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.AffectationProgrammeDTO;
import com.streetLeague.backend.service.AffectationProgrammeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affectations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AffectationProgrammeController {

    private final AffectationProgrammeService affectationService;

    @PostMapping
    public ResponseEntity<AffectationProgrammeDTO.Response> create(
            @Valid @RequestBody AffectationProgrammeDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(affectationService.create(dto));
    }

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<List<AffectationProgrammeDTO.Response>> getByProgramme(
            @PathVariable Integer programmeId) {
        return ResponseEntity.ok(affectationService.getByProgramme(programmeId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AffectationProgrammeDTO.Response>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(affectationService.getByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        affectationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
