# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/WeatherService.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Participe aux fonctionnalités météo, vêtements recommandés et notifications/rappels. 
- Appelé par un controller et utilise souvent un repository pour appliquer les règles métier.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.service;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import lombok.extern.slf4j.Slf4j;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import org.springframework.beans.factory.annotation.Value;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import org.springframework.web.client.RestTemplate;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 8 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import java.util.Map;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * Step 4 : Service météo utilisant l'API OpenWeatherMap.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` * Vérifie les conditions météo pour les séances en plein air.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | ` *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 15 | ` * Pour activer : ajouter dans application.properties :` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 16 | ` *   weather.api.key=VOTRE_CLE_API` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 17 | ` *   weather.api.enabled=true` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 18 | ` *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 19 | ` * Clé gratuite sur : https://openweathermap.org/api` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 20 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 21 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 22 | `@Slf4j` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `public class WeatherService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    @Value("${weather.api.key:}")` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 26 | `    private String apiKey;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 28 | `    @Value("${weather.api.enabled:false}")` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 29 | `    private boolean enabled;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `    private final RestTemplate restTemplate = new RestTemplate();` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 32 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 33 | `    private static final String FORECAST_URL =` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 34 | `            "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={key}&units=metric&lang=fr";` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 35 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 36 | `    /** Codes météo considérés comme "mauvais temps" */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 37 | `    private static final List<String> BAD_WEATHER_CODES = List.of(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 38 | `            "Rain", "Drizzle", "Thunderstorm", "Snow", "Squall", "Tornado"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `    );` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 41 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 42 | `     * Vérifie la météo pour une position géographique.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 43 | `     * Retourne un résumé météo ou null si le service est désactivé/indisponible.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 44 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 45 | `    public WeatherInfo getWeatherForecast(Double latitude, Double longitude) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 46 | `        if (!enabled \|\| apiKey == null \|\| apiKey.isBlank()) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 47 | `            log.debug("Service météo désactivé ou clé API manquante");` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 48 | `            return null;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 49 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 50 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 51 | `        try {` | Début d’un bloc de sécurité pour gérer les erreurs possibles. |
| 52 | `            @SuppressWarnings("unchecked")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `            Map<String, Object> response = restTemplate.getForObject(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `                    FORECAST_URL, Map.class, latitude, longitude, apiKey);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 56 | `            if (response == null \|\| !response.containsKey("list")) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 57 | `                return null;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 58 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 59 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 60 | `            @SuppressWarnings("unchecked")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `            List<Map<String, Object>> forecasts = (List<Map<String, Object>>) response.get("list");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 63 | `            if (forecasts.isEmpty()) return null;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 64 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 65 | `            // Prendre la première prévision (3h)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 66 | `            Map<String, Object> first = forecasts.get(0);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `            @SuppressWarnings("unchecked")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `            List<Map<String, Object>> weather = (List<Map<String, Object>>) first.get("weather");` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 69 | `            @SuppressWarnings("unchecked")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `            Map<String, Object> main = (Map<String, Object>) first.get("main");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 72 | `            String condition = weather.get(0).get("main").toString();` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 73 | `            String description = weather.get(0).get("description").toString();` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 74 | `            double temp = ((Number) main.get("temp")).doubleValue();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `            boolean isBadWeather = BAD_WEATHER_CODES.contains(condition);` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 76 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 77 | `            return new WeatherInfo(condition, description, temp, isBadWeather);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 78 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 79 | `        } catch (Exception e) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 80 | `            log.warn("Erreur lors de l'appel à l'API météo : {}", e.getMessage());` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 81 | `            return null;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 82 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 83 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 84 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 85 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 86 | `     * Vérifie si les conditions météo sont mauvaises pour une séance en plein air.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 87 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 88 | `    public boolean isBadWeatherForOutdoor(Double latitude, Double longitude) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 89 | `        WeatherInfo info = getWeatherForecast(latitude, longitude);` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 90 | `        return info != null && info.isBadWeather();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 91 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 92 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 93 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 94 | `     * Génère un message de recommandation météo.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 95 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 96 | `    public String getWeatherRecommendation(WeatherInfo info) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 97 | `        if (info == null) return null;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 98 | `        if (!info.isBadWeather()) return null;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 99 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 100 | `        StringBuilder msg = new StringBuilder();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 101 | `        msg.append("⚠️ Alerte météo : ").append(info.description());` | Ligne liée à la météo ou aux recommandations selon les conditions extérieures. |
| 102 | `        msg.append(" (").append(String.format("%.1f", info.temperature())).append("°C). ");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 103 | `        msg.append("Recommandations : ");` | Ligne liée à la recommandation IA d’exercices. |
| 104 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 105 | `        if (info.condition().equals("Rain") \|\| info.condition().equals("Drizzle")) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 106 | `            msg.append("Déplacer la séance en intérieur si possible, ou reporter. Prévoir des vêtements imperméables.");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 107 | `        } else if (info.condition().equals("Thunderstorm")) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 108 | `            msg.append("ANNULER ou REPORTER la séance. Danger d'orage.");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 109 | `        } else if (info.condition().equals("Snow")) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 110 | `            msg.append("Déplacer en intérieur. Prévoir des vêtements chauds si maintien en extérieur.");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 111 | `        } else {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 112 | `            msg.append("Vérifier les conditions avant la séance. Prévoir un plan B en intérieur.");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 113 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 114 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 115 | `        return msg.toString();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 116 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 117 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 118 | `    /** Record immutable pour les infos météo */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 119 | `    public record WeatherInfo(String condition, String description, double temperature, boolean isBadWeather) {}` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 120 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `WeatherService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.