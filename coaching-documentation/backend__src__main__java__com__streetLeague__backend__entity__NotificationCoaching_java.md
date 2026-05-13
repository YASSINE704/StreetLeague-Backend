# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/entity/NotificationCoaching.java`

## 1. Rôle du fichier

Entité JPA : représente une table ou un objet métier persistant.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Participe aux fonctionnalités météo, vêtements recommandés et notifications/rappels. 
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
| 6 | `import java.time.LocalDateTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 8 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 9 | ` * Step 5 : Historique des notifications envoyées pour le module coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 10 | ` * Permet de ne pas envoyer deux fois la même notification.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 11 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | `@Entity` | Déclare une entité JPA : Hibernate va créer/mapper une table pour cette classe. |
| 13 | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `public class NotificationCoaching {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 15 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 16 | `    @Id` | Indique que ce champ est la clé primaire de la table. |
| 17 | `    @GeneratedValue(strategy = GenerationType.IDENTITY)` | Indique que la valeur de l’identifiant est générée automatiquement par la base/Hibernate. |
| 18 | `    private Integer idNotification;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `    private String type;          // RAPPEL_SEANCE, ALERTE_METEO` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `    private String message;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `    private String destinataireEmail;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `    private LocalDateTime dateEnvoi;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `    private Boolean envoyee;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 26 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 27 | `    @JoinColumn(name = "seance_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 28 | `    private SeanceEntrainement seance;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 29 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 30 | `    @ManyToOne` | Relation JPA N→1 : plusieurs objets de cette classe peuvent être liés à un même parent. |
| 31 | `    @JoinColumn(name = "user_id")` | Définit la colonne de clé étrangère utilisée pour relier deux tables. |
| 32 | `    private User user;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 33 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `NotificationCoaching.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.