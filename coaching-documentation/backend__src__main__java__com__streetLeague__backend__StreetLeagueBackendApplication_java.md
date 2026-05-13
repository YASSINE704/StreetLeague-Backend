# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/StreetLeagueBackendApplication.java`

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
| 1 | `package com.streetLeague.backend;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import org.springframework.boot.SpringApplication;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import org.springframework.boot.autoconfigure.SpringBootApplication;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import org.springframework.scheduling.annotation.EnableScheduling;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 7 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 8 | ` * StreetLeague Backend Application - Main Entry Point.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 9 | ` * ` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 10 | ` * This is the main Spring Boot application class that initializes the StreetLeague` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 11 | ` * backend API server. The application manages street sports teams, players, matches,` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * and terrains.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` * ` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | ` * Database: MySQL (via XAMPP)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 15 | ` * Port: 8080` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 16 | ` * API Base Path: /api` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 17 | ` * ` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 18 | ` * @author StreetLeague Team` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 19 | ` * @version 1.0` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 20 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 21 | `@SpringBootApplication` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `@EnableScheduling  // Step 5 : active les tâches planifiées (@Scheduled)` | Tâche automatique planifiée : Spring exécute cette méthode régulièrement. |
| 23 | `public class StreetLeagueBackendApplication {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `     * Main method to run the Spring Boot application.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 27 | `     * ` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 28 | `     * @param args Command line arguments` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 29 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 30 | `    public static void main(String[] args) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 31 | `        SpringApplication.run(StreetLeagueBackendApplication.class, args);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 33 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 34 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `StreetLeagueBackendApplication.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.