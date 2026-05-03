package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.DtoMapper;
import com.streetLeague.backend.dto.EndroitDTO;
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
@CrossOrigin(originPatterns = "*")
public class EndroitController {
    private final EndroitService endroitService;
    private final DtoMapper mapper;
    private static final String UPLOAD_DIR = "uploads/";
@PostMapping
public ResponseEntity<?> createEndroit(@RequestBody EndroitDTO dto) {
    try {
        Endroit saved = endroitService.createEndroit(mapper.toEndroit(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toEndroitDTO(saved));
    } catch (Exception e) {
        e.printStackTrace(); // ← voir dans la console le vrai message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erreur: " + e.getMessage() + " | Cause: " + e.getCause());
    }
}
    @GetMapping
    public ResponseEntity<List<EndroitDTO>> getAllEndroits() {
        return ResponseEntity.ok(endroitService.getAllEndroits().stream().map(mapper::toEndroitDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndroitDTO> getEndroitById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toEndroitDTO(endroitService.getEndroitById(id)));
    }

  
    @PutMapping("/{id}")
    public ResponseEntity<EndroitDTO> updateEndroit(@PathVariable Long id, @RequestBody EndroitDTO dto) {
        return ResponseEntity.ok(mapper.toEndroitDTO(endroitService.updateEndroit(id, mapper.toEndroit(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndroit(@PathVariable Long id) {
        endroitService.deleteEndroit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<EndroitDTO> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        Endroit endroit = endroitService.getEndroitById(id);
        endroit.setImageUrl("/uploads/" + filename);
        return ResponseEntity.ok(mapper.toEndroitDTO(endroitService.updateEndroit(id, endroit)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EndroitDTO>> getEndroitsByType(@PathVariable TypeEndroit type) {
        return ResponseEntity.ok(endroitService.getEndroitsByType(type).stream().map(mapper::toEndroitDTO).toList());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<EndroitDTO>> getEndroitsByStatut(@PathVariable StatutEndroit statut) {
        return ResponseEntity.ok(endroitService.getEndroitsByStatut(statut).stream().map(mapper::toEndroitDTO).toList());
    }

    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<EndroitDTO>> getEndroitsByVille(@PathVariable String ville) {
        return ResponseEntity.ok(endroitService.getEndroitsByVille(ville).stream().map(mapper::toEndroitDTO).toList());
    }
}
