package com.streetLeague.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Step 5 : Historique des notifications envoyées pour le module coaching.
 * Permet de ne pas envoyer deux fois la même notification.
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationCoaching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotification;

    private String type;          // RAPPEL_SEANCE, ALERTE_METEO
    private String message;
    private String destinataireEmail;
    private LocalDateTime dateEnvoi;
    private Boolean envoyee;

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
