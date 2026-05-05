# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/dto/ReservationSeanceDTO.java`

## 1. Rôle du fichier

DTO : définit les données reçues/envoyées entre Angular et Spring Boot avec validation.

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
| 1 | `package com.streetLeague.backend.dto;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.enums.ModePaiement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.StatutPaiement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.enums.StatutReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import jakarta.validation.constraints.NotNull;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `import java.time.LocalDateTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `public class ReservationSeanceDTO {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `    public static class Request {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 15 | `        @NotNull(message = "L'identifiant de la séance est obligatoire")` | Validation backend : la valeur est obligatoire. |
| 16 | `        private Integer seanceId;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `        @NotNull(message = "Le mode de paiement est obligatoire")` | Validation backend : la valeur est obligatoire. |
| 19 | `        private ModePaiement modePaiement;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `    public static class Response {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `        private Integer idReservation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `        private LocalDateTime dateReservation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 26 | `        private StatutReservationSeance statut;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `        private ModePaiement modePaiement;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 28 | `        private StatutPaiement statutPaiement;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 29 | `        private Double montant;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `        private String motifAnnulation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 31 | `        private Integer userId;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 32 | `        private String userNom;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 33 | `        private Integer seanceId;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 34 | `        private String seanceTitre;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 35 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 36 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ReservationSeanceDTO.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.