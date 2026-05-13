# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/controller/ExerciceController.java`

## 1. Rôle du fichier

Controller REST : reçoit les requêtes HTTP envoyées par Angular.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.controller;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.dto.ExerciceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.enums.TypeExercice;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.service.CoachingRoleService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.service.ExerciceService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import jakarta.validation.Valid;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import org.springframework.http.HttpStatus;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import org.springframework.http.ResponseEntity;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import org.springframework.web.bind.annotation.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `@RestController` | Déclare un controller REST Spring Boot : cette classe reçoit des requêtes HTTP et retourne du JSON. |
| 16 | `@RequestMapping("/api/exercices")` | Définit l’URL de base utilisée par tous les endpoints de ce controller. |
| 17 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `@CrossOrigin(origins = "*")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `public class ExerciceController {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `    private final ExerciceService exerciceService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `    private final CoachingRoleService roleService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    /* ── CREATE : COACH ou ADMIN uniquement ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 25 | `    @PostMapping` | Déclare un endpoint HTTP POST : utilisé pour créer une nouvelle ressource. |
| 26 | `    public ResponseEntity<ExerciceDTO.Response> create(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `            @Valid @RequestBody ExerciceDTO.Request dto) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 29 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 30 | `        return ResponseEntity.status(HttpStatus.CREATED).body(exerciceService.create(dto));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 31 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 32 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 33 | `    /* ── READ : tout utilisateur ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 34 | `    @GetMapping` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 35 | `    public ResponseEntity<List<ExerciceDTO.Response>> getAll() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 36 | `        return ResponseEntity.ok(exerciceService.getAll());` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 37 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 38 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 39 | `    @GetMapping("/{id}")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 40 | `    public ResponseEntity<ExerciceDTO.Response> getById(@PathVariable Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 41 | `        return ResponseEntity.ok(exerciceService.getById(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 42 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 43 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 44 | `    @GetMapping("/type/{type}")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 45 | `    public ResponseEntity<List<ExerciceDTO.Response>> getByType(@PathVariable TypeExercice type) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 46 | `        return ResponseEntity.ok(exerciceService.getByType(type));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 47 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 48 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 49 | `    /* ── UPDATE : COACH ou ADMIN uniquement ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 50 | `    @PutMapping("/{id}")` | Déclare un endpoint de modification : utilisé pour mettre à jour une ressource existante. |
| 51 | `    public ResponseEntity<ExerciceDTO.Response> update(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 52 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `            @PathVariable Integer id, @Valid @RequestBody ExerciceDTO.Request dto) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 54 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 55 | `        return ResponseEntity.ok(exerciceService.update(id, dto));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 56 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 57 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 58 | `    /* ── DELETE : COACH ou ADMIN uniquement ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 59 | `    @DeleteMapping("/{id}")` | Déclare un endpoint HTTP DELETE : utilisé pour supprimer une ressource. |
| 60 | `    public ResponseEntity<Void> delete(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 61 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `            @PathVariable Integer id) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 63 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 64 | `        exerciceService.delete(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `        return ResponseEntity.noContent().build();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 66 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 67 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ExerciceController.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.