# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/dto/ExerciceDTO.java`

## 1. Rôle du fichier

DTO : définit les données reçues/envoyées entre Angular et Spring Boot avec validation.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.dto;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.enums.TypeExercice;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import jakarta.validation.constraints.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 7 | `public class ExerciceDTO {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `    public static class Request {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 11 | `        @NotBlank(message = "Le nom de l'exercice est obligatoire")` | Validation backend : le texte ne doit pas être vide. |
| 12 | `        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")` | Validation backend : limite la longueur minimale ou maximale d’un texte. |
| 13 | `        private String nom;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")` | Validation backend : limite la longueur minimale ou maximale d’un texte. |
| 16 | `        private String description;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `        @NotNull(message = "Le type d'exercice est obligatoire")` | Validation backend : la valeur est obligatoire. |
| 19 | `        private TypeExercice type;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `        @Size(max = 500, message = "L'URL vidéo ne doit pas dépasser 500 caractères")` | Validation backend : limite la longueur minimale ou maximale d’un texte. |
| 22 | `        private String videoUrl;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `    public static class Response {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `        private Integer idExercice;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 28 | `        private String nom;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 29 | `        private String description;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `        @NotNull(message = "Le type d'exercice est obligatoire")` | Validation backend : la valeur est obligatoire. |
| 31 | `        private TypeExercice type;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 32 | `        private String videoUrl;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 33 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 34 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ExerciceDTO.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.