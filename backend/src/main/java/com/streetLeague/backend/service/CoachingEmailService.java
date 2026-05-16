package com.streetLeague.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Step 5 : Service d'envoi d'emails pour le module coaching.
 * Utilise Gmail SMTP configuré dans application.properties.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CoachingEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envoie un email simple.
     */
    public boolean sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("[StreetLeague Coaching] " + subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email envoyé à {} : {}", to, subject);
            return true;
        } catch (Exception e) {
            log.error("Erreur envoi email à {} : {}", to, e.getMessage());
            return false;
        }
    }

    /**
     * Envoie un rappel de séance.
     */
    public boolean sendSessionReminder(String to, String nomDestinataire,
                                        String titreSeance, String dateSeance,
                                        String heureDebut, String lieu) {
        String subject = "Rappel : Séance « " + titreSeance + " » dans 2 heures";
        String body = "Bonjour " + nomDestinataire + ",\n\n"
                + "Ceci est un rappel pour votre séance d'entraînement :\n\n"
                + "📋 Séance : " + titreSeance + "\n"
                + "📅 Date : " + dateSeance + "\n"
                + "🕐 Heure : " + (heureDebut != null ? heureDebut : "Non définie") + "\n"
                + "📍 Lieu : " + (lieu != null ? lieu : "Non défini") + "\n\n"
                + "Préparez-vous et soyez à l'heure !\n\n"
                + "— L'équipe StreetLeague Coaching";
        return sendEmail(to, subject, body);
    }

    /**
     * Envoie une alerte météo.
     */
    public boolean sendWeatherAlert(String to, String nomDestinataire,
                                     String titreSeance, String dateSeance,
                                     String recommandation) {
        String subject = "⚠️ Alerte météo : Séance « " + titreSeance + " »";
        String body = "Bonjour " + nomDestinataire + ",\n\n"
                + "Les conditions météo ne sont pas favorables pour votre séance en plein air :\n\n"
                + "📋 Séance : " + titreSeance + "\n"
                + "📅 Date : " + dateSeance + "\n\n"
                + recommandation + "\n\n"
                + "Contactez votre coach pour plus d'informations.\n\n"
                + "— L'équipe StreetLeague Coaching";
        return sendEmail(to, subject, body);
    }
}
