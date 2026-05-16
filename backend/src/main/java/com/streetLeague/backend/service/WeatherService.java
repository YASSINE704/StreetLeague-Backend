package com.streetLeague.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Step 4 : Service météo utilisant l'API OpenWeatherMap.
 * Vérifie les conditions météo pour les séances en plein air.
 *
 * Pour activer : ajouter dans application.properties :
 *   weather.api.key=VOTRE_CLE_API
 *   weather.api.enabled=true
 *
 * Clé gratuite sur : https://openweathermap.org/api
 */
@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key:}")
    private String apiKey;

    @Value("${weather.api.enabled:false}")
    private boolean enabled;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={key}&units=metric&lang=fr";

    /** Codes météo considérés comme "mauvais temps" */
    private static final List<String> BAD_WEATHER_CODES = List.of(
            "Rain", "Drizzle", "Thunderstorm", "Snow", "Squall", "Tornado"
    );

    /**
     * Vérifie la météo pour une position géographique.
     * Retourne un résumé météo ou null si le service est désactivé/indisponible.
     */
    public WeatherInfo getWeatherForecast(Double latitude, Double longitude) {
        if (!enabled || apiKey == null || apiKey.isBlank()) {
            log.debug("Service météo désactivé ou clé API manquante");
            return null;
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(
                    FORECAST_URL, Map.class, latitude, longitude, apiKey);

            if (response == null || !response.containsKey("list")) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> forecasts = (List<Map<String, Object>>) response.get("list");

            if (forecasts.isEmpty()) return null;

            // Prendre la première prévision (3h)
            Map<String, Object> first = forecasts.get(0);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> weather = (List<Map<String, Object>>) first.get("weather");
            @SuppressWarnings("unchecked")
            Map<String, Object> main = (Map<String, Object>) first.get("main");

            String condition = weather.get(0).get("main").toString();
            String description = weather.get(0).get("description").toString();
            double temp = ((Number) main.get("temp")).doubleValue();
            boolean isBadWeather = BAD_WEATHER_CODES.contains(condition);

            return new WeatherInfo(condition, description, temp, isBadWeather);

        } catch (Exception e) {
            log.warn("Erreur lors de l'appel à l'API météo : {}", e.getMessage());
            return null;
        }
    }

    /**
     * Vérifie si les conditions météo sont mauvaises pour une séance en plein air.
     */
    public boolean isBadWeatherForOutdoor(Double latitude, Double longitude) {
        WeatherInfo info = getWeatherForecast(latitude, longitude);
        return info != null && info.isBadWeather();
    }

    /**
     * Génère un message de recommandation météo.
     */
    public String getWeatherRecommendation(WeatherInfo info) {
        if (info == null) return null;
        if (!info.isBadWeather()) return null;

        StringBuilder msg = new StringBuilder();
        msg.append("⚠️ Alerte météo : ").append(info.description());
        msg.append(" (").append(String.format("%.1f", info.temperature())).append("°C). ");
        msg.append("Recommandations : ");

        if (info.condition().equals("Rain") || info.condition().equals("Drizzle")) {
            msg.append("Déplacer la séance en intérieur si possible, ou reporter. Prévoir des vêtements imperméables.");
        } else if (info.condition().equals("Thunderstorm")) {
            msg.append("ANNULER ou REPORTER la séance. Danger d'orage.");
        } else if (info.condition().equals("Snow")) {
            msg.append("Déplacer en intérieur. Prévoir des vêtements chauds si maintien en extérieur.");
        } else {
            msg.append("Vérifier les conditions avant la séance. Prévoir un plan B en intérieur.");
        }

        return msg.toString();
    }

    /** Record immutable pour les infos météo */
    public record WeatherInfo(String condition, String description, double temperature, boolean isBadWeather) {}
}
