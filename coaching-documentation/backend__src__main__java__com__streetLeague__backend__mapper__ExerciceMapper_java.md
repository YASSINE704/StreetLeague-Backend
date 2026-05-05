# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/mapper/ExerciceMapper.java`

## 1. Rôle du fichier

Mapper : conversion entre Entity et DTO.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.mapper;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.dto.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `public class ExerciceMapper {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 7 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 8 | `    private ExerciceMapper() {}` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `    public static Exercice toEntity(ExerciceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 11 | `        return Exercice.builder()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 12 | `                .nom(dto.getNom())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 13 | `                .description(dto.getDescription())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `                .type(dto.getType())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `                .videoUrl(dto.getVideoUrl())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `    public static ExerciceDTO.Response toResponse(Exercice entity) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `        return ExerciceDTO.Response.builder()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 21 | `                .idExercice(entity.getIdExercice())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `                .nom(entity.getNom())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `                .description(entity.getDescription())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `                .type(entity.getType())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `                .videoUrl(entity.getVideoUrl())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 28 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 29 | `    public static SeanceExerciceDTO.Response toSeanceExerciceResponse(SeanceExercice entity) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `        return SeanceExerciceDTO.Response.builder()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 31 | `                .idSeanceExercice(entity.getIdSeanceExercice())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `                .seanceId(entity.getSeance().getIdSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `                .seanceTitre(entity.getSeance().getTitreSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `                .exerciceId(entity.getExercice().getIdExercice())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `                .exerciceNom(entity.getExercice().getNom())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `                .series(entity.getSeries())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `                .repetitions(entity.getRepetitions())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 38 | `                .charge(entity.getCharge())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `                .tempsSecondes(entity.getTempsSecondes())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `                .ordre(entity.getOrdre())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 42 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 43 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ExerciceMapper.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.