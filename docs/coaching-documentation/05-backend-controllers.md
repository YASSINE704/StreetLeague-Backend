# 05 - Backend Controllers (Points d'entrée API)

## 🎯 Qu'est-ce qu'un Controller ?

Un **Controller** = le point d'entrée de l'API. Il reçoit les requêtes HTTP et renvoie les réponses.

### En mots simples :
Le Controller est comme un **réceptionniste** :
1. Il reçoit la demande du client (requête HTTP)
2. Il la transmet au bon service (logique métier)
3. Il renvoie la réponse au client

### Le Controller ne fait PAS de logique métier. Il fait :
- Recevoir la requête
- Valider le format (via @Valid)
- Appeler le service
- Renvoyer la réponse avec le bon code HTTP

---

## 📚 Annotations de base expliquées

```java
@RestController          // "Je suis un controller REST (je renvoie du JSON, pas du HTML)"
@RequestMapping("/api/programmes")  // "Toutes mes URLs commencent par /api/programmes"
@RequiredArgsConstructor // Lombok : injection des dépendances par constructeur
@CrossOrigin(origins = "*")  // Autorise les requêtes depuis n'importe quelle origine
```

### Annotations sur les méthodes :

| Annotation | Verbe HTTP | Action |
|------------|-----------|--------|
| `@GetMapping` | GET | Lire/Récupérer |
| `@PostMapping` | POST | Créer |
| `@PutMapping` | PUT | Modifier |
| `@DeleteMapping` | DELETE | Supprimer |

### Annotations sur les paramètres :

| Annotation | Signification | Exemple |
|------------|---------------|---------|
| `@RequestBody` | Le corps JSON de la requête | `@RequestBody ProgrammeDTO.Request dto` |
| `@PathVariable` | Variable dans l'URL | `/programmes/{id}` → `@PathVariable Integer id` |
| `@RequestHeader` | En-tête HTTP | `@RequestHeader("X-User-Id") Integer userId` |
| `@Valid` | Active la validation du DTO | Vérifie @NotBlank, @Size, etc. |

---

## 1️⃣ ProgrammeEntrainementController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/ProgrammeEntrainementController.java`
### 🔒 Rôles requis : COACH, ADMIN

```java
@RestController
@RequestMapping("/api/programmes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgrammeEntrainementController {

    private final ProgrammeEntrainementService programmeService;
```

### 📋 Endpoints :

| Méthode | URL | Description | Body | Réponse |
|---------|-----|-------------|------|---------|
| POST | `/api/programmes` | Créer un programme | Request DTO | 201 + Response |
| GET | `/api/programmes` | Lister tous | - | 200 + List |
| GET | `/api/programmes/{id}` | Obtenir par ID | - | 200 + Response |
| GET | `/api/programmes/statut/{statut}` | Filtrer par statut | - | 200 + List |
| PUT | `/api/programmes/{id}` | Modifier | Request DTO | 200 + Response |
| DELETE | `/api/programmes/{id}` | Supprimer | - | 204 (No Content) |

### Exemple détaillé - Création :
```java
@PostMapping
public ResponseEntity<ProgrammeEntrainementDTO.Response> create(
        @Valid @RequestBody ProgrammeEntrainementDTO.Request dto) {
    //  @Valid → vérifie les annotations de validation du DTO
    //  @RequestBody → parse le JSON du corps de la requête en objet Java
    return ResponseEntity.status(HttpStatus.CREATED)  // Code 201
            .body(programmeService.create(dto));
}
```

### Exemple - Requête HTTP :
```http
POST http://localhost:8080/api/programmes
Content-Type: application/json

{
  "titre": "Programme Force Q1",
  "description": "Renforcement musculaire",
  "dateDebut": "2025-01-01",
  "dateFin": "2025-03-31",
  "statut": "BROUILLON"
}
```

---

## 2️⃣ SeanceEntrainementController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/SeanceEntrainementController.java`
### 🔒 Rôles requis : COACH, ADMIN

### 📋 Endpoints :

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/seances` | Créer une séance |
| GET | `/api/seances` | Lister toutes les séances |
| GET | `/api/seances/{id}` | Obtenir une séance par ID |
| GET | `/api/seances/programme/{programmeId}` | Séances d'un programme |
| PUT | `/api/seances/{id}` | Modifier une séance |
| DELETE | `/api/seances/{id}` | Supprimer une séance |

### Exemple - Requête de création :
```http
POST http://localhost:8080/api/seances
Content-Type: application/json

{
  "titreSeance": "Séance Force Haut du corps",
  "dateSeance": "2025-01-15",
  "dureeMinutes": 60,
  "intensite": "MOYENNE",
  "statut": "PREVUE",
  "programmeId": 1
}
```

---

## 3️⃣ ExerciceController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/ExerciceController.java`
### 🔒 Rôles requis : COACH, ADMIN

### 📋 Endpoints :

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/exercices` | Créer un exercice |
| GET | `/api/exercices` | Lister tous les exercices |
| GET | `/api/exercices/{id}` | Obtenir par ID |
| GET | `/api/exercices/type/{type}` | Filtrer par type (FORCE, CARDIO...) |
| PUT | `/api/exercices/{id}` | Modifier |
| DELETE | `/api/exercices/{id}` | Supprimer |

### Exemple - Filtrer par type :
```http
GET http://localhost:8080/api/exercices/type/FORCE
```
Réponse : liste de tous les exercices de type FORCE.

---

## 4️⃣ SeanceExerciceController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/SeanceExerciceController.java`
### 🔒 Rôles requis : COACH, ADMIN

### 📋 Endpoints :

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/seance-exercices` | Ajouter un exercice à une séance |
| GET | `/api/seance-exercices` | Lister tous les liens |
| GET | `/api/seance-exercices/{id}` | Obtenir par ID |
| GET | `/api/seance-exercices/seance/{seanceId}` | Exercices d'une séance |
| GET | `/api/seance-exercices/exercice/{exerciceId}` | Séances utilisant un exercice |
| PUT | `/api/seance-exercices/{id}` | Modifier les paramètres |
| DELETE | `/api/seance-exercices/{id}` | Retirer un exercice d'une séance |

### Exemple - Ajouter un exercice à une séance :
```http
POST http://localhost:8080/api/seance-exercices
Content-Type: application/json

{
  "seanceId": 1,
  "exerciceId": 3,
  "series": 3,
  "repetitions": 12,
  "charge": 0,
  "tempsSecondes": null,
  "ordre": 1
}
```

---

## 5️⃣ SuiviSeanceController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/SuiviSeanceController.java`
### 🔒 Rôles requis : COACH, ADMIN

### 📋 Endpoints :

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/suivis` | Créer un suivi |
| GET | `/api/suivis` | Lister tous les suivis |
| GET | `/api/suivis/{id}` | Obtenir par ID |
| GET | `/api/suivis/seance/{seanceId}` | Suivi d'une séance |
| PUT | `/api/suivis/{id}` | Modifier un suivi |
| DELETE | `/api/suivis/{id}` | Supprimer un suivi |

### Exemple - Créer un suivi :
```http
POST http://localhost:8080/api/suivis
Content-Type: application/json

{
  "seanceId": 1,
  "ressenti": 8,
  "fatigue": 6,
  "commentaire": "Bonne séance, un peu fatigué à la fin"
}
```

---

## 6️⃣ AffectationProgrammeController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/AffectationProgrammeController.java`
### 🔒 Rôles requis : COACH, ADMIN

### 📋 Endpoints :

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/affectations` | Créer une affectation |
| GET | `/api/affectations` | Lister toutes |
| GET | `/api/affectations/{id}` | Obtenir par ID |
| GET | `/api/affectations/programme/{programmeId}` | Affectations d'un programme |
| GET | `/api/affectations/user/{userId}` | Programmes d'un utilisateur |
| PUT | `/api/affectations/{id}` | Modifier |
| DELETE | `/api/affectations/{id}` | Supprimer |

---

## 7️⃣ AIRecommendationController

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/controller/AIRecommendationController.java`
### 🔒 Rôles requis : COACH, ADMIN (via X-User-Id header)

```java
@RestController
@RequestMapping("/api/coaching/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIRecommendationController {

    private final AIRecommendationService aiRecommendationService;
    private final CoachingRoleService coachingRoleService;

    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> recommend(
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @RequestBody Map<String, Object> context) {

        if (userId != null) {
            coachingRoleService.requireCoachOrAdmin(userId);
            // ↑ Vérifie que l'utilisateur est COACH ou ADMIN
        }

        Map<String, Object> recommendations = aiRecommendationService.getRecommendations(context);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        boolean available = aiRecommendationService.isAvailable();
        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        response.put("message", available ? "Service AI opérationnel" : "Service AI indisponible");
        return ResponseEntity.ok(response);
    }
}
```

### 📋 Endpoints :

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/coaching/ai/recommend` | Obtenir des recommandations IA |
| GET | `/api/coaching/ai/health` | Vérifier si l'IA est disponible |

### Exemple - Demander des recommandations :
```http
POST http://localhost:8080/api/coaching/ai/recommend
Content-Type: application/json
X-User-Id: 1

{
  "objectifProgramme": "renforcement musculaire",
  "typeSeance": "FORCE",
  "intensite": "MOYENNE",
  "nbParticipants": 5,
  "dureeSeanceMinutes": 60,
  "niveauJoueurs": "Intermédiaire"
}
```

---

## 📊 Codes HTTP utilisés

| Code | Signification | Quand |
|------|---------------|-------|
| 200 | OK | Lecture ou modification réussie |
| 201 | Created | Création réussie |
| 204 | No Content | Suppression réussie |
| 400 | Bad Request | Validation échouée (@Valid) |
| 403 | Forbidden | Pas les droits (rôle insuffisant) |
| 404 | Not Found | Ressource inexistante |
| 500 | Internal Server Error | Erreur serveur inattendue |

---

## 🎓 Questions du professeur

**Q : Pourquoi @RestController et pas @Controller ?**
> R : @RestController = @Controller + @ResponseBody. Ça signifie que chaque méthode renvoie directement du JSON (pas une vue HTML). C'est le standard pour les API REST.

**Q : Que fait @Valid ?**
> R : @Valid active la validation des annotations Jakarta (ex: @NotBlank, @Size, @Min). Si la validation échoue, Spring renvoie automatiquement une erreur 400 avec les messages d'erreur.

**Q : Pourquoi ResponseEntity et pas juste retourner l'objet ?**
> R : ResponseEntity permet de contrôler le code HTTP de la réponse (201 pour création, 204 pour suppression). Sans ResponseEntity, c'est toujours 200.

**Q : Que fait @CrossOrigin(origins = "*") ?**
> R : Autorise les requêtes depuis n'importe quel domaine. Sans ça, le navigateur bloque les requêtes de localhost:4200 vers localhost:8080 (politique Same-Origin).

**Q : Pourquoi le header X-User-Id dans le controller IA ?**
> R : C'est un mécanisme simplifié pour identifier l'utilisateur. Le frontend envoie l'ID de l'utilisateur connecté dans un header HTTP. Le backend vérifie ensuite son rôle.

**Q : Quelle est la différence entre @PathVariable et @RequestParam ?**
> R : @PathVariable extrait une valeur de l'URL (`/programmes/5` → id=5). @RequestParam extrait un paramètre de requête (`/programmes?statut=ACTIF` → statut=ACTIF).
