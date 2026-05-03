package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.*;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.enums.TypeAffectationProgramme;
import com.streetLeague.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Step 5 : Tâche planifiée qui envoie des notifications email
 * 2 heures avant chaque séance de coaching.
 *
 * Exécutée toutes les 30 minutes.
 * Vérifie aussi la météo pour les séances en plein air.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CoachingNotificationScheduler {

    private final SeanceEntrainementRepository seanceRepository;
    private final ReservationSeanceRepository reservationRepository;
    private final AffectationProgrammeRepository affectationRepository;
    private final NotificationCoachingRepository notificationRepository;
    private final CoachingEmailService emailService;
    private final WeatherService weatherService;

    @Value("${coaching.notification.enabled:true}")
    private boolean enabled;

    @Value("${coaching.notification.hours-before:2}")
    private int hoursBefore;

    /**
     * Vérifie toutes les 30 minutes s'il y a des séances à venir
     * et envoie les rappels par email.
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes en millisecondes
    public void checkUpcomingSessions() {
        if (!enabled) return;

        log.debug("Vérification des séances à venir pour notifications...");

        LocalDate today = LocalDate.now();
        List<SeanceEntrainement> seancesAujourdhui = seanceRepository.findByDateSeance(today);

        for (SeanceEntrainement seance : seancesAujourdhui) {
            if (seance.getStatut() != StatutSeance.PREVUE) continue;
            if (seance.getHeureDebut() == null) continue;

            // Vérifier si la séance est dans les X heures à venir
            LocalTime maintenant = LocalTime.now();
            LocalTime limiteNotif = seance.getHeureDebut().minusHours(hoursBefore);

            if (maintenant.isAfter(limiteNotif) && maintenant.isBefore(seance.getHeureDebut())) {
                notifyParticipants(seance);
                notifyCoach(seance);

                // Vérifier la météo pour les séances en plein air
                if (Boolean.TRUE.equals(seance.getEnPleinAir())) {
                    checkWeatherAndNotify(seance);
                }
            }
        }
    }

    /** Notifie tous les sportifs qui ont réservé cette séance */
    private void notifyParticipants(SeanceEntrainement seance) {
        List<ReservationSeance> reservations = reservationRepository
                .findBySeanceIdSeanceAndStatutNot(seance.getIdSeance(), StatutReservationSeance.ANNULEE);

        for (ReservationSeance r : reservations) {
            User user = r.getUser();
            if (alreadyNotified(seance.getIdSeance(), "RAPPEL_SEANCE", user.getIdUser())) continue;

            String lieu = seance.getLieu() != null ? seance.getLieu().getNom() : null;
            boolean sent = emailService.sendSessionReminder(
                    user.getEmail(),
                    user.getPrenom() + " " + user.getNom(),
                    seance.getTitreSeance(),
                    seance.getDateSeance().toString(),
                    seance.getHeureDebut().toString(),
                    lieu
            );

            saveNotification(seance, user, "RAPPEL_SEANCE",
                    "Rappel séance « " + seance.getTitreSeance() + " »", sent);
        }
    }

    /** Notifie le coach du programme */
    private void notifyCoach(SeanceEntrainement seance) {
        affectationRepository
                .findByProgrammeIdProgrammeAndType(seance.getProgramme().getIdProgramme(), TypeAffectationProgramme.COACH)
                .ifPresent(aff -> {
                    User coach = aff.getUser();
                    if (alreadyNotified(seance.getIdSeance(), "RAPPEL_SEANCE", coach.getIdUser())) return;

                    String lieu = seance.getLieu() != null ? seance.getLieu().getNom() : null;
                    boolean sent = emailService.sendSessionReminder(
                            coach.getEmail(),
                            coach.getPrenom() + " " + coach.getNom(),
                            seance.getTitreSeance(),
                            seance.getDateSeance().toString(),
                            seance.getHeureDebut().toString(),
                            lieu
                    );

                    saveNotification(seance, coach, "RAPPEL_SEANCE",
                            "Rappel séance « " + seance.getTitreSeance() + " » (coach)", sent);
                });
    }

    /** Vérifie la météo et notifie si mauvais temps */
    private void checkWeatherAndNotify(SeanceEntrainement seance) {
        // Coordonnées par défaut (Tunis) — en production, utiliser les coordonnées du lieu
        Double lat = 36.8;
        Double lon = 10.18;

        if (seance.getLieu() != null && seance.getLieu().getEndroit() != null) {
            Endroit endroit = seance.getLieu().getEndroit();
            if (endroit.getLatitude() != null) lat = endroit.getLatitude();
            if (endroit.getLongitude() != null) lon = endroit.getLongitude();
        }

        WeatherService.WeatherInfo info = weatherService.getWeatherForecast(lat, lon);
        if (info == null || !info.isBadWeather()) return;

        String recommandation = weatherService.getWeatherRecommendation(info);

        // Notifier tous les participants
        List<ReservationSeance> reservations = reservationRepository
                .findBySeanceIdSeanceAndStatutNot(seance.getIdSeance(), StatutReservationSeance.ANNULEE);

        for (ReservationSeance r : reservations) {
            User user = r.getUser();
            if (alreadyNotified(seance.getIdSeance(), "ALERTE_METEO", user.getIdUser())) continue;

            boolean sent = emailService.sendWeatherAlert(
                    user.getEmail(),
                    user.getPrenom() + " " + user.getNom(),
                    seance.getTitreSeance(),
                    seance.getDateSeance().toString(),
                    recommandation
            );

            saveNotification(seance, user, "ALERTE_METEO", recommandation, sent);
        }
    }

    private boolean alreadyNotified(Integer seanceId, String type, Integer userId) {
        return notificationRepository.existsBySeanceIdSeanceAndTypeAndUserIdUser(seanceId, type, userId);
    }

    private void saveNotification(SeanceEntrainement seance, User user, String type, String message, boolean sent) {
        notificationRepository.save(NotificationCoaching.builder()
                .seance(seance)
                .user(user)
                .type(type)
                .message(message)
                .destinataireEmail(user.getEmail())
                .dateEnvoi(LocalDateTime.now())
                .envoyee(sent)
                .build());
    }
}
