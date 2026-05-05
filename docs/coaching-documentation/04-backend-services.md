# 04 - Backend Services (Logique Métier)

## 🎯 Qu'est-ce qu'un Service ?

Un **Service** = la couche qui contient toute la **logique métier** (les règles du business).

### En mots simples :
- Le **Controller** = le réceptionniste (reçoit la demande)
- Le **Service** = le manager (décide quoi faire, vérifie les règles)
- Le **Repository** = l'archiviste (va chercher/stocker en BDD)

Le Service est le **cerveau** de l'application. C'est là que se trouvent les règles comme "on ne peut pas modifier un programme terminé".

---

## 1️⃣ ProgrammeEntrainementService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/ProgrammeEntrainementService.java`

### 🎯 But : Gérer les programmes d'entraînement (CRUD + règles métier)

```java
@Service                    // ① Dit à Spring : "je suis un service, gère-moi"
@RequiredArgsConstructor    // ② Lombok crée le constructeur avec les champs final
@Transactional              // ③ Chaque méthode est dans une transaction BDD
public class ProgrammeEntrainementService {

    private final ProgrammeEntrainementRepository programmeRepository;
    // ↑ Injecté automatiquement par Spring (injection de dépendances)
```

### 📋 Méthodes :

#### `create(Request dto)` → Response
```java
public ProgrammeEntrainementDTO.Response create(ProgrammeEntrainementDTO.Request dto) {
    validateDates(dto);                              // Vérifie que dateFin > dateDebut
    ProgrammeEntrainement programme = ProgrammeMapper.toEntity(dto);  // DTO → Entity
    return ProgrammeMapper.toResponse(programmeRepository.save(programme)); // Sauvegarde + retour
}
```
- **Paramètre** : Le DTO avec titre, description, dates, statut
- **Retour** : Le programme créé avec son ID généré
- **Règle** : La date de fin doit être après la date de début

#### `getAll()` → List<Response>
```java
@Transactional(readOnly = true)  // Optimisation : pas de verrou d'écriture
public List<ProgrammeEntrainementDTO.Response> getAll() {
    return programmeRepository.findAll().stream().map(ProgrammeMapper::toResponse).toList();
}
```
- **Retour** : Tous les programmes avec leurs séances et affectations

#### `getById(Integer id)` → Response
```java
@Transactional(readOnly = true)
public ProgrammeEntrainementDTO.Response getById(Integer id) {
    return ProgrammeMapper.toResponse(findOrThrow(id));
}
```
- **Paramètre** : L'ID du programme
- **Retour** : Le programme trouvé
- **Erreur** : ResourceNotFoundException si l'ID n'existe pas

#### `getByStatut(StatutProgramme statut)` → List<Response>
- **Paramètre** : BROUILLON, ACTIF, ou TERMINE
- **Retour** : Tous les programmes avec ce statut

#### `update(Integer id, Request dto)` → Response
```java
public ProgrammeEntrainementDTO.Response update(Integer id, ProgrammeEntrainementDTO.Request dto) {
    ProgrammeEntrainement programme = findOrThrow(id);

    // RÈGLE : impossible de modifier un programme TERMINE
    if (programme.getStatut() == StatutProgramme.TERMINE) {
        throw new BusinessRuleException("Impossible de modifier un programme terminé");
    }

    validateDates(dto);
    programme.setTitre(dto.getTitre());
    programme.setDescription(dto.getDescription());
    programme.setDateDebut(dto.getDateDebut());
    programme.setDateFin(dto.getDateFin());

    if (dto.getStatut() != null) {
        // RÈGLE : pour passer à TERMINE, toutes les séances doivent être REALISEE ou ANNULEE
        if (dto.getStatut() == StatutProgramme.TERMINE) {
            boolean hasSeancesPrevues = programme.getSeances().stream()
                    .anyMatch(s -> s.getStatut() == StatutSeance.PREVUE);
            if (hasSeancesPrevues) {
                throw new BusinessRuleException(
                    "Impossible de terminer : certaines séances sont encore PREVUE");
            }
        }
        programme.setStatut(dto.getStatut());
    }
    return ProgrammeMapper.toResponse(programmeRepository.save(programme));
}
```

#### `delete(Integer id)` → void
- Vérifie que le programme existe, puis le supprime (cascade supprime les séances)

#### `findOrThrow(Integer id)` → ProgrammeEntrainement
- Méthode utilitaire : trouve l'entity ou lance une exception

---

## 2️⃣ SeanceEntrainementService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/SeanceEntrainementService.java`

### 🎯 But : Gérer les séances d'entraînement

### 📋 Méthodes :

#### `create(Request dto)` → Response
```java
public SeanceEntrainementDTO.Response create(SeanceEntrainementDTO.Request dto) {
    ProgrammeEntrainement programme = programmeService.findOrThrow(dto.getProgrammeId());

    // RÈGLE : impossible de créer une séance si programme TERMINE
    if (programme.getStatut() == StatutProgramme.TERMINE) {
        throw new BusinessRuleException("Impossible de créer une séance pour un programme terminé");
    }

    // RÈGLE : la date doit être dans l'intervalle du programme
    validateDateInProgrammeRange(dto.getDateSeance(), programme);

    SeanceEntrainement seance = SeanceMapper.toEntity(dto);
    seance.setProgramme(programme);
    return SeanceMapper.toResponse(seanceRepository.save(seance));
}
```

#### `update(Integer id, Request dto)` → Response
Règles vérifiées :
1. Impossible de modifier une séance REALISEE
2. La date doit rester dans l'intervalle du programme
3. Pour passer à REALISEE, il faut au moins 1 exercice

#### `getAll()`, `getById(id)`, `getByProgramme(programmeId)`, `delete(id)`
- CRUD standard avec vérification d'existence

#### `validateDateInProgrammeRange(date, programme)` (privée)
```java
private void validateDateInProgrammeRange(LocalDate dateSeance, ProgrammeEntrainement programme) {
    if (dateSeance == null || programme.getDateDebut() == null || programme.getDateFin() == null) {
        return;  // Si des dates manquent, on ne valide pas
    }
    if (dateSeance.isBefore(programme.getDateDebut()) || dateSeance.isAfter(programme.getDateFin())) {
        throw new BusinessRuleException("La date de la séance doit être comprise entre...");
    }
}
```

---

## 3️⃣ ExerciceService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/ExerciceService.java`

### 🎯 But : Gérer le catalogue d'exercices

### 📋 Méthodes :

| Méthode | Paramètres | Retour | Description |
|---------|-----------|--------|-------------|
| `create(dto)` | ExerciceDTO.Request | Response | Crée un exercice |
| `getAll()` | - | List<Response> | Tous les exercices |
| `getById(id)` | Integer | Response | Un exercice par ID |
| `getByType(type)` | TypeExercice | List<Response> | Exercices par type |
| `update(id, dto)` | Integer, Request | Response | Modifie un exercice |
| `delete(id)` | Integer | void | Supprime un exercice |

Ce service est simple : pas de règles métier complexes, juste du CRUD.

---

## 4️⃣ SeanceExerciceService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/SeanceExerciceService.java`

### 🎯 But : Gérer les liens entre séances et exercices (avec paramètres)

### 📋 Méthodes :

#### `create(Request dto)` → Response
```java
public SeanceExerciceDTO.Response create(SeanceExerciceDTO.Request dto) {
    SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());
    Exercice exercice = exerciceRepository.findById(dto.getExerciceId())
            .orElseThrow(() -> new ResourceNotFoundException("Exercice non trouvé"));

    validateVolume(dto);  // RÈGLE : doit avoir répétitions OU temps

    SeanceExercice se = SeanceExercice.builder()
            .seance(seance)
            .exercice(exercice)
            .series(dto.getSeries())
            .repetitions(dto.getRepetitions())
            .charge(dto.getCharge())
            .tempsSecondes(dto.getTempsSecondes())
            .ordre(dto.getOrdre())
            .build();
    return ExerciceMapper.toSeanceExerciceResponse(seanceExerciceRepository.save(se));
}
```

#### `validateVolume(dto)` (privée)
```java
private void validateVolume(SeanceExerciceDTO.Request dto) {
    boolean hasRepetitions = dto.getRepetitions() != null && dto.getRepetitions() > 0;
    boolean hasDuration = dto.getTempsSecondes() != null && dto.getTempsSecondes() > 0;
    if (!hasRepetitions && !hasDuration) {
        throw new BusinessRuleException(
            "Un exercice de séance doit avoir des répétitions ou un temps d'exécution");
    }
}
```

#### Autres méthodes :
- `getBySeance(seanceId)` : tous les exercices d'une séance (triés par ordre)
- `getByExercice(exerciceId)` : toutes les séances utilisant cet exercice
- `update(id, dto)` : modifie les paramètres (séries, reps, charge...)
- `delete(id)` : retire un exercice d'une séance

---

## 5️⃣ SuiviSeanceService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/SuiviSeanceService.java`

### 🎯 But : Gérer le feedback post-séance

#### `create(Request dto)` → Response
```java
public SuiviSeanceDTO.Response create(SuiviSeanceDTO.Request dto) {
    SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());

    // RÈGLE : suivi seulement si séance REALISEE
    if (seance.getStatut() != StatutSeance.REALISEE) {
        throw new BusinessRuleException("Le suivi ne peut être créé que pour une séance REALISEE");
    }

    // RÈGLE : un seul suivi par séance
    if (suiviRepository.findBySeanceIdSeance(dto.getSeanceId()).isPresent()) {
        throw new BusinessRuleException("Un suivi existe déjà pour cette séance");
    }

    SuiviSeance suivi = SuiviSeance.builder()
            .seance(seance)
            .dateValidation(LocalDateTime.now())  // Date automatique
            .ressenti(dto.getRessenti())
            .fatigue(dto.getFatigue())
            .commentaire(dto.getCommentaire())
            .build();
    return SuiviMapper.toResponse(suiviRepository.save(suivi));
}
```

---

## 6️⃣ CoachingRoleService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/CoachingRoleService.java`

### 🎯 But : Vérifier les droits d'accès (qui peut faire quoi)

```java
@Service
@RequiredArgsConstructor
public class CoachingRoleService {

    private final UserRepository userRepository;

    // Vérifie que l'utilisateur est COACH ou ADMIN
    public void requireCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("Accès refusé : seuls les COACH et ADMIN...");
        }
    }

    // Vérifie que l'utilisateur est SPORTIF, COACH ou ADMIN
    public void requireSportifOrCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.SPORTIF
                && user.getRole() != Role.COACH
                && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("Accès refusé...");
        }
    }

    public User findUserOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}
```

---

## 7️⃣ AIRecommendationService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/AIRecommendationService.java`

### 🎯 But : Communiquer avec le service Python IA

```java
@Service
@RequiredArgsConstructor
@Slf4j                          // Lombok : ajoute un logger
public class AIRecommendationService {

    private static final String AI_SERVICE_BASE_URL = "http://localhost:5000/api/ai";
    private final RestTemplate restTemplate;  // Client HTTP de Spring

    // Appelle le service Python pour obtenir des recommandations
    public Map<String, Object> getRecommendations(Map<String, Object> context) {
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    AI_SERVICE_BASE_URL + "/recommend",
                    context,        // Le corps de la requête (JSON)
                    Map.class       // Type de réponse attendu
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            return buildFallbackResponse("Réponse invalide du service AI");
        } catch (Exception e) {
            // Si le service Python est en panne → fallback
            log.warn("Service AI indisponible : {}", e.getMessage());
            return buildFallbackResponse("Service AI temporairement indisponible");
        }
    }

    // Vérifie si le service Python est disponible
    public boolean isAvailable() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    AI_SERVICE_BASE_URL + "/health", Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    // Réponse de secours si l'IA est indisponible
    private Map<String, Object> buildFallbackResponse(String message) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("status", "fallback");
        fallback.put("message", message);
        fallback.put("nbRecommandations", 0);
        fallback.put("recommandations", Collections.emptyList());
        return fallback;
    }
}
```

---

## 📚 @Transactional expliqué

### Qu'est-ce qu'une transaction ?
Une **transaction** = un groupe d'opérations qui doivent TOUTES réussir ou TOUTES échouer.

### Analogie :
Un virement bancaire : on débite le compte A ET on crédite le compte B. Si le crédit échoue, le débit est annulé.

```java
@Transactional              // Toutes les opérations dans cette méthode = 1 transaction
@Transactional(readOnly = true)  // Optimisation pour les lectures (pas de verrou d'écriture)
```

### Que se passe-t-il si une exception est lancée ?
→ **Rollback** : toutes les modifications sont annulées. La BDD revient à son état précédent.

---

## 🎓 Questions du professeur

**Q : Pourquoi le Service et pas le Controller contient la logique métier ?**
> R : Séparation des responsabilités. Le Controller gère HTTP (requêtes/réponses). Le Service gère le métier (règles, validations). Si on change de protocole (ex: WebSocket), on garde le même Service.

**Q : Qu'est-ce que @RequiredArgsConstructor fait ?**
> R : Lombok génère un constructeur avec tous les champs `final`. Spring utilise ce constructeur pour injecter automatiquement les dépendances (injection par constructeur).

**Q : Pourquoi utiliser un fallback dans AIRecommendationService ?**
> R : Pour la résilience. Si le service Python tombe en panne, l'application continue de fonctionner. L'utilisateur voit un message "IA indisponible" au lieu d'une erreur 500.

**Q : Qu'est-ce que @Transactional(readOnly = true) ?**
> R : C'est une optimisation. On dit à la BDD "je ne vais que lire, pas écrire". La BDD peut alors éviter de poser des verrous d'écriture, ce qui améliore les performances.

**Q : Pourquoi validateVolume vérifie répétitions OU temps ?**
> R : Certains exercices se mesurent en répétitions (pompes : 3x12) et d'autres en temps (gainage : 3x60s). Il faut au moins un des deux pour que l'exercice ait du sens.
