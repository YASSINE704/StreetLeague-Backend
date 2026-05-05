# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/mapper/SeanceMapper.java`

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
| 5 | `import com.streetLeague.backend.enums.StatutReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.enums.StatutSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 8 | `import java.util.Collections;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `public class SeanceMapper {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `    private SeanceMapper() {}` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `    public static SeanceEntrainement toEntity(SeanceEntrainementDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 16 | `        return SeanceEntrainement.builder()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 17 | `                .titreSeance(dto.getTitreSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `                .dateSeance(dto.getDateSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `                .heureDebut(dto.getHeureDebut())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `                .heureFin(dto.getHeureFin())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `                .dureeMinutes(dto.getDureeMinutes())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `                .maxParticipants(dto.getMaxParticipants() != null ? dto.getMaxParticipants() : 5)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `                .enPleinAir(dto.getEnPleinAir() != null ? dto.getEnPleinAir() : false)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `                .intensite(dto.getIntensite())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `                .statut(dto.getStatut() != null ? dto.getStatut() : StatutSeance.PREVUE)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 28 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 29 | `    public static SeanceEntrainementDTO.Response toResponse(SeanceEntrainement entity) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `        List<SeanceExerciceDTO.Response> exercices = entity.getSeanceExercices() != null` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `                ? entity.getSeanceExercices().stream().map(ExerciceMapper::toSeanceExerciceResponse).toList()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `                : Collections.emptyList();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 34 | `        SuiviSeanceDTO.Response suivi = entity.getSuiviSeance() != null` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `                ? SuiviMapper.toResponse(entity.getSuiviSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `                : null;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `        /* Calculer les réservations actives et les places restantes */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 39 | `        List<ReservationSeanceDTO.Response> reservations = Collections.emptyList();` | Ligne liée à la réservation d’une séance par un sportif. |
| 40 | `        int placesRestantes = entity.getMaxParticipants() != null ? entity.getMaxParticipants() : 5;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 42 | `        if (entity.getReservations() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 43 | `            reservations = entity.getReservations().stream()` | Ligne liée à la réservation d’une séance par un sportif. |
| 44 | `                    .map(ReservationSeanceMapper::toResponse).toList();` | Ligne liée à la réservation d’une séance par un sportif. |
| 45 | `            long reserveesActives = entity.getReservations().stream()` | Ligne liée à la réservation d’une séance par un sportif. |
| 46 | `                    .filter(r -> r.getStatut() != StatutReservationSeance.ANNULEE)` | Ligne liée à la réservation d’une séance par un sportif. |
| 47 | `                    .count();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `            placesRestantes = Math.max(0, placesRestantes - (int) reserveesActives);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 50 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 51 | `        return SeanceEntrainementDTO.Response.builder()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 52 | `                .idSeance(entity.getIdSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `                .titreSeance(entity.getTitreSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `                .dateSeance(entity.getDateSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `                .heureDebut(entity.getHeureDebut())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `                .heureFin(entity.getHeureFin())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `                .dureeMinutes(entity.getDureeMinutes())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `                .maxParticipants(entity.getMaxParticipants())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 59 | `                .placesRestantes(placesRestantes)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `                .intensite(entity.getIntensite())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `                .statut(entity.getStatut())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `                .programmeId(entity.getProgramme().getIdProgramme())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `                .programmeTitre(entity.getProgramme().getTitre())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 64 | `                /* Step 4 : lieu */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 65 | `                .sousEspaceId(entity.getLieu() != null ? entity.getLieu().getId() : null)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 66 | `                .lieuNom(entity.getLieu() != null ? entity.getLieu().getNom() : null)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `                .endroitNom(entity.getLieu() != null && entity.getLieu().getEndroit() != null` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `                        ? entity.getLieu().getEndroit().getNom() : null)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `                .enPleinAir(entity.getEnPleinAir())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `                .exercices(exercices)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `                .suiviSeance(suivi)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `                .reservations(reservations)` | Ligne liée à la réservation d’une séance par un sportif. |
| 73 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 74 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 75 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SeanceMapper.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.