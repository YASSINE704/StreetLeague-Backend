package com.streetLeague.backend.controller;

import com.streetLeague.backend.entity.Reservation;
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

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<Reservation> createReservation(@PathVariable Long sousEspaceId, @RequestBody Reservation reservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(sousEspaceId, reservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sous-espace/{sousEspaceId}")
    public ResponseEntity<List<Reservation>> getReservationsBySousEspaceId(@PathVariable Long sousEspaceId) {
        return ResponseEntity.ok(reservationService.getReservationsBySousEspaceId(sousEspaceId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Reservation>> getReservationsByStatut(@PathVariable StatutReservation statut) {
        return ResponseEntity.ok(reservationService.getReservationsByStatut(statut));
    }

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<Reservation> confirmerReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmerReservation(id));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Reservation> annulerReservation(@PathVariable Long id, @RequestParam String motif) {
        return ResponseEntity.ok(reservationService.annulerReservation(id, motif));
    }
}
