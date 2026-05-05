# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/controller/SuiviSeanceController.java`

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
| 3 | `import com.streetLeague.backend.dto.SuiviSeanceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.service.CoachingRoleService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.service.SuiviSeanceService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import jakarta.validation.Valid;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import org.springframework.http.HttpStatus;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import org.springframework.http.ResponseEntity;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import org.springframework.web.bind.annotation.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 14 | `@RestController` | Déclare un controller REST Spring Boot : cette classe reçoit des requêtes HTTP et retourne du JSON. |
| 15 | `@RequestMapping("/api/suivis")` | Définit l’URL de base utilisée par tous les endpoints de ce controller. |
| 16 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `@CrossOrigin(origins = "*")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `public class SuiviSeanceController {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `    private final SuiviSeanceService suiviService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `    private final CoachingRoleService roleService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 23 | `    /* ── CREATE : SPORTIF, COACH ou ADMIN (le sportif donne son feedback) ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 24 | `    @PostMapping` | Déclare un endpoint HTTP POST : utilisé pour créer une nouvelle ressource. |
| 25 | `    public ResponseEntity<SuiviSeanceDTO.Response> create(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 26 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `            @Valid @RequestBody SuiviSeanceDTO.Request dto) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 28 | `        roleService.requireSportifOrCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 29 | `        return ResponseEntity.status(HttpStatus.CREATED).body(suiviService.create(dto, userId));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 30 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 31 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 32 | `    /* ── READ : tout utilisateur ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 33 | `    @GetMapping` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 34 | `    public ResponseEntity<List<SuiviSeanceDTO.Response>> getAll() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 35 | `        return ResponseEntity.ok(suiviService.getAll());` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 36 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `    @GetMapping("/{id}")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 39 | `    public ResponseEntity<SuiviSeanceDTO.Response> getById(@PathVariable Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 40 | `        return ResponseEntity.ok(suiviService.getById(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 41 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 42 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 43 | `    @GetMapping("/seance/{seanceId}")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 44 | `    public ResponseEntity<SuiviSeanceDTO.Response> getBySeance(@PathVariable Integer seanceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 45 | `        return ResponseEntity.ok(suiviService.getBySeance(seanceId));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 46 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 47 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 48 | `    /* ── UPDATE : SPORTIF, COACH ou ADMIN ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 49 | `    @PutMapping("/{id}")` | Déclare un endpoint de modification : utilisé pour mettre à jour une ressource existante. |
| 50 | `    public ResponseEntity<SuiviSeanceDTO.Response> update(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 51 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `            @PathVariable Integer id, @Valid @RequestBody SuiviSeanceDTO.Request dto) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 53 | `        roleService.requireSportifOrCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 54 | `        return ResponseEntity.ok(suiviService.update(id, dto));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 55 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `    /* ── DELETE : COACH ou ADMIN uniquement ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 58 | `    @DeleteMapping("/{id}")` | Déclare un endpoint HTTP DELETE : utilisé pour supprimer une ressource. |
| 59 | `    public ResponseEntity<Void> delete(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 60 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `            @PathVariable Integer id) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 62 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 63 | `        suiviService.delete(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 64 | `        return ResponseEntity.noContent().build();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 65 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 66 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SuiviSeanceController.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.