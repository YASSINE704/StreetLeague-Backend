# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/entity/ReservationSeance.java`

## 1. Rôle du fichier

Entité JPA : représente une table ou un objet métier persistant.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Participe au workflow de réservation et aux contrôles de capacité/doublons/chevauchements.
- Relié à MySQL via JPA/Hibernate.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.entity;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.enums.ModePaiement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.StatutPaiement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.enums.StatutReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import jakarta.persistence.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `import java.time.LocalDateTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * Réservation d'un sportif pour une séance de coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` * Un sportif réserve sa place dans une séance — le coach peut confirmer ou annuler.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 15 | `@Entity` | Déclare une entité JPA : Hibernate va créer/mapper une table pour cette classe. |
| 16 | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `public class ReservationSeance {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `    @Id` | Indique que ce champ est la clé primaire de la table. |
| 20 | `    @GeneratedValue(strategy = GenerationType.IDENTITY)` | Indique que la valeur de l’identifiant est générée automatiquement par la base/Hibernate. |
| 21 | `    private Integer idReservation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 23 | `    private LocalDateTime dateReservation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 26 | `    private StatutReservationSeance statut;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 28 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 29 | `    private ModePaiement modePaiement;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `    /* Step 6 : statut du paiement */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 32 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 33 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `    private StatutPaiement statutPaiement = StatutPaiement.EN_ATTENTE;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 35 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 36 | `    private Double montant;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `    private String motifAnnulation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 39 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 40 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 41 | `    @JoinColumn(name = "user_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 42 | `    private User user;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 43 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 44 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 45 | `    @JoinColumn(name = "seance_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 46 | `    private SeanceEntrainement seance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 47 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ReservationSeance.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.