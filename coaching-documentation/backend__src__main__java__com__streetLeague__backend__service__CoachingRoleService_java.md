# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/CoachingRoleService.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Appelé par un controller et utilise souvent un repository pour appliquer les règles métier.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.service;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.entity.User;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.Role;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.exception.BusinessRuleException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.exception.ResourceNotFoundException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.repository.UserRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 12 | ` * Service utilitaire pour la vérification des rôles dans le module Coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | ` * Centralise la logique d'autorisation pour éviter la duplication dans chaque service.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | ` *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 15 | ` * Rôles coaching :` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 16 | ` * - ADMIN  : accès complet` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 17 | ` * - COACH  : créer/modifier/supprimer programmes, séances, exercices` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 18 | ` * - SPORTIF: consulter (GET), ajouter un suivi, rejoindre une séance` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 19 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 20 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 21 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `public class CoachingRoleService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    private final UserRepository userRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 26 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 27 | `     * Récupère un utilisateur par son ID ou lève une exception.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 28 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 29 | `    public User findUserOrThrow(Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `        if (userId == null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 31 | `            throw new BusinessRuleException("L'identifiant utilisateur est requis (header X-User-Id)");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 33 | `        return userRepository.findById(userId)` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 34 | `                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec id: " + userId));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 36 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 37 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 38 | `     * Vérifie que l'utilisateur a le rôle COACH ou ADMIN.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 39 | `     * Utilisé pour les opérations de création/modification/suppression de programmes, séances, exercices.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 40 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 41 | `    public User requireCoachOrAdmin(Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 42 | `        User user = findUserOrThrow(userId);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `        if (user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 44 | `            throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 45 | `                    "Accès refusé : seuls les coachs et administrateurs peuvent effectuer cette action. Votre rôle : " + user.getRole());` | Ligne liée aux rôles et aux permissions utilisateur. |
| 46 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 47 | `        return user;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 48 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 49 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 50 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 51 | `     * Vérifie que l'utilisateur a le rôle SPORTIF, COACH ou ADMIN.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 52 | `     * Utilisé pour les opérations de suivi (feedback après séance).` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 53 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 54 | `    public User requireSportifOrCoachOrAdmin(Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 55 | `        User user = findUserOrThrow(userId);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `        if (user.getRole() != Role.SPORTIF && user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 57 | `            throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `                    "Accès refusé : seuls les sportifs, coachs et administrateurs peuvent effectuer cette action. Votre rôle : " + user.getRole());` | Ligne liée aux rôles et aux permissions utilisateur. |
| 59 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 60 | `        return user;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 61 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 62 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 63 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 64 | `     * Vérifie que l'utilisateur est authentifié (n'importe quel rôle).` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 65 | `     * Utilisé pour les opérations de lecture (GET).` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 66 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 67 | `    public User requireAuthenticated(Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 68 | `        return findUserOrThrow(userId);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 69 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 70 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `CoachingRoleService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.