package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Forecast service that:
 * 1. Aggregates historical reservation counts per day from the DB
 * 2. Sends them to the Python SARIMA microservice
 * 3. Returns the 7-day forecast to the frontend
 */
@Service
@RequiredArgsConstructor
public class ForecastService {

    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;

    private static final String PYTHON_SERVICE_URL = "http://localhost:5001/predict";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Build daily history from all reservations and call SARIMA microservice.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getForecast(int forecastDays) {
        List<Reservation> all = reservationRepository.findAll();

        // Aggregate: count reservations per day (by dateCreation)
        Map<String, Long> countByDay = all.stream()
                .filter(r -> r.getDateCreation() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getDateCreation().toLocalDate().format(DATE_FMT),
                        Collectors.counting()
                ));

        if (countByDay.isEmpty()) {
            // No data — return empty forecast with zeros
            return buildEmptyForecast(forecastDays);
        }

        // Build history list sorted by date
        List<Map<String, Object>> history = countByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    Map<String, Object> point = new LinkedHashMap<>();
                    point.put("date", e.getKey());
                    point.put("count", e.getValue());
                    return point;
                })
                .collect(Collectors.toList());

        // Call Python SARIMA service
        Map<String, Object> payload = new HashMap<>();
        payload.put("history", history);
        payload.put("forecast_days", forecastDays);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(PYTHON_SERVICE_URL, request, Map.class);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("forecast", response.getBody().get("forecast"));
            result.put("model", response.getBody().get("model"));
            result.put("historyPoints", response.getBody().get("history_points"));
            result.put("history", history);
            return result;

        } catch (Exception e) {
            // Python service unavailable — return history + empty forecast
            Map<String, Object> fallback = new LinkedHashMap<>();
            fallback.put("forecast", buildEmptyForecast(forecastDays).get("forecast"));
            fallback.put("model", "Service indisponible");
            fallback.put("historyPoints", history.size());
            fallback.put("history", history);
            fallback.put("error", "Microservice SARIMA indisponible: " + e.getMessage());
            return fallback;
        }
    }

    private Map<String, Object> buildEmptyForecast(int days) {
        List<Map<String, Object>> forecast = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 1; i <= days; i++) {
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("date", today.plusDays(i).format(DATE_FMT));
            point.put("predicted", 0);
            forecast.add(point);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("forecast", forecast);
        result.put("model", "Aucune donnée historique");
        result.put("historyPoints", 0);
        result.put("history", Collections.emptyList());
        return result;
    }
}
