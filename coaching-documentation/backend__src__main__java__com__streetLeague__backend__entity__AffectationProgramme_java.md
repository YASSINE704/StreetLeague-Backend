# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/entity/AffectationProgramme.java`

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
| 3 | `import com.streetLeague.backend.enums.TypeAffectationProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import jakarta.persistence.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import lombok.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 7 | `import java.time.LocalDateTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `@Entity` | Déclare une entité JPA : Hibernate va créer/mapper une table pour cette classe. |
| 10 | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 11 | `public class AffectationProgramme {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `    @Id` | Indique que ce champ est la clé primaire de la table. |
| 14 | `    @GeneratedValue(strategy = GenerationType.IDENTITY)` | Indique que la valeur de l’identifiant est générée automatiquement par la base/Hibernate. |
| 15 | `    private Integer idAffectation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `    @Enumerated(EnumType.STRING)` | Indique que l’enum est stocké en base sous forme de texte lisible. |
| 18 | `    private TypeAffectationProgramme type;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `    private LocalDateTime dateAffectation;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 23 | `    @JoinColumn(name = "user_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 24 | `    private User user;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 26 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 27 | `    @JoinColumn(name = "programme_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 28 | `    private ProgrammeEntrainement programme;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 29 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `AffectationProgramme.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.