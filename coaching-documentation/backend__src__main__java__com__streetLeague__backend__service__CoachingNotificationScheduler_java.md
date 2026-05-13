# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/CoachingNotificationScheduler.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Participe aux fonctionnalités météo, vêtements recommandés et notifications/rappels. 
- Appelé par un controller et utilise souvent un repository pour appliquer les règles métier.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.service;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.entity.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.StatutReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.enums.StatutSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.enums.TypeAffectationProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.repository.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import lombok.extern.slf4j.Slf4j;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import org.springframework.beans.factory.annotation.Value;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import org.springframework.scheduling.annotation.Scheduled;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 14 | `import java.time.LocalDate;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 15 | `import java.time.LocalDateTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 16 | `import java.time.LocalTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 17 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 20 | ` * Step 5 : Tâche planifiée qui envoie des notifications email` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 21 | ` * 2 heures avant chaque séance de coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 22 | ` *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 23 | ` * Exécutée toutes les 30 minutes.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 24 | ` * Vérifie aussi la météo pour les séances en plein air.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 25 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 27 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `@Slf4j` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `public class CoachingNotificationScheduler {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `    private final SeanceEntrainementRepository seanceRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 32 | `    private final ReservationSeanceRepository reservationRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 33 | `    private final AffectationProgrammeRepository affectationRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 34 | `    private final NotificationCoachingRepository notificationRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 35 | `    private final CoachingEmailService emailService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 36 | `    private final WeatherService weatherService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `    @Value("${coaching.notification.enabled:true}")` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 39 | `    private boolean enabled;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 40 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 41 | `    @Value("${coaching.notification.hours-before:2}")` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 42 | `    private int hoursBefore;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 43 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 44 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 45 | `     * Vérifie toutes les 30 minutes s'il y a des séances à venir` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 46 | `     * et envoie les rappels par email.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 47 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 48 | `    @Scheduled(fixedRate = 1800000) // 30 minutes en millisecondes` | Tâche automatique planifiée : Spring exécute cette méthode régulièrement. |
| 49 | `    public void checkUpcomingSessions() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 50 | `        if (!enabled) return;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 51 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 52 | `        log.debug("Vérification des séances à venir pour notifications...");` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 53 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 54 | `        LocalDate today = LocalDate.now();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `        List<SeanceEntrainement> seancesAujourdhui = seanceRepository.findByDateSeance(today);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `        for (SeanceEntrainement seance : seancesAujourdhui) {` | Boucle : répète une logique sur plusieurs éléments. |
| 58 | `            if (seance.getStatut() != StatutSeance.PREVUE) continue;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 59 | `            if (seance.getHeureDebut() == null) continue;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 60 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 61 | `            // Vérifier si la séance est dans les X heures à venir` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 62 | `            LocalTime maintenant = LocalTime.now();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `            LocalTime limiteNotif = seance.getHeureDebut().minusHours(hoursBefore);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 64 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 65 | `            if (maintenant.isAfter(limiteNotif) && maintenant.isBefore(seance.getHeureDebut())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 66 | `                notifyParticipants(seance);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `                notifyCoach(seance);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 68 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 69 | `                // Vérifier la météo pour les séances en plein air` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 70 | `                if (Boolean.TRUE.equals(seance.getEnPleinAir())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 71 | `                    checkWeatherAndNotify(seance);` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 72 | `                }` | Fin d’un bloc de code ou d’un élément HTML. |
| 73 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 74 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 75 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 76 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 77 | `    /** Notifie tous les sportifs qui ont réservé cette séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 78 | `    private void notifyParticipants(SeanceEntrainement seance) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 79 | `        List<ReservationSeance> reservations = reservationRepository` | Ligne liée à la réservation d’une séance par un sportif. |
| 80 | `                .findBySeanceIdSeanceAndStatutNot(seance.getIdSeance(), StatutReservationSeance.ANNULEE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 81 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 82 | `        for (ReservationSeance r : reservations) {` | Boucle : répète une logique sur plusieurs éléments. |
| 83 | `            User user = r.getUser();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 84 | `            if (alreadyNotified(seance.getIdSeance(), "RAPPEL_SEANCE", user.getIdUser())) continue;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 85 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 86 | `            String lieu = seance.getLieu() != null ? seance.getLieu().getNom() : null;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 87 | `            boolean sent = emailService.sendSessionReminder(` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 88 | `                    user.getEmail(),` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 89 | `                    user.getPrenom() + " " + user.getNom(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 90 | `                    seance.getTitreSeance(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 91 | `                    seance.getDateSeance().toString(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 92 | `                    seance.getHeureDebut().toString(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 93 | `                    lieu` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 94 | `            );` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 95 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 96 | `            saveNotification(seance, user, "RAPPEL_SEANCE",` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 97 | `                    "Rappel séance « " + seance.getTitreSeance() + " »", sent);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 98 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 99 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 100 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 101 | `    /** Notifie le coach du programme */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 102 | `    private void notifyCoach(SeanceEntrainement seance) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 103 | `        affectationRepository` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 104 | `                .findByProgrammeIdProgrammeAndType(seance.getProgramme().getIdProgramme(), TypeAffectationProgramme.COACH)` | Ligne liée aux rôles et aux permissions utilisateur. |
| 105 | `                .ifPresent(aff -> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 106 | `                    User coach = aff.getUser();` | Ligne liée aux rôles et aux permissions utilisateur. |
| 107 | `                    if (alreadyNotified(seance.getIdSeance(), "RAPPEL_SEANCE", coach.getIdUser())) return;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 108 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 109 | `                    String lieu = seance.getLieu() != null ? seance.getLieu().getNom() : null;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 110 | `                    boolean sent = emailService.sendSessionReminder(` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 111 | `                            coach.getEmail(),` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 112 | `                            coach.getPrenom() + " " + coach.getNom(),` | Ligne liée aux rôles et aux permissions utilisateur. |
| 113 | `                            seance.getTitreSeance(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 114 | `                            seance.getDateSeance().toString(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 115 | `                            seance.getHeureDebut().toString(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 116 | `                            lieu` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 117 | `                    );` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 118 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 119 | `                    saveNotification(seance, coach, "RAPPEL_SEANCE",` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 120 | `                            "Rappel séance « " + seance.getTitreSeance() + " » (coach)", sent);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 121 | `                });` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 122 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 123 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 124 | `    /** Vérifie la météo et notifie si mauvais temps */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 125 | `    private void checkWeatherAndNotify(SeanceEntrainement seance) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 126 | `        // Coordonnées par défaut (Tunis) — en production, utiliser les coordonnées du lieu` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 127 | `        Double lat = 36.8;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 128 | `        Double lon = 10.18;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 129 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 130 | `        if (seance.getLieu() != null && seance.getLieu().getEndroit() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 131 | `            Endroit endroit = seance.getLieu().getEndroit();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 132 | `            if (endroit.getLatitude() != null) lat = endroit.getLatitude();` | Condition : exécute le bloc seulement si la règle est vraie. |
| 133 | `            if (endroit.getLongitude() != null) lon = endroit.getLongitude();` | Condition : exécute le bloc seulement si la règle est vraie. |
| 134 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 135 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 136 | `        WeatherService.WeatherInfo info = weatherService.getWeatherForecast(lat, lon);` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 137 | `        if (info == null \|\| !info.isBadWeather()) return;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 138 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 139 | `        String recommandation = weatherService.getWeatherRecommendation(info);` | Ligne liée à la recommandation IA d’exercices. |
| 140 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 141 | `        // Notifier tous les participants` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 142 | `        List<ReservationSeance> reservations = reservationRepository` | Ligne liée à la réservation d’une séance par un sportif. |
| 143 | `                .findBySeanceIdSeanceAndStatutNot(seance.getIdSeance(), StatutReservationSeance.ANNULEE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 144 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 145 | `        for (ReservationSeance r : reservations) {` | Boucle : répète une logique sur plusieurs éléments. |
| 146 | `            User user = r.getUser();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 147 | `            if (alreadyNotified(seance.getIdSeance(), "ALERTE_METEO", user.getIdUser())) continue;` | Condition : exécute le bloc seulement si la règle est vraie. |
| 148 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 149 | `            boolean sent = emailService.sendWeatherAlert(` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 150 | `                    user.getEmail(),` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 151 | `                    user.getPrenom() + " " + user.getNom(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 152 | `                    seance.getTitreSeance(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 153 | `                    seance.getDateSeance().toString(),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 154 | `                    recommandation` | Ligne liée à la recommandation IA d’exercices. |
| 155 | `            );` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 156 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 157 | `            saveNotification(seance, user, "ALERTE_METEO", recommandation, sent);` | Ligne liée à la recommandation IA d’exercices. |
| 158 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 159 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 160 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 161 | `    private boolean alreadyNotified(Integer seanceId, String type, Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 162 | `        return notificationRepository.existsBySeanceIdSeanceAndTypeAndUserIdUser(seanceId, type, userId);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 163 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 164 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 165 | `    private void saveNotification(SeanceEntrainement seance, User user, String type, String message, boolean sent) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 166 | `        notificationRepository.save(NotificationCoaching.builder()` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 167 | `                .seance(seance)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 168 | `                .user(user)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 169 | `                .type(type)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 170 | `                .message(message)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 171 | `                .destinataireEmail(user.getEmail())` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 172 | `                .dateEnvoi(LocalDateTime.now())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 173 | `                .envoyee(sent)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 174 | `                .build());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 175 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 176 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `CoachingNotificationScheduler.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.