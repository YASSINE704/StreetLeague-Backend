package com.streetLeague.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    private WeatherService weatherService = new WeatherService();

    @Test
    void getWeatherForecast_shouldReturnNull_whenDisabled() {
        // By default, weather.api.enabled=false and key is empty
        WeatherService.WeatherInfo result = weatherService.getWeatherForecast(36.8, 10.18);

        assertNull(result, "Should return null when service is disabled");
    }

    @Test
    void getWeatherRecommendation_shouldReturnNull_whenInfoIsNull() {
        String result = weatherService.getWeatherRecommendation(null);

        assertNull(result);
    }

    @Test
    void getWeatherRecommendation_shouldReturnNull_whenWeatherIsGood() {
        WeatherService.WeatherInfo goodWeather = new WeatherService.WeatherInfo("Clear", "ciel dégagé", 25.0, false);

        String result = weatherService.getWeatherRecommendation(goodWeather);

        assertNull(result, "No recommendation needed for good weather");
    }

    @Test
    void getWeatherRecommendation_shouldReturnMessage_whenRain() {
        WeatherService.WeatherInfo rain = new WeatherService.WeatherInfo("Rain", "pluie modérée", 15.0, true);

        String result = weatherService.getWeatherRecommendation(rain);

        assertNotNull(result);
        assertTrue(result.contains("imperméables"));
    }

    @Test
    void getWeatherRecommendation_shouldReturnMessage_whenThunderstorm() {
        WeatherService.WeatherInfo storm = new WeatherService.WeatherInfo("Thunderstorm", "orage", 20.0, true);

        String result = weatherService.getWeatherRecommendation(storm);

        assertNotNull(result);
        assertTrue(result.contains("ANNULER") || result.contains("REPORTER"));
    }

    @Test
    void getWeatherRecommendation_shouldReturnMessage_whenSnow() {
        WeatherService.WeatherInfo snow = new WeatherService.WeatherInfo("Snow", "neige", 0.0, true);

        String result = weatherService.getWeatherRecommendation(snow);

        assertNotNull(result);
        assertTrue(result.contains("chauds"));
    }

    @Test
    void isBadWeatherForOutdoor_shouldReturnFalse_whenDisabled() {
        boolean result = weatherService.isBadWeatherForOutdoor(36.8, 10.18);

        assertFalse(result, "Should return false when service is disabled");
    }
}
