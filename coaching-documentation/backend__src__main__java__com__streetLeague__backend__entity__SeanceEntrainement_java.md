# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/entity/SeanceEntrainement.java`

## 1. Rôle du fichier

Entité JPA : représente une table ou un objet métier persistant.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Relié à MySQL via JPA/Hibernate.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.entity;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.enums.Intensite;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.StatutSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import jakarta.persistence.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 8 | `import java.time.LocalDate;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import java.time.LocalTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import java.util.ArrayList;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `@Entity` | Déclare une entité JPA : Hibernate va créer/mapper une table pour cette classe. |
| 14 | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `public class SeanceEntrainement {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `    @Id` | Indique que ce champ est la clé primaire de la table. |
| 18 | `    @GeneratedValue(strategy = GenerationType.IDENTITY)` | Indique que la valeur de l’identifiant est générée automatiquement par la base/Hibernate. |
| 19 | `    private Integer idSeance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `    private String titreSeance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `    private LocalDate dateSeance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `    private Integer dureeMinutes;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    /* ── Champs ajoutés pour Step 2 : réservation & planning ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | `    private LocalTime heureDebut;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `    private LocalTime heureFin;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 28 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 29 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 30 | `    private Integer maxParticipants = 5;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 31 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 32 | `    /* ── Step 4 : lieu et météo ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 33 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 34 | `    @JoinColumn(name = "sous_espace_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 35 | `    private SousEspace lieu;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 36 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 37 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 38 | `    private Boolean enPleinAir = false;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 39 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 40 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 41 | `    private Intensite intensite;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 42 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 43 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 44 | `    private StatutSeance statut;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 45 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 46 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 47 | `    @JoinColumn(name = "programme_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 48 | `    private ProgrammeEntrainement programme;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 49 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 50 | `    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)` | Relation JPA 1→N : cet objet parent possède une liste d’objets enfants. |
| 51 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `    private List<SeanceExercice> seanceExercices = new ArrayList<>();` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 53 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 54 | `    @OneToOne(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)` | Relation JPA 1→1 : un objet est lié à au maximum un autre objet. |
| 55 | `    private SuiviSeance suiviSeance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `    /* ── Réservations des sportifs pour cette séance ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 58 | `    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)` | Relation JPA 1→N : cet objet parent possède une liste d’objets enfants. |
| 59 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `    private List<ReservationSeance> reservations = new ArrayList<>();` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 61 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SeanceEntrainement.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.