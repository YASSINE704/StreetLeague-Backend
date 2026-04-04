package com.streetLeague.backend.controller;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
import com.streetLeague.backend.service.EndroitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/endroits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EndroitController {
    private final EndroitService endroitService;
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public ResponseEntity<List<Endroit>> getAllEndroits() {
        return ResponseEntity.ok(endroitService.getAllEndroits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endroit> getEndroitById(@PathVariable Long id) {
        return ResponseEntity.ok(endroitService.getEndroitById(id));
    }

    @PostMapping
    public ResponseEntity<Endroit> createEndroit(@RequestBody Endroit endroit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(endroitService.createEndroit(endroit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endroit> updateEndroit(@PathVariable Long id, @RequestBody Endroit endroit) {
        return ResponseEntity.ok(endroitService.updateEndroit(id, endroit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndroit(@PathVariable Long id) {
        endroitService.deleteEndroit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Endroit> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));

        Endroit endroit = endroitService.getEndroitById(id);
        endroit.setImageUrl("/uploads/" + filename);
        return ResponseEntity.ok(endroitService.updateEndroit(id, endroit));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Endroit>> getEndroitsByType(@PathVariable TypeEndroit type) {
        return ResponseEntity.ok(endroitService.getEndroitsByType(type));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Endroit>> getEndroitsByStatut(@PathVariable StatutEndroit statut) {
        return ResponseEntity.ok(endroitService.getEndroitsByStatut(statut));
    }

    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<Endroit>> getEndroitsByVille(@PathVariable String ville) {
        return ResponseEntity.ok(endroitService.getEndroitsByVille(ville));
    }
}
