# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/CoachingEmailService.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Appelé par un controller et utilise souvent un repository pour appliquer les règles métier.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.service;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import lombok.extern.slf4j.Slf4j;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import org.springframework.beans.factory.annotation.Value;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import org.springframework.mail.SimpleMailMessage;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import org.springframework.mail.javamail.JavaMailSender;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 11 | ` * Step 5 : Service d'envoi d'emails pour le module coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * Utilise Gmail SMTP configuré dans application.properties.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 15 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `@Slf4j` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `public class CoachingEmailService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `    private final JavaMailSender mailSender;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `    @Value("${spring.mail.username}")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `    private String fromEmail;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 25 | `     * Envoie un email simple.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 27 | `    public boolean sendEmail(String to, String subject, String body) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 28 | `        try {` | Début d’un bloc de sécurité pour gérer les erreurs possibles. |
| 29 | `            SimpleMailMessage message = new SimpleMailMessage();` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 30 | `            message.setFrom(fromEmail);` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 31 | `            message.setTo(to);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `            message.setSubject("[StreetLeague Coaching] " + subject);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 33 | `            message.setText(body);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `            mailSender.send(message);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `            log.info("Email envoyé à {} : {}", to, subject);` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 36 | `            return true;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 37 | `        } catch (Exception e) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 38 | `            log.error("Erreur envoi email à {} : {}", to, e.getMessage());` | Ligne liée aux notifications ou à l’envoi d’emails. |
| 39 | `            return false;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 40 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 41 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 42 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 43 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 44 | `     * Envoie un rappel de séance.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 45 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 46 | `    public boolean sendSessionReminder(String to, String nomDestinataire,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 47 | `                                        String titreSeance, String dateSeance,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `                                        String heureDebut, String lieu) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 49 | `        String subject = "Rappel : Séance « " + titreSeance + " » dans 2 heures";` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `        String body = "Bonjour " + nomDestinataire + ",\n\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `                + "Ceci est un rappel pour votre séance d'entraînement :\n\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `                + "📋 Séance : " + titreSeance + "\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `                + "📅 Date : " + dateSeance + "\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `                + "🕐 Heure : " + (heureDebut != null ? heureDebut : "Non définie") + "\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `                + "📍 Lieu : " + (lieu != null ? lieu : "Non défini") + "\n\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `                + "Préparez-vous et soyez à l'heure !\n\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `                + "— L'équipe StreetLeague Coaching";` | Ligne liée aux rôles et aux permissions utilisateur. |
| 58 | `        return sendEmail(to, subject, body);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 59 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 60 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 61 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 62 | `     * Envoie une alerte météo.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 63 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 64 | `    public boolean sendWeatherAlert(String to, String nomDestinataire,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 65 | `                                     String titreSeance, String dateSeance,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 66 | `                                     String recommandation) {` | Ligne liée à la recommandation IA d’exercices. |
| 67 | `        String subject = "⚠️ Alerte météo : Séance « " + titreSeance + " »";` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 68 | `        String body = "Bonjour " + nomDestinataire + ",\n\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `                + "Les conditions météo ne sont pas favorables pour votre séance en plein air :\n\n"` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 70 | `                + "📋 Séance : " + titreSeance + "\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `                + "📅 Date : " + dateSeance + "\n\n"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `                + recommandation + "\n\n"` | Ligne liée à la recommandation IA d’exercices. |
| 73 | `                + "Contactez votre coach pour plus d'informations.\n\n"` | Ligne liée aux rôles et aux permissions utilisateur. |
| 74 | `                + "— L'équipe StreetLeague Coaching";` | Ligne liée aux rôles et aux permissions utilisateur. |
| 75 | `        return sendEmail(to, subject, body);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 76 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 77 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `CoachingEmailService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.