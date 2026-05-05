# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/entity/ProgrammeEntrainement.java`

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
| 3 | `import com.streetLeague.backend.enums.StatutProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import jakarta.persistence.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 7 | `import java.time.LocalDate;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import java.util.ArrayList;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `@Entity` | Déclare une entité JPA : Hibernate va créer/mapper une table pour cette classe. |
| 12 | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 13 | `public class ProgrammeEntrainement {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `    @Id` | Indique que ce champ est la clé primaire de la table. |
| 16 | `    @GeneratedValue(strategy = GenerationType.IDENTITY)` | Indique que la valeur de l’identifiant est générée automatiquement par la base/Hibernate. |
| 17 | `    private Integer idProgramme;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 18 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 19 | `    private String titre;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `    private String description;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `    private LocalDate dateDebut;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `    private LocalDate dateFin;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 25 | `    private StatutProgramme statut;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 26 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 27 | `    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)` | Relation JPA 1→N : cet objet parent possède une liste d’objets enfants. |
| 28 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `    private List<SeanceEntrainement> seances = new ArrayList<>();` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)` | Relation JPA 1→N : cet objet parent possède une liste d’objets enfants. |
| 32 | `    @Builder.Default` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `    private List<AffectationProgramme> affectations = new ArrayList<>();` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 34 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ProgrammeEntrainement.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.