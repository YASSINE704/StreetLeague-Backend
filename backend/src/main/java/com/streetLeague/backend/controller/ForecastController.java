package com.streetLeague.backend.controller;

import com.streetLeague.backend.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoint for AI-powered reservation forecasting.
 * Uses SARIMA model via Python microservice.
 */
@RestController
@RequestMapping("/api/forecast")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ForecastController {

    private final ForecastService forecastService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getForecast(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(forecastService.getForecast(days));
    }
}
