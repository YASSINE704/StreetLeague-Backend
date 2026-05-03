package com.streetLeague.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.disable())
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoint - public
                        .requestMatchers("/api/auth/**").permitAll()
                        // Endroits, sous-espaces, reservations - open for dev/test
                        .requestMatchers("/api/**").permitAll()
                        // Module Coaching
                        .requestMatchers("/api/programmes/**").permitAll()
                        .requestMatchers("/api/seances/**").permitAll()
                        .requestMatchers("/api/exercices/**").permitAll()
                        .requestMatchers("/api/seance-exercices/**").permitAll()
                        .requestMatchers("/api/suivis/**").permitAll()
                        .requestMatchers("/api/affectations/**").permitAll()
                        .requestMatchers("/api/reservations-seances/**").permitAll()
                        .requestMatchers("/api/coaching/stats/**").permitAll()
                        .requestMatchers("/api/coaching/ai/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // Sports module
                        .requestMatchers("/players/**").permitAll()
                        .requestMatchers("/teams/**").permitAll()
                        .requestMatchers("/terrains/**").permitAll()
                        .requestMatchers("/matches/**").permitAll()
                        .requestMatchers("/player-stats/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
