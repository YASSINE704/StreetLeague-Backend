package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.entity.SousEspace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingService {

    private final WeatherService weatherService;

    // Coordonnées par défaut (Tunis) — utilisées si l'endroit n'a pas de coords
    private static final double DEFAULT_LAT = 36.8065;
    private static final double DEFAULT_LON = 10.1815;

    /**
     * Calcule le prix total d'une réservation.
     *
     * Règles :
     *  - Prix de base = prixBase (DT/h) × durée en heures
     *  - +20% si weekend (sam/dim)
     *  - +15% si heure de pointe (18h–22h)
     *  - -10% si mauvais temps (pluie, orage...)
     */
    public double calculerPrix(Reservation reservation, SousEspace sousEspace) {
        double prixBase = sousEspace.getPrixBase() != null ? sousEspace.getPrixBase() : 10.0;

        LocalDateTime debut = reservation.getDateDebut();
        LocalDateTime fin   = reservation.getDateFin();

        // Durée en heures
        double dureeHeures = java.time.Duration.between(debut, fin).toMinutes() / 60.0;

        double multiplicateur = 1.0;
        StringBuilder detail = new StringBuilder();

        // Weekend +20%
        DayOfWeek jour = debut.getDayOfWeek();
        if (jour == DayOfWeek.SATURDAY || jour == DayOfWeek.SUNDAY) {
            multiplicateur += 0.20;
            detail.append("+20% weekend ");
        }

        // Heure de pointe 18h-22h +15%
        int heure = debut.getHour();
        if (heure >= 18 && heure < 22) {
            multiplicateur += 0.15;
            detail.append("+15% heure de pointe ");
        }

        double prix = prixBase * dureeHeures * multiplicateur;

        // Mauvais temps -10%
        try {
            WeatherService.WeatherInfo weather = weatherService.getWeatherForecast(DEFAULT_LAT, DEFAULT_LON);
            if (weather != null && weather.isBadWeather()) {
                prix *= 0.90;
                detail.append("-10% mauvais temps (").append(weather.description()).append(") ");
            }
        } catch (Exception e) {
            log.debug("Météo indisponible pour le calcul de prix : {}", e.getMessage());
        }

        log.info("Prix calculé pour réservation: {}DT ({}) — base={}DT/h × {}h × {}",
                String.format("%.2f", prix), detail.toString().trim(),
                prixBase, String.format("%.2f", dureeHeures), multiplicateur);

        return Math.round(prix * 100.0) / 100.0;
    }
}
