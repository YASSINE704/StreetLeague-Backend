package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.DtoMapper;
import com.streetLeague.backend.dto.ReservationDTO;
import com.streetLeague.backend.enums.StatutReservation;
import com.streetLeague.backend.service.ReservationPdfService;
import com.streetLeague.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationPdfService reservationPdfService;
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
    public ResponseEntity<?> createReservation(@PathVariable Long sousEspaceId, @RequestBody ReservationDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toReservationDTO(reservationService.createReservation(sousEspaceId, mapper.toReservation(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("error", e.getMessage()));
        }
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

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadReservationPdf(@PathVariable Long id) {
        try {
            var reservation = reservationService.getReservationById(id);
            byte[] pdf = reservationPdfService.generateReservationPdf(reservation);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reservation-" + id + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{id}/view", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> viewReservation(@PathVariable Long id) {
        try {
            var r = reservationService.getReservationById(id);
            boolean confirmed = r.getStatut() != null && "CONFIRMEE".equals(r.getStatut().name());
            String statutColor = confirmed ? "#16a34a" : "#ea580c";
            String statutBg    = confirmed ? "#dcfce7"  : "#ffedd5";
            String statutLabel = confirmed ? "Confirmee" : "En attente";

            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String debut   = r.getDateDebut()    != null ? r.getDateDebut().format(fmt)    : "-";
            String fin     = r.getDateFin()      != null ? r.getDateFin().format(fmt)      : "-";
            String created = r.getDateCreation() != null ? r.getDateCreation().format(fmt) : "-";

            String duration = "";
            if (r.getDateDebut() != null && r.getDateFin() != null) {
                long mins = java.time.Duration.between(r.getDateDebut(), r.getDateFin()).toMinutes();
                long h = mins / 60, m = mins % 60;
                duration = h > 0 ? h + "h" + (m > 0 ? m + "min" : "") : m + " min";
            }

            String html = """
                <!DOCTYPE html>
                <html lang="fr">
                <head>
                  <meta charset="UTF-8"/>
                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                  <title>Reservation #%d - StreetLeague</title>
                  <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                           background: #f1f5f9; min-height: 100vh; padding: 24px 16px; }
                    .card { background: white; border-radius: 16px; max-width: 480px;
                            margin: 0 auto; overflow: hidden;
                            box-shadow: 0 4px 24px rgba(0,0,0,0.10); }
                    .header { background: #0f172a; padding: 28px 28px 20px; }
                    .header h1 { color: #22c55e; font-size: 22px; font-weight: 800; }
                    .header p  { color: #64748b; font-size: 12px; margin-top: 4px; }
                    .header .num { color: white; font-size: 13px; margin-top: 10px; }
                    .header .num span { color: #22c55e; font-size: 20px; font-weight: 700; }
                    .accent { height: 4px; background: #22c55e; }
                    .statut { padding: 12px 28px; font-weight: 700; font-size: 13px;
                              background: %s; color: %s; }
                    .body { padding: 24px 28px; }
                    .section-title { font-size: 13px; font-weight: 700; color: #0f172a;
                                     margin-bottom: 14px; }
                    .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px;
                            margin-bottom: 18px; }
                    .info-card { background: #f8fafc; border: 1px solid #e2e8f0;
                                 border-radius: 10px; padding: 12px; }
                    .info-card .lbl { font-size: 10px; font-weight: 700; color: #64748b;
                                      text-transform: uppercase; letter-spacing: .5px; margin-bottom: 4px; }
                    .info-card .val { font-size: 13px; font-weight: 700; color: #0f172a; }
                    .duration { background: #f8fafc; border: 1.5px solid #22c55e;
                                border-radius: 10px; padding: 14px; text-align: center;
                                margin-bottom: 18px; }
                    .duration .lbl { font-size: 10px; color: #64748b; margin-bottom: 4px; }
                    .duration .val { font-size: 28px; font-weight: 800; color: #22c55e; }
                    .footer-note { font-size: 11px; color: #94a3b8; text-align: center;
                                   padding: 16px 28px; border-top: 1px solid #e2e8f0; }
                    .footer-bar { background: #0f172a; padding: 12px; text-align: center;
                                  font-size: 11px; color: #64748b; }
                  </style>
                </head>
                <body>
                  <div class="card">
                    <div class="header">
                      <h1>StreetLeague</h1>
                      <p>Plateforme de reservation de terrains</p>
                      <div class="num">Reservation <span>#%d</span></div>
                    </div>
                    <div class="accent"></div>
                    <div class="statut">%s</div>
                    <div class="body">
                      <div class="section-title">Details de la reservation</div>
                      <div class="grid">
                        <div class="info-card"><div class="lbl">Endroit</div><div class="val">%s</div></div>
                        <div class="info-card"><div class="lbl">Sous-espace</div><div class="val">%s</div></div>
                        <div class="info-card"><div class="lbl">Debut</div><div class="val">%s</div></div>
                        <div class="info-card"><div class="lbl">Fin</div><div class="val">%s</div></div>
                      </div>
                      %s
                      %s
                    </div>
                    <div class="footer-note">Cree le %s &nbsp;|&nbsp; Presentez ce document a l'accueil</div>
                    <div class="footer-bar">streetleague.tn &nbsp;|&nbsp; contact@streetleague.tn</div>
                  </div>
                </body>
                </html>
                """.formatted(
                    r.getId(),
                    statutBg, statutColor,
                    r.getId(),
                    statutLabel,
                    r.fetchEndroitNom()    != null ? r.fetchEndroitNom()    : "-",
                    r.fetchSousEspaceNom() != null ? r.fetchSousEspaceNom() : "-",
                    debut, fin,
                    duration.isBlank() ? "" :
                        "<div class=\"duration\"><div class=\"lbl\">Duree totale</div><div class=\"val\">" + duration + "</div></div>",
                    r.getPrixTotal() != null ?
                        "<div class=\"duration\" style=\"border-color:#16a34a;margin-top:0\"><div class=\"lbl\">Prix total</div><div class=\"val\" style=\"color:#16a34a\">" + String.format("%.2f DT", r.getPrixTotal()) + "</div></div>" : "",
                    created
            );

            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("<h2>Reservation introuvable</h2>");
        }
    }
}
