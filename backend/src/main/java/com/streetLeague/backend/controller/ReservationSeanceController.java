package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ReservationSeanceDTO;
import com.streetLeague.backend.security.AuthenticatedUserResolver;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.ReservationSeanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour la réservation de séances de coaching.
 * Un SPORTIF réserve sa place, un COACH confirme ou annule.
 */
@RestController
@RequestMapping("/api/reservations-seances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationSeanceController {

    private final ReservationSeanceService reservationService;
    private final CoachingRoleService roleService;
    private final AuthenticatedUserResolver userResolver;

    /* ── RÉSERVER : SPORTIF ou ADMIN ── */
    @PostMapping
    public ResponseEntity<ReservationSeanceDTO.Response> reserver(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @Valid @RequestBody ReservationSeanceDTO.Request dto) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.reserver(userId, dto));
    }

    /* ── ANNULER : le sportif ou le coach ── */
    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationSeanceDTO.Response> annuler(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, String> body) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireSportifOrCoachOrAdmin(userId);
        String motif = body != null ? body.get("motif") : null;
        return ResponseEntity.ok(reservationService.annuler(id, motif));
    }

    /* ── CONFIRMER : COACH ou ADMIN ── */
    @PutMapping("/{id}/confirmer")
    public ResponseEntity<ReservationSeanceDTO.Response> confirmer(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(reservationService.confirmer(id));
    }

    /* ── LIRE : réservations d'une séance ── */
    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<ReservationSeanceDTO.Response>> getBySeance(@PathVariable Integer seanceId) {
        return ResponseEntity.ok(reservationService.getBySeance(seanceId));
    }

    /* ── LIRE : réservations d'un sportif ── */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationSeanceDTO.Response>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(reservationService.getByUser(userId));
    }

    /* ── LIRE : mes réservations (utilisateur connecté) ── */
    @GetMapping("/me")
    public ResponseEntity<List<ReservationSeanceDTO.Response>> getMyReservations(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        return ResponseEntity.ok(reservationService.getByUser(userId));
    }

    /* ── LIRE : places restantes pour une séance ── */
    @GetMapping("/seance/{seanceId}/places")
    public ResponseEntity<Map<String, Integer>> getPlacesRestantes(@PathVariable Integer seanceId) {
        int places = reservationService.getPlacesRestantes(seanceId);
        return ResponseEntity.ok(Map.of("placesRestantes", places));
    }

    /* ── Step 6 : MARQUER COMME PAYÉ ── */
    @PutMapping("/{id}/payer")
    public ResponseEntity<ReservationSeanceDTO.Response> marquerPaye(
            @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
            @PathVariable Integer id) {
        Integer userId = userResolver.resolveUserId(headerUserId);
        roleService.requireCoachOrAdmin(userId);
        return ResponseEntity.ok(reservationService.marquerPaye(id));
    }
}
