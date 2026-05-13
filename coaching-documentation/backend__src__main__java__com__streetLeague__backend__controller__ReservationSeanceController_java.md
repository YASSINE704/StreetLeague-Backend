# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/controller/ReservationSeanceController.java`

## 1. Rôle du fichier

Controller REST : reçoit les requêtes HTTP envoyées par Angular.

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
| 1 | `package com.streetLeague.backend.controller;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.dto.ReservationSeanceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.service.CoachingRoleService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.service.ReservationSeanceService;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import jakarta.validation.Valid;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import org.springframework.http.HttpStatus;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import org.springframework.http.ResponseEntity;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import org.springframework.web.bind.annotation.*;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `import java.util.Map;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 16 | ` * Controller pour la réservation de séances de coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 17 | ` * Un SPORTIF réserve sa place, un COACH confirme ou annule.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 18 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 19 | `@RestController` | Déclare un controller REST Spring Boot : cette classe reçoit des requêtes HTTP et retourne du JSON. |
| 20 | `@RequestMapping("/api/reservations-seances")` | Définit l’URL de base utilisée par tous les endpoints de ce controller. |
| 21 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `@CrossOrigin(origins = "*")` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `public class ReservationSeanceController {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    private final ReservationSeanceService reservationService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 26 | `    private final CoachingRoleService roleService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 27 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 28 | `    /* ── RÉSERVER : SPORTIF ou ADMIN ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 29 | `    @PostMapping` | Déclare un endpoint HTTP POST : utilisé pour créer une nouvelle ressource. |
| 30 | `    public ResponseEntity<ReservationSeanceDTO.Response> reserver(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 31 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `            @Valid @RequestBody ReservationSeanceDTO.Request dto) {` | Ligne liée à la réservation d’une séance par un sportif. |
| 33 | `        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.reserver(userId, dto));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 34 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 35 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 36 | `    /* ── ANNULER : le sportif ou le coach ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 37 | `    @PutMapping("/{id}/annuler")` | Déclare un endpoint de modification : utilisé pour mettre à jour une ressource existante. |
| 38 | `    public ResponseEntity<ReservationSeanceDTO.Response> annuler(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 39 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `            @PathVariable Integer id,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `            @RequestBody(required = false) Map<String, String> body) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 42 | `        roleService.requireSportifOrCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 43 | `        String motif = body != null ? body.get("motif") : null;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 44 | `        return ResponseEntity.ok(reservationService.annuler(id, motif));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 45 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 46 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 47 | `    /* ── CONFIRMER : COACH ou ADMIN ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 48 | `    @PutMapping("/{id}/confirmer")` | Déclare un endpoint de modification : utilisé pour mettre à jour une ressource existante. |
| 49 | `    public ResponseEntity<ReservationSeanceDTO.Response> confirmer(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 50 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `            @PathVariable Integer id) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 52 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 53 | `        return ResponseEntity.ok(reservationService.confirmer(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 54 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 55 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 56 | `    /* ── LIRE : réservations d'une séance ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 57 | `    @GetMapping("/seance/{seanceId}")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 58 | `    public ResponseEntity<List<ReservationSeanceDTO.Response>> getBySeance(@PathVariable Integer seanceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 59 | `        return ResponseEntity.ok(reservationService.getBySeance(seanceId));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 60 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 61 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 62 | `    /* ── LIRE : réservations d'un sportif ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 63 | `    @GetMapping("/user/{userId}")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 64 | `    public ResponseEntity<List<ReservationSeanceDTO.Response>> getByUser(@PathVariable Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 65 | `        return ResponseEntity.ok(reservationService.getByUser(userId));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 66 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 67 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 68 | `    /* ── LIRE : places restantes pour une séance ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 69 | `    @GetMapping("/seance/{seanceId}/places")` | Déclare un endpoint HTTP GET : utilisé pour lire/consulter des données. |
| 70 | `    public ResponseEntity<Map<String, Integer>> getPlacesRestantes(@PathVariable Integer seanceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 71 | `        int places = reservationService.getPlacesRestantes(seanceId);` | Ligne liée à la réservation d’une séance par un sportif. |
| 72 | `        return ResponseEntity.ok(Map.of("placesRestantes", places));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 73 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 74 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 75 | `    /* ── Step 6 : MARQUER COMME PAYÉ ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 76 | `    @PutMapping("/{id}/payer")` | Déclare un endpoint de modification : utilisé pour mettre à jour une ressource existante. |
| 77 | `    public ResponseEntity<ReservationSeanceDTO.Response> marquerPaye(` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 78 | `            @RequestHeader(value = "X-User-Id", required = false) Integer userId,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `            @PathVariable Integer id) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 80 | `        roleService.requireCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 81 | `        return ResponseEntity.ok(reservationService.marquerPaye(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 82 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 83 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ReservationSeanceController.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.