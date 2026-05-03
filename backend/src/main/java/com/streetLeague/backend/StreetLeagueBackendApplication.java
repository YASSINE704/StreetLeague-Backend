package com.streetLeague.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * StreetLeague Backend Application - Main Entry Point.
 * 
 * This is the main Spring Boot application class that initializes the StreetLeague
 * backend API server. The application manages street sports teams, players, matches,
 * and terrains.
 * 
 * Database: MySQL (via XAMPP)
 * Port: 8080
 * API Base Path: /api
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling  // Step 5 : active les tâches planifiées (@Scheduled)
public class StreetLeagueBackendApplication {

    /**
     * Main method to run the Spring Boot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(StreetLeagueBackendApplication.class, args);
    }

}
