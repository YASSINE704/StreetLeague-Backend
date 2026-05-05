# 03 - Backend DTOs & Mappers

## 🎯 Qu'est-ce qu'un DTO ?

**DTO** = Data Transfer Object = Objet de Transfert de Données

### En mots simples :
- Une **Entity** = ce qu'on stocke en base de données (toutes les infos, même sensibles)
- Un **DTO** = ce qu'on envoie/reçoit via l'API (seulement les infos nécessaires)

### Analogie :
- L'**Entity** = votre dossier médical complet (tout est dedans)
- Le **DTO** = le certificat médical que vous donnez à votre employeur (seulement ce qui est pertinent)

### Pourquoi séparer ?
1. **Sécurité** : On ne veut pas exposer le mot de passe hashé de l'utilisateur
2. **Validation** : On peut valider les données entrantes (taille min/max, champs obligatoires)
3. **Flexibilité** : L'API peut évoluer sans changer la BDD
4. **Séparation** : Le frontend ne connaît pas la structure interne de la BDD

---

## 📦 Request DTO vs Response DTO

| Type | Direction | Rôle | Exemple |
|------|-----------|------|---------|
| **Request** | Frontend → Backend | Données envoyées par l'utilisateur | Formulaire de création |
| **Response** | Backend → Frontend | Données renvoyées à l'utilisateur | Résultat affiché |

```
Frontend  ──── Request DTO ────→  Backend (Controller)
Frontend  ←─── Response DTO ────  Backend (Controller)
```

---

## 1️⃣ ProgrammeEntrainementDTO

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/dto/ProgrammeEntrainementDTO.java`

```java
public class ProgrammeEntrainementDTO {

    // ═══════════════════════════════════════════
    // REQUEST : ce que le frontend ENVOIE
    // ═══════════════════════════════════════════
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotBlank(message = "Le titre est obligatoire")
        // → Le titre ne peut pas être vide ou que des espaces
        @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
        // → Minimum 3 caractères, maximum 100
        private String titre;

        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        // → Maximum 500 caractères (optionnel car pas de @NotBlank)
        private String description;

        @NotNull(message = "La date de début est obligatoire")
        // → Ce champ doit être rempli (pas null)
        private LocalDate dateDebut;

        @NotNull(message = "La date de fin est obligatoire")
        private LocalDate dateFin;

        private StatutProgramme statut;
        // → Pas de validation : si absent, le service met BROUILLON par défaut
    }

    // ═══════════════════════════════════════════
    // RESPONSE : ce que le backend RENVOIE
    // ═══════════════════════════════════════════
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idProgramme;       // L'ID généré par la BDD
        private String titre;
        private String description;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private StatutProgramme statut;
        private List<SeanceEntrainementDTO.Response> seances;       // Les séances incluses
        private List<AffectationProgrammeDTO.Response> affectations; // Les personnes assignées
    }
}
```

### Différences Request vs Response :
- **Request** : pas d'ID (la BDD le génère), pas de listes (on crée d'abord le programme, puis on ajoute les séances)
- **Response** : contient l'ID, contient les séances et affectations liées

---

## 2️⃣ SeanceEntrainementDTO

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/dto/SeanceEntrainementDTO.java`

```java
public class SeanceEntrainementDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Le titre de la séance est obligatoire")
        @Size(min = 3, max = 100)
        private String titreSeance;

        @NotNull(message = "La date de la séance est obligatoire")
        private LocalDate dateSeance;

        @Min(value = 1, message = "La durée doit être d'au moins 1 minute")
        @Max(value = 300, message = "La durée ne peut pas dépasser 300 minutes")
        private Integer dureeMinutes;
        // → Entre 1 et 300 minutes (5 heures max)

        private Intensite intensite;    // FAIBLE, MOYENNE, FORTE
        private StatutSeance statut;    // PREVUE, REALISEE, ANNULEE

        @NotNull(message = "Le programme est obligatoire")
        private Integer programmeId;    // À quel programme cette séance appartient
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idSeance;
        private String titreSeance;
        private LocalDate dateSeance;
        private Integer dureeMinutes;
        private Intensite intensite;
        private StatutSeance statut;
        private Integer programmeId;
        private String programmeTitre;   // ← Ajouté pour l'affichage (pas dans l'entity)
        private List<SeanceExerciceDTO.Response> exercices;
        private SuiviSeanceDTO.Response suiviSeance;
    }
}
```

---

## 3️⃣ ExerciceDTO

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/dto/ExerciceDTO.java`

```java
public class ExerciceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Le nom de l'exercice est obligatoire")
        @Size(min = 2, max = 100)
        private String nom;

        @Size(max = 500)
        private String description;

        @NotNull(message = "Le type d'exercice est obligatoire")
        private TypeExercice type;    // FORCE, CARDIO, MOBILITE, TECHNIQUE

        @Size(max = 500)
        private String videoUrl;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idExercice;
        private String nom;
        private String description;
        private TypeExercice type;
        private String videoUrl;
    }
}
```

---

## 4️⃣ SeanceExerciceDTO

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/dto/SeanceExerciceDTO.java`

```java
public class SeanceExerciceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull(message = "L'identifiant de la séance est obligatoire")
        private Integer seanceId;

        @NotNull(message = "L'identifiant de l'exercice est obligatoire")
        private Integer exerciceId;

        @Min(value = 1, message = "Le nombre de séries doit être au moins 1")
        private Integer series;

        @Min(value = 1, message = "Le nombre de répétitions doit être au moins 1")
        private Integer repetitions;

        @Min(value = 0, message = "La charge ne peut pas être négative")
        private Float charge;

        @Min(value = 1, message = "Le temps doit être au moins 1 seconde")
        private Integer tempsSecondes;

        @Min(value = 1, message = "L'ordre doit être au moins 1")
        private Integer ordre;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idSeanceExercice;
        private Integer seanceId;
        private String seanceTitre;     // ← Nom de la séance (pour affichage)
        private Integer exerciceId;
        private String exerciceNom;     // ← Nom de l'exercice (pour affichage)
        private Integer series;
        private Integer repetitions;
        private Float charge;
        private Integer tempsSecondes;
        private Integer ordre;
    }
}
```

---

## 5️⃣ SuiviSeanceDTO

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/dto/SuiviSeanceDTO.java`

```java
public class SuiviSeanceDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull(message = "L'identifiant de la séance est obligatoire")
        private Integer seanceId;

        @NotNull(message = "Le ressenti est obligatoire")
        @Min(value = 1, message = "Le ressenti doit être entre 1 et 10")
        @Max(value = 10, message = "Le ressenti doit être entre 1 et 10")
        private Integer ressenti;

        @NotNull(message = "La fatigue est obligatoire")
        @Min(value = 1, message = "La fatigue doit être entre 1 et 10")
        @Max(value = 10, message = "La fatigue doit être entre 1 et 10")
        private Integer fatigue;

        @Size(max = 500)
        private String commentaire;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idSuivi;
        private LocalDateTime dateValidation;
        private Integer ressenti;
        private Integer fatigue;
        private String commentaire;
        private Integer seanceId;
    }
}
```

---

## 6️⃣ AffectationProgrammeDTO

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/dto/AffectationProgrammeDTO.java`

```java
public class AffectationProgrammeDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotNull
        @Positive(message = "L'identifiant du programme doit être positif")
        private Integer programmeId;

        @NotNull
        @Positive(message = "L'identifiant de l'utilisateur doit être positif")
        private Integer userId;

        @NotNull(message = "Le type d'affectation est obligatoire")
        private TypeAffectationProgramme type;  // COACH ou SPORTIF
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Integer idAffectation;
        private TypeAffectationProgramme type;
        private LocalDateTime dateAffectation;
        private Integer userId;
        private String userNom;        // ← Nom complet pour affichage
        private Integer programmeId;
    }
}
```

---

## 🔄 Les Mappers (Convertisseurs)

### Qu'est-ce qu'un Mapper ?
Un **Mapper** convertit un objet d'un type vers un autre type.

```
Request DTO  ──── Mapper.toEntity() ────→  Entity (pour sauvegarder en BDD)
Entity       ──── Mapper.toResponse() ──→  Response DTO (pour renvoyer au frontend)
```

### ProgrammeMapper

📍 `backend/src/main/java/com/streetLeague/backend/mapper/ProgrammeMapper.java`

```java
public class ProgrammeMapper {

    private ProgrammeMapper() {}  // Constructeur privé → pas d'instanciation

    // Convertit un Request DTO en Entity
    public static ProgrammeEntrainement toEntity(ProgrammeEntrainementDTO.Request dto) {
        return ProgrammeEntrainement.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .statut(dto.getStatut() != null ? dto.getStatut() : StatutProgramme.BROUILLON)
                // ↑ Si pas de statut fourni, on met BROUILLON par défaut
                .build();
    }

    // Convertit une Entity en Response DTO
    public static ProgrammeEntrainementDTO.Response toResponse(ProgrammeEntrainement entity) {
        // Convertit aussi les séances et affectations liées
        List<SeanceEntrainementDTO.Response> seances = entity.getSeances() != null
                ? entity.getSeances().stream().map(SeanceMapper::toResponse).toList()
                : Collections.emptyList();

        List<AffectationProgrammeDTO.Response> affectations = entity.getAffectations() != null
                ? entity.getAffectations().stream().map(AffectationMapper::toResponse).toList()
                : Collections.emptyList();

        return ProgrammeEntrainementDTO.Response.builder()
                .idProgramme(entity.getIdProgramme())
                .titre(entity.getTitre())
                .description(entity.getDescription())
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .statut(entity.getStatut())
                .seances(seances)
                .affectations(affectations)
                .build();
    }
}
```

### SeanceMapper

📍 `backend/src/main/java/com/streetLeague/backend/mapper/SeanceMapper.java`

```java
public class SeanceMapper {

    public static SeanceEntrainement toEntity(SeanceEntrainementDTO.Request dto) {
        return SeanceEntrainement.builder()
                .titreSeance(dto.getTitreSeance())
                .dateSeance(dto.getDateSeance())
                .dureeMinutes(dto.getDureeMinutes())
                .intensite(dto.getIntensite())
                .statut(dto.getStatut() != null ? dto.getStatut() : StatutSeance.PREVUE)
                .build();
    }

    public static SeanceEntrainementDTO.Response toResponse(SeanceEntrainement entity) {
        // Inclut les exercices et le suivi dans la réponse
        List<SeanceExerciceDTO.Response> exercices = entity.getSeanceExercices() != null
                ? entity.getSeanceExercices().stream()
                    .map(ExerciceMapper::toSeanceExerciceResponse).toList()
                : Collections.emptyList();

        SuiviSeanceDTO.Response suivi = entity.getSuiviSeance() != null
                ? SuiviMapper.toResponse(entity.getSuiviSeance())
                : null;

        return SeanceEntrainementDTO.Response.builder()
                .idSeance(entity.getIdSeance())
                .titreSeance(entity.getTitreSeance())
                .dateSeance(entity.getDateSeance())
                .dureeMinutes(entity.getDureeMinutes())
                .intensite(entity.getIntensite())
                .statut(entity.getStatut())
                .programmeId(entity.getProgramme().getIdProgramme())
                .programmeTitre(entity.getProgramme().getTitre())
                .exercices(exercices)
                .suiviSeance(suivi)
                .build();
    }
}
```

### ExerciceMapper

📍 `backend/src/main/java/com/streetLeague/backend/mapper/ExerciceMapper.java`

```java
public class ExerciceMapper {

    public static Exercice toEntity(ExerciceDTO.Request dto) {
        return Exercice.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .type(dto.getType())
                .videoUrl(dto.getVideoUrl())
                .build();
    }

    public static ExerciceDTO.Response toResponse(Exercice entity) {
        return ExerciceDTO.Response.builder()
                .idExercice(entity.getIdExercice())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .type(entity.getType())
                .videoUrl(entity.getVideoUrl())
                .build();
    }

    // Convertit un SeanceExercice (table de liaison) en DTO
    public static SeanceExerciceDTO.Response toSeanceExerciceResponse(SeanceExercice entity) {
        return SeanceExerciceDTO.Response.builder()
                .idSeanceExercice(entity.getIdSeanceExercice())
                .seanceId(entity.getSeance().getIdSeance())
                .seanceTitre(entity.getSeance().getTitreSeance())
                .exerciceId(entity.getExercice().getIdExercice())
                .exerciceNom(entity.getExercice().getNom())
                .series(entity.getSeries())
                .repetitions(entity.getRepetitions())
                .charge(entity.getCharge())
                .tempsSecondes(entity.getTempsSecondes())
                .ordre(entity.getOrdre())
                .build();
    }
}
```

### SuiviMapper & AffectationMapper

```java
// SuiviMapper - Convertit SuiviSeance → SuiviSeanceDTO.Response
public class SuiviMapper {
    public static SuiviSeanceDTO.Response toResponse(SuiviSeance entity) {
        return SuiviSeanceDTO.Response.builder()
                .idSuivi(entity.getIdSuivi())
                .dateValidation(entity.getDateValidation())
                .ressenti(entity.getRessenti())
                .fatigue(entity.getFatigue())
                .commentaire(entity.getCommentaire())
                .seanceId(entity.getSeance().getIdSeance())
                .build();
    }
}

// AffectationMapper - Convertit AffectationProgramme → AffectationProgrammeDTO.Response
public class AffectationMapper {
    public static AffectationProgrammeDTO.Response toResponse(AffectationProgramme entity) {
        return AffectationProgrammeDTO.Response.builder()
                .idAffectation(entity.getIdAffectation())
                .type(entity.getType())
                .dateAffectation(entity.getDateAffectation())
                .userId(entity.getUser().getIdUser())
                .userNom(entity.getUser().getNom() + " " + entity.getUser().getPrenom())
                .programmeId(entity.getProgramme().getIdProgramme())
                .build();
    }
}
```

---

## 📚 Annotations de validation résumées

| Annotation | Signification | Exemple |
|------------|---------------|---------|
| `@NotBlank` | Pas vide, pas null, pas que des espaces | Titre obligatoire |
| `@NotNull` | Pas null (mais peut être vide) | Date obligatoire |
| `@Size(min, max)` | Longueur entre min et max | Titre 3-100 chars |
| `@Min(value)` | Valeur minimum | Durée ≥ 1 |
| `@Max(value)` | Valeur maximum | Durée ≤ 300 |
| `@Positive` | Doit être > 0 | ID positif |

---

## 🎓 Questions du professeur

**Q : Pourquoi ne pas envoyer directement l'Entity au frontend ?**
> R : Pour la sécurité (ne pas exposer les mots de passe), la flexibilité (l'API peut changer sans modifier la BDD), et la validation (les annotations de validation sont sur le DTO, pas sur l'Entity).

**Q : Pourquoi les Mappers sont des classes avec des méthodes statiques ?**
> R : Parce qu'ils n'ont pas d'état (pas de variables d'instance). Ce sont de pures fonctions de conversion. Le constructeur privé empêche de les instancier inutilement.

**Q : Pourquoi le Response DTO contient `programmeTitre` alors que l'Entity n'a que `programme` ?**
> R : Pour éviter au frontend de faire une requête supplémentaire. Le Mapper "aplatit" la relation en extrayant le titre directement.

**Q : Que se passe-t-il si la validation échoue ?**
> R : Spring Boot renvoie automatiquement une erreur 400 (Bad Request) avec les messages d'erreur définis dans les annotations. Le frontend affiche ces messages à l'utilisateur.

**Q : Pourquoi @NotBlank et pas @NotNull pour le titre ?**
> R : @NotNull accepte une chaîne vide "". @NotBlank refuse null, "", et "   " (que des espaces). Pour un titre, on veut du vrai contenu.
