# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/mapper/ReservationSeanceMapper.java`

## 1. Rôle du fichier

Mapper : conversion entre Entity et DTO.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Participe au workflow de réservation et aux contrôles de capacité/doublons/chevauchements.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.mapper;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.dto.ReservationSeanceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.ReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `public class ReservationSeanceMapper {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 7 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 8 | `    private ReservationSeanceMapper() {}` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `    public static ReservationSeanceDTO.Response toResponse(ReservationSeance entity) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 11 | `        return ReservationSeanceDTO.Response.builder()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 12 | `                .idReservation(entity.getIdReservation())` | Ligne liée à la réservation d’une séance par un sportif. |
| 13 | `                .dateReservation(entity.getDateReservation())` | Ligne liée à la réservation d’une séance par un sportif. |
| 14 | `                .statut(entity.getStatut())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `                .modePaiement(entity.getModePaiement())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `                .statutPaiement(entity.getStatutPaiement())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `                .montant(entity.getMontant())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `                .motifAnnulation(entity.getMotifAnnulation())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `                .userId(entity.getUser().getIdUser())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `                .userNom(entity.getUser().getNom() + " " + entity.getUser().getPrenom())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `                .seanceId(entity.getSeance().getIdSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `                .seanceTitre(entity.getSeance().getTitreSeance())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 25 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ReservationSeanceMapper.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.