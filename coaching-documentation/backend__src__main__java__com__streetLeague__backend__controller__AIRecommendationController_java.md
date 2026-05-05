# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/controller/AIRecommendationController.java`

## 1. Rôle du fichier

Controller REST : reçoit les requêtes HTTP envoyées par Angular.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.controller;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.service.AIRecommendationService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.service.CoachingRoleService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import org.springframework.http.ResponseEntity;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import org.springframework.web.bind.annotation.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `import java.util.Map;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * Step 9 : Endpoints pour les recommandations IA d'exercices.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` * Le coach demande des suggestions, l'IA propose, le coach valide.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 15 | `@RestController` | Déclare un controller REST Spring Boot : cette classe reçoit des requêtes HTTP et retourne du JSON. |
| 16 | `@RequestMapping("/api/coaching/ai")` | Définit l’URL de base utilisée par tous les endpoints de ce controller. |
| 17 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `@CrossOrigin(origins = "*")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `public class AIRecommendationController {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `    private final AIRecommendationService aiService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `    private final CoachingRoleService roleService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 25 | `     * Demander des recommandations d'exercices à l'IA.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `     * Seul le COACH ou ADMIN peut demander.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 27 | `     *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 28 | `     * Body JSON :` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 29 | `     * {` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 30 | `     *   "objectifProgramme": "renforcement musculaire",` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 31 | `     *   "typeSeance": "FORCE",` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 32 | `     *   "intensite": "MOYENNE",` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 33 | `     *   "nbParticipants": 4,` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 34 | `     *   "dureeSeanceMinutes": 60` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 35 | `     * }` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 36 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 37 | `    @PostMapping("/recommend")` | Déclare un endpoint HTTP POST : utilisé pour créer une nouvelle ressource. |
| 38 | `    public ResponseEntity<Map<String, Object>> recommend(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 39 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `            @RequestBody Map<String, Object> context) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 41 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 42 | `        return ResponseEntity.ok(aiService.getRecommendations(context));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 43 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 44 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 45 | `    /** Vérifier si le service IA est disponible */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 46 | `    @GetMapping("/health")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 47 | `    public ResponseEntity<Map<String, Object>> health() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 48 | `        boolean available = aiService.isAvailable();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `        return ResponseEntity.ok(Map.of(` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 50 | `                "available", available,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `                "message", available ? "Service IA opérationnel" : "Service IA indisponible"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `        ));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 54 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `AIRecommendationController.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.