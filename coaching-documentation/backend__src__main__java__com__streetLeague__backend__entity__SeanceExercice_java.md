# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/entity/SeanceExercice.java`

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
| 3 | `import jakarta.persistence.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `@Entity` | Déclare une entité JPA : Hibernate va créer/mapper une table pour cette classe. |
| 7 | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `public class SeanceExercice {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `    @Id` | Indique que ce champ est la clé primaire de la table. |
| 11 | `    @GeneratedValue(strategy = GenerationType.IDENTITY)` | Indique que la valeur de l’identifiant est générée automatiquement par la base/Hibernate. |
| 12 | `    private Integer idSeanceExercice;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 13 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 14 | `    private Integer series;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 15 | `    private Integer repetitions;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 16 | `    private Float charge;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 17 | `    private Integer tempsSecondes;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 18 | `    private Integer ordre;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 21 | `    @JoinColumn(name = "seance_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 22 | `    private SeanceEntrainement seance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 25 | `    @JoinColumn(name = "exercice_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 26 | `    private Exercice exercice;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SeanceExercice.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.