package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.DtoMapper;
import com.streetLeague.backend.dto.ReservationDTO;
import com.streetLeague.backend.enums.StatutReservation;
import com.streetLeague.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService reservationService;
    private final DtoMapper mapper;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations().stream().map(mapper::toReservationDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toReservationDTO(reservationService.getReservationById(id)));
    }

    @PostMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<ReservationDTO> createReservation(@PathVariable Long sousEspaceId, @RequestBody ReservationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toReservationDTO(reservationService.createReservation(sousEspaceId, mapper.toReservation(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(mapper.toReservationDTO(reservationService.updateReservation(id, mapper.toReservation(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsBySousEspaceId(@PathVariable Long sousEspaceId) {
        return ResponseEntity.ok(reservationService.getReservationsBySousEspaceId(sousEspaceId).stream().map(mapper::toReservationDTO).toList());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatut(@PathVariable StatutReservation statut) {
        return ResponseEntity.ok(reservationService.getReservationsByStatut(statut).stream().map(mapper::toReservationDTO).toList());
    }

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ReservationDTO> confirmerReservation(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toReservationDTO(reservationService.confirmerReservation(id)));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ReservationDTO> annulerReservation(@PathVariable Long id, @RequestParam String motif) {
        return ResponseEntity.ok(mapper.toReservationDTO(reservationService.annulerReservation(id, motif)));
    }
}
