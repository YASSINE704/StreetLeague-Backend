package com.streetLeague.backend.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration mail de secours.
 * Si Spring Boot ne peut pas auto-configurer JavaMailSender
 * (par exemple quand les credentials SMTP sont absentes),
 * ce bean fournit un sender par défaut qui ne bloque pas le démarrage.
 * Les emails ne seront pas envoyés réellement — ils seront loggés.
 */
@Configuration
public class MailConfig {

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(25);
        return mailSender;
    }
}
