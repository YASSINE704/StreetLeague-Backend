package com.streetLeague.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public void sendVerificationCode(String to, String code) {
        sendEmail(
                to,
                "StreetLeague email verification code",
                """
                Hello,

                Your StreetLeague verification code is: %s

                This code expires in 10 minutes.
                If you did not create a StreetLeague account, you can ignore this email.
                """.formatted(code)
        );
    }

    public void sendPasswordResetCode(String to, String code) {
        sendEmail(
                to,
                "StreetLeague password reset code",
                """
                Hello,

                Your StreetLeague password reset code is: %s

                This code expires in 10 minutes.
                If you did not request a password reset, you can ignore this email.
                """.formatted(code)
        );
    }

    private void sendEmail(String to, String subject, String message) {
        if (!emailEnabled) {
            log.info("Email sending disabled. Email to [{}], subject [{}], body [{}]", to, subject, message);
            return;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            log.info("Email sent to [{}] with subject [{}]", to, subject);
        } catch (Exception ex) {
            log.error("Failed to send email to [{}]. Check SMTP credentials/settings.", to, ex);
            throw new IllegalStateException("Could not send email. Please try again later.");
        }
    }
}
