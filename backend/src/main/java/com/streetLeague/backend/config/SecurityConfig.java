package com.streetLeague.backend.config;

import com.streetLeague.backend.security.JwtAuthenticationFilter;
import com.streetLeague.backend.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/programmes/**").hasAnyRole("COACH", "ADMIN", "SPORTIF", "JOUEUR")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/seances/**").hasAnyRole("COACH", "ADMIN", "SPORTIF", "JOUEUR")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/exercices/**").hasAnyRole("COACH", "ADMIN", "SPORTIF", "JOUEUR")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/seance-exercices/**").hasAnyRole("COACH", "ADMIN", "SPORTIF", "JOUEUR")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/coaching/**").hasAnyRole("COACH", "ADMIN", "SPORTIF", "JOUEUR")
                        .requestMatchers("/api/programmes/**").hasAnyRole("COACH", "ADMIN")
                        .requestMatchers("/api/seances/**").hasAnyRole("COACH", "ADMIN")
                        .requestMatchers("/api/exercices/**").hasAnyRole("COACH", "ADMIN")
                        .requestMatchers("/api/seance-exercices/**").hasAnyRole("COACH", "ADMIN")
                        .requestMatchers("/api/suivis/**").hasAnyRole("COACH", "ADMIN", "SPORTIF", "JOUEUR")
                        .requestMatchers("/api/affectations/**").hasAnyRole("COACH", "ADMIN")
                        .requestMatchers("/api/coaching/**").hasAnyRole("COACH", "ADMIN")
                        .requestMatchers("/api/reservations-seances/**").hasAnyRole("SPORTIF", "JOUEUR", "COACH", "ADMIN")
                        .requestMatchers("/terrains/**").hasAnyRole("TERRAIN_MANAGER", "ADMIN")
                        .requestMatchers("/players/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "ADMIN")
                        .requestMatchers("/teams/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "ADMIN")
                        .requestMatchers("/matches/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "TERRAIN_MANAGER", "ADMIN")
                        .requestMatchers("/player-stats/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
