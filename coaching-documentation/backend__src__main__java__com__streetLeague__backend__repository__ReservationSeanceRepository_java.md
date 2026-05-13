# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/repository/ReservationSeanceRepository.java`

## 1. Rôle du fichier

Repository Spring Data JPA : accès à la base MySQL.

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
| 1 | `package com.streetLeague.backend.repository;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.entity.ReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.StatutReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import org.springframework.data.jpa.repository.JpaRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 7 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import java.util.Optional;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `public interface ReservationSeanceRepository extends JpaRepository<ReservationSeance, Integer> {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `    /** Toutes les réservations actives (non annulées) pour une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | `    List<ReservationSeance> findBySeanceIdSeanceAndStatutNot(Integer seanceId, StatutReservationSeance statut);` | Ligne liée à la réservation d’une séance par un sportif. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `    /** Vérifier si un sportif a déjà réservé cette séance (non annulée) */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 16 | `    Optional<ReservationSeance> findByUserIdUserAndSeanceIdSeanceAndStatutNot(` | Ligne liée à la réservation d’une séance par un sportif. |
| 17 | `            Integer userId, Integer seanceId, StatutReservationSeance statut);` | Ligne liée à la réservation d’une séance par un sportif. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `    /** Toutes les réservations d'un sportif (non annulées) */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 20 | `    List<ReservationSeance> findByUserIdUserAndStatutNot(Integer userId, StatutReservationSeance statut);` | Ligne liée à la réservation d’une séance par un sportif. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `    /** Toutes les réservations pour une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 23 | `    List<ReservationSeance> findBySeanceIdSeance(Integer seanceId);` | Ligne liée à la réservation d’une séance par un sportif. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    /** Toutes les réservations d'un sportif */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `    List<ReservationSeance> findByUserIdUser(Integer userId);` | Ligne liée à la réservation d’une séance par un sportif. |
| 27 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ReservationSeanceRepository.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.