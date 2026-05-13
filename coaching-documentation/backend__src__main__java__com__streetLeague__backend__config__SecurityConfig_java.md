# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/config/SecurityConfig.java`

## 1. Rôle du fichier

Fichier important du module.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.config;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import org.springframework.context.annotation.Bean;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import org.springframework.context.annotation.Configuration;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import org.springframework.security.config.annotation.web.builders.HttpSecurity;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import org.springframework.security.crypto.password.PasswordEncoder;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import org.springframework.security.web.SecurityFilterChain;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import org.springframework.web.cors.CorsConfiguration;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `import org.springframework.web.cors.CorsConfigurationSource;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `import org.springframework.web.cors.UrlBasedCorsConfigurationSource;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 15 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 16 | `import java.util.Arrays;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 17 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `@Configuration` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `@EnableWebSecurity` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `public class SecurityConfig {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 23 | `    @Bean` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `        http` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `                .cors(cors -> cors.configurationSource(corsConfigurationSource()))` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `                .csrf(AbstractHttpConfigurer::disable)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `                .httpBasic(basic -> basic.disable())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 30 | `                .authorizeHttpRequests(auth -> auth` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `                        // Auth endpoint - public` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 32 | `                        .requestMatchers("/api/auth/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `                        // Endroits, sous-espaces, reservations - open for dev/test` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 34 | `                        .requestMatchers("/api/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `                        // Module Coaching` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 36 | `                        .requestMatchers("/api/programmes/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `                        .requestMatchers("/api/seances/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 38 | `                        .requestMatchers("/api/exercices/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `                        .requestMatchers("/api/seance-exercices/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `                        .requestMatchers("/api/suivis/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `                        .requestMatchers("/api/affectations/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 42 | `                        .requestMatchers("/api/reservations-seances/**").permitAll()` | Ligne liée à la réservation d’une séance par un sportif. |
| 43 | `                        .requestMatchers("/api/coaching/stats/**").permitAll()` | Ligne liée aux rôles et aux permissions utilisateur. |
| 44 | `                        .requestMatchers("/api/coaching/ai/**").permitAll()` | Ligne liée aux rôles et aux permissions utilisateur. |
| 45 | `                        .requestMatchers("/h2-console/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 46 | `                        // Sports module` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 47 | `                        .requestMatchers("/players/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `                        .requestMatchers("/teams/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `                        .requestMatchers("/terrains/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `                        .requestMatchers("/matches/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `                        .requestMatchers("/player-stats/**").permitAll()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `                        .anyRequest().authenticated()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `                );` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `        return http.build();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 55 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `    @Bean` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `    public PasswordEncoder passwordEncoder() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 59 | `        return new BCryptPasswordEncoder();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 60 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 61 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 62 | `    @Bean` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `    public CorsConfigurationSource corsConfigurationSource() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 64 | `        CorsConfiguration configuration = new CorsConfiguration();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `        configuration.setAllowedOrigins(List.of("http://localhost:4200"));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 66 | `        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `        configuration.setAllowedHeaders(List.of("*"));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `        configuration.setAllowCredentials(false);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `        source.registerCorsConfiguration("/**", configuration);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `        return source;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 72 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 73 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SecurityConfig.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.