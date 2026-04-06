package com.streetLeague.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for StreetLeague Backend API.
 * 
 * This configuration class defines security rules for the REST API.
 * Currently allows public access to all endpoints for development.
 * In production, implement JWT or OAuth2 authentication.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * Current setup for development:
     * - CSRF is disabled (suitable for stateless REST APIs)
     * - All requests are allowed without authentication
     * - Session creation is disabled (stateless)
     * 
     * @param http The HttpSecurity object to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
            // Disable CSRF for REST API (stateless)
            .csrf(csrf -> csrf.disable())
            
            // Configure authorization for development (allow all)
            // TODO: Implement proper authentication in production
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Allow all requests during development
            )
            
            // Use stateless session (no server-side session storage)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Enable HTTP Basic authentication for testing if needed
            .httpBasic();

        return http.build();
    }
}