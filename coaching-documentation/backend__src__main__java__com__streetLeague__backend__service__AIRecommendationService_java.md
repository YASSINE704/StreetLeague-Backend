# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/AIRecommendationService.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
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
| 8 | `import java.util.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 11 | ` * Step 9 : Service qui communique avec le microservice Python AI` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * pour obtenir des recommandations d'exercices.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | ` * Si le service Python est indisponible, retourne une liste vide (fallback).` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 15 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 16 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 17 | `@Slf4j` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `public class AIRecommendationService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `    @Value("${ai.service.url:http://localhost:5000}")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `    private String aiServiceUrl;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 23 | `    @Value("${ai.service.enabled:true}")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `    private boolean enabled;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 26 | `    private final RestTemplate restTemplate = new RestTemplate();` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 28 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 29 | `     * Demande des recommandations d'exercices au service Python AI.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 30 | `     *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 31 | `     * @param context Map contenant : objectifProgramme, typeSeance, intensite,` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 32 | `     *                nbParticipants, niveauJoueurs, dureeSeanceMinutes, lieuType, enPleinAir` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 33 | `     * @return Map avec status et liste de recommandations, ou fallback si indisponible` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 34 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 35 | `    @SuppressWarnings("unchecked")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `    public Map<String, Object> getRecommendations(Map<String, Object> context) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 37 | `        if (!enabled) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 38 | `            return fallbackResponse("Service IA désactivé");` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 39 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 40 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 41 | `        try {` | Début d’un bloc de sécurité pour gérer les erreurs possibles. |
| 42 | `            Map<String, Object> response = restTemplate.postForObject(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `                    aiServiceUrl + "/api/ai/recommend", context, Map.class);` | Ligne liée à la recommandation IA d’exercices. |
| 44 | `            if (response != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 45 | `                log.info("Recommandations IA reçues : {} exercices", response.get("nbRecommandations"));` | Ligne liée à la recommandation IA d’exercices. |
| 46 | `                return response;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 47 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 48 | `            return fallbackResponse("Réponse vide du service IA");` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 49 | `        } catch (Exception e) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 50 | `            log.warn("Service IA indisponible : {}", e.getMessage());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `            return fallbackResponse("Service IA indisponible — ajoutez les exercices manuellement");` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 52 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 53 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 54 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 55 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 56 | `     * Vérifie si le service Python AI est en ligne.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 57 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 58 | `    public boolean isAvailable() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 59 | `        if (!enabled) return false;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 60 | `        try {` | Début d’un bloc de sécurité pour gérer les erreurs possibles. |
| 61 | `            restTemplate.getForObject(aiServiceUrl + "/api/ai/health", Map.class);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `            return true;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 63 | `        } catch (Exception e) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 64 | `            return false;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 65 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 66 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 67 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 68 | `    private Map<String, Object> fallbackResponse(String message) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 69 | `        Map<String, Object> fallback = new LinkedHashMap<>();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `        fallback.put("status", "fallback");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `        fallback.put("message", message);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `        fallback.put("nbRecommandations", 0);` | Ligne liée à la recommandation IA d’exercices. |
| 73 | `        fallback.put("recommandations", Collections.emptyList());` | Ligne liée à la recommandation IA d’exercices. |
| 74 | `        return fallback;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 75 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 76 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `AIRecommendationService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.