package com.streetLeague.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyReservationCreated(Long reservationId, String sousEspaceNom, String endroitNom) {
        send("NOUVELLE_RESERVATION", "Nouvelle réservation pour " + sousEspaceNom + " (" + endroitNom + ")", reservationId);
    }

    public void notifyReservationConfirmed(Long reservationId, String sousEspaceNom, String endroitNom) {
        send("RESERVATION_CONFIRMEE", "Réservation confirmée pour " + sousEspaceNom + " (" + endroitNom + ")", reservationId);
    }

    public void notifyReservationCancelled(Long reservationId, String sousEspaceNom, String endroitNom) {
        send("RESERVATION_ANNULEE", "Réservation annulée pour " + sousEspaceNom + " (" + endroitNom + ")", reservationId);
    }

    private void send(String type, String message, Long reservationId) {
        messagingTemplate.convertAndSend("/topic/notifications", Map.of(
            "type", type,
            "message", message,
            "reservationId", reservationId,
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}
