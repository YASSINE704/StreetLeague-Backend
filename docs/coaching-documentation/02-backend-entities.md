# 02 - Backend Entities (Entités JPA)

## 🎯 Qu'est-ce qu'une entité ?

Une **entité** = une classe Java qui représente une **table** dans la base de données.

Chaque **instance** (objet) de cette classe = une **ligne** dans la table.
Chaque **attribut** (champ) de la classe = une **colonne** dans la table.

### En mots simples :
Si la base de données est un classeur Excel, alors :
- Une **entité** = un onglet (feuille)
- Un **objet** = une ligne
- Un **champ** = une colonne

---

## 📊 Diagramme des relations entre entités

```
┌──────────────────────┐
│        User          │
│  (Utilisateur)       │
└──────────┬───────────┘
           │ 1
           │
           │ *
┌──────────▼───────────┐         ┌─────────────────────────┐
│ AffectationProgramme │ * ──── 1│  ProgrammeEntrainement  │
│ (Qui fait quoi)      │         │  (Plan d'entraînement)  │
└──────────────────────┘         └────────────┬────────────┘
                                              │ 1
                                              │
                                              │ *
                                 ┌────────────▼────────────┐
                                 │   SeanceEntrainement     │
                                 │   (Une séance)           │
                                 └─────┬──────────────┬─────┘
                                       │ 1            │ 1
                                       │              │
                                       │ *            │ 0..1
                          ┌────────────▼──┐    ┌─────▼──────────┐
                          │ SeanceExercice │    │  SuiviSeance   │
                          │ (Lien séance- │    │  (Feedback)    │
                          │  exercice)    │    └────────────────┘
                          └────────┬──────┘
                                   │ *
                                   │
                                   │ 1
                          ┌────────▼──────┐
                          │   Exercice    │
                          │ (Un exercice) │
                          └───────────────┘
```

**Légende :** `1` = un seul, `*` = plusieurs, `0..1` = zéro ou un

---

## 1️⃣ ProgrammeEntrainement

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/ProgrammeEntrainement.java`

### 🎯 But : Représente un plan d'entraînement complet (ex: "Programme Force Janvier-Mars")

### 📝 Code avec explications :

```java
@Entity                          // ① Dit à Java : "cette classe = une table en BDD"
@Getter @Setter                  // ② Lombok génère automatiquement les getters/setters
@NoArgsConstructor               // ③ Crée un constructeur sans paramètres (obligatoire pour JPA)
@AllArgsConstructor              // ④ Crée un constructeur avec tous les paramètres
@Builder                         // ⑤ Permet de créer des objets avec le pattern Builder
public class ProgrammeEntrainement {

    @Id                          // ⑥ Ce champ est la clé primaire (identifiant unique)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ⑦ La BDD génère l'ID automatiquement
    private Integer idProgramme;

    private String titre;        // Nom du programme (ex: "Force explosive")
    private String description;  // Description détaillée
    private LocalDate dateDebut; // Date de début du programme
    private LocalDate dateFin;   // Date de fin du programme

    @Enumerated(EnumType.STRING) // ⑧ Stocke l'enum comme texte en BDD (pas comme nombre)
    private StatutProgramme statut;  // BROUILLON, ACTIF, ou TERMINE

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)
    // ⑨ Un programme a PLUSIEURS séances
    // mappedBy = "programme" → la relation est gérée par le champ "programme" dans SeanceEntrainement
    // cascade = ALL → si on supprime le programme, ses séances sont supprimées aussi
    // orphanRemoval = true → si on retire une séance de la liste, elle est supprimée de la BDD
    @Builder.Default
    private List<SeanceEntrainement> seances = new ArrayList<>();

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)
    // ⑩ Un programme a PLUSIEURS affectations (coach + sportifs assignés)
    @Builder.Default
    private List<AffectationProgramme> affectations = new ArrayList<>();
}
```

### 🔑 Champs expliqués :

| Champ | Type | Stocke quoi | Pourquoi |
|-------|------|-------------|----------|
| `idProgramme` | Integer | Identifiant unique | Pour retrouver ce programme précis |
| `titre` | String | Nom du programme | Pour l'afficher à l'utilisateur |
| `description` | String | Détails du programme | Pour expliquer l'objectif |
| `dateDebut` | LocalDate | Date de début | Pour savoir quand commencer |
| `dateFin` | LocalDate | Date de fin | Pour savoir quand ça se termine |
| `statut` | StatutProgramme | BROUILLON/ACTIF/TERMINE | Pour gérer le cycle de vie |
| `seances` | List | Liste des séances | Relation parent-enfant |
| `affectations` | List | Qui est assigné | Lier coach et sportifs |

### 📏 Règles métier :
- Un programme TERMINE ne peut plus être modifié
- La date de fin doit être après la date de début
- Pour passer à TERMINE, toutes les séances doivent être REALISEE ou ANNULEE

---

## 2️⃣ SeanceEntrainement

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/SeanceEntrainement.java`

### 🎯 But : Représente une séance d'entraînement (un jour précis d'exercices)

```java
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeanceEntrainement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeance;

    private String titreSeance;      // Ex: "Séance Force Haut du corps"
    private LocalDate dateSeance;    // Quand la séance a lieu
    private Integer dureeMinutes;    // Durée prévue (ex: 60 minutes)

    @Enumerated(EnumType.STRING)
    private Intensite intensite;     // FAIBLE, MOYENNE, ou FORTE

    @Enumerated(EnumType.STRING)
    private StatutSeance statut;     // PREVUE, REALISEE, ou ANNULEE

    @ManyToOne                       // ⑪ Plusieurs séances appartiennent à UN programme
    @JoinColumn(name = "programme_id")  // ⑫ Nom de la colonne FK en BDD
    private ProgrammeEntrainement programme;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SeanceExercice> seanceExercices = new ArrayList<>();
    // ⑬ Une séance contient PLUSIEURS exercices (via la table de liaison)

    @OneToOne(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    private SuiviSeance suiviSeance;
    // ⑭ Une séance a AU PLUS UN suivi (feedback après réalisation)
}
```

### 🔑 Champs expliqués :

| Champ | Type | Stocke quoi | Pourquoi |
|-------|------|-------------|----------|
| `idSeance` | Integer | ID unique | Identifier la séance |
| `titreSeance` | String | Nom de la séance | Affichage |
| `dateSeance` | LocalDate | Date prévue | Planification |
| `dureeMinutes` | Integer | Durée en minutes | Planification du temps |
| `intensite` | Intensite | FAIBLE/MOYENNE/FORTE | Adapter la difficulté |
| `statut` | StatutSeance | PREVUE/REALISEE/ANNULEE | Cycle de vie |
| `programme` | ProgrammeEntrainement | Le programme parent | Lien hiérarchique |
| `seanceExercices` | List | Exercices de la séance | Contenu de la séance |
| `suiviSeance` | SuiviSeance | Feedback post-séance | Suivi performance |

### 📏 Règles métier :
- La date doit être dans l'intervalle du programme parent
- Impossible de modifier une séance REALISEE
- Pour passer à REALISEE, il faut au moins 1 exercice

---

## 3️⃣ Exercice

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/Exercice.java`

### 🎯 But : Représente un exercice du catalogue (ex: "Pompes", "Squats")

```java
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExercice;

    private String nom;           // Ex: "Pompes explosives"
    private String description;   // Ex: "Pompes avec phase explosive..."

    @Enumerated(EnumType.STRING)
    private TypeExercice type;    // FORCE, CARDIO, MOBILITE, ou TECHNIQUE

    private String videoUrl;      // Lien vers une vidéo démonstrative
}
```

### 🔑 Champs expliqués :

| Champ | Type | Stocke quoi | Pourquoi |
|-------|------|-------------|----------|
| `idExercice` | Integer | ID unique | Identifier l'exercice |
| `nom` | String | Nom de l'exercice | Affichage |
| `description` | String | Comment le faire | Instructions |
| `type` | TypeExercice | Catégorie | Filtrage et IA |
| `videoUrl` | String | Lien vidéo | Support visuel |

### 📏 Règles métier :
- Un exercice est un élément du catalogue, réutilisable dans plusieurs séances
- Le type permet à l'IA de recommander des exercices pertinents

---

## 4️⃣ SeanceExercice

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/SeanceExercice.java`

### 🎯 But : Table de liaison entre une séance et un exercice, avec les paramètres (séries, répétitions, etc.)

### En mots simples :
C'est comme un "bon de commande" : il dit "dans CETTE séance, on fait CET exercice avec CES paramètres".

```java
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeanceExercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeanceExercice;

    private Integer series;        // Nombre de séries (ex: 3)
    private Integer repetitions;   // Nombre de répétitions par série (ex: 12)
    private Float charge;          // Poids en kg (ex: 20.5)
    private Integer tempsSecondes; // Durée en secondes (pour exercices chronométrés)
    private Integer ordre;         // Position dans la séance (1er, 2ème, 3ème...)

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;  // La séance concernée

    @ManyToOne
    @JoinColumn(name = "exercice_id")
    private Exercice exercice;          // L'exercice du catalogue
}
```

### 🔑 Champs expliqués :

| Champ | Type | Stocke quoi | Pourquoi |
|-------|------|-------------|----------|
| `idSeanceExercice` | Integer | ID unique | Identifier ce lien |
| `series` | Integer | Nb de séries | Paramètre d'entraînement |
| `repetitions` | Integer | Nb de reps | Volume de travail |
| `charge` | Float | Poids en kg | Intensité |
| `tempsSecondes` | Integer | Durée | Pour exercices chronométrés |
| `ordre` | Integer | Position | Ordre d'exécution |
| `seance` | SeanceEntrainement | Quelle séance | Lien vers la séance |
| `exercice` | Exercice | Quel exercice | Lien vers le catalogue |

### 📏 Règles métier :
- Doit avoir soit des répétitions, soit un temps (au moins un des deux)
- L'ordre détermine la séquence d'exécution

---

## 5️⃣ SuiviSeance

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/SuiviSeance.java`

### 🎯 But : Feedback du sportif après une séance (comment il s'est senti)

```java
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SuiviSeance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSuivi;

    private LocalDateTime dateValidation;  // Quand le suivi a été rempli
    private Integer ressenti;              // Note de 1 à 10 (bien-être)
    private Integer fatigue;               // Note de 1 à 10 (niveau de fatigue)
    private String commentaire;            // Commentaire libre

    @OneToOne
    @JoinColumn(name = "seance_id")
    private SeanceEntrainement seance;     // La séance concernée
}
```

### 📏 Règles métier :
- Le suivi ne peut être créé que si la séance est REALISEE
- Un seul suivi par séance (relation OneToOne)
- Ressenti et fatigue entre 1 et 10

---

## 6️⃣ AffectationProgramme

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/AffectationProgramme.java`

### 🎯 But : Lie un utilisateur (coach ou sportif) à un programme

```java
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AffectationProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAffectation;

    @Enumerated(EnumType.STRING)
    private TypeAffectationProgramme type;  // COACH ou SPORTIF

    private LocalDateTime dateAffectation;  // Quand l'affectation a été faite

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;                      // L'utilisateur affecté

    @ManyToOne
    @JoinColumn(name = "programme_id")
    private ProgrammeEntrainement programme; // Le programme concerné
}
```

### 📏 Règles métier :
- Un seul COACH par programme
- Un seul SPORTIF par programme (par type d'affectation)

---

## 7️⃣ User

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/entity/User.java`

### 🎯 But : Représente un utilisateur de l'application

```java
@Entity
@Table(name = "users")           // ⑮ Nom de la table en BDD (car "user" est un mot réservé SQL)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    private String nom;
    private String prenom;

    @Column(unique = true)       // ⑯ Pas deux utilisateurs avec le même email
    private String email;

    private String motDePasse;   // Stocké hashé (BCrypt)

    @Enumerated(EnumType.STRING)
    private Role role;           // ADMIN, COACH, SPORTIF, JOUEUR, TERRAIN_MANAGER

    @Builder.Default
    private Boolean emailVerified = true;

    private String emailVerificationCode;
    private LocalDateTime emailVerificationCodeExpiresAt;

    @Builder.Default
    private Integer failedLoginAttempts = 0;
    private LocalDateTime accountLockedUntil;

    private String resetPasswordCode;
    private LocalDateTime resetPasswordCodeExpiresAt;
}
```

---

## 📚 Annotations expliquées (résumé)

| Annotation | Signification | En mots simples |
|------------|---------------|-----------------|
| `@Entity` | Classe = table BDD | "Je suis une table" |
| `@Id` | Clé primaire | "Je suis l'identifiant unique" |
| `@GeneratedValue(IDENTITY)` | Auto-incrémenté | "La BDD me donne un numéro automatiquement" |
| `@ManyToOne` | Relation N→1 | "Plusieurs de moi → un de toi" |
| `@OneToMany` | Relation 1→N | "Un de moi → plusieurs de toi" |
| `@OneToOne` | Relation 1→1 | "Un de moi → un seul de toi" |
| `@JoinColumn` | Colonne FK | "Voici la colonne qui fait le lien" |
| `@Enumerated(STRING)` | Enum en texte | "Stocke le nom, pas le numéro" |
| `@Column(unique=true)` | Valeur unique | "Pas de doublons" |
| `@Table(name="...")` | Nom de table | "Ma table s'appelle..." |
| `@Builder` | Pattern Builder | "Permet de construire l'objet étape par étape" |
| `@Getter/@Setter` | Lombok | "Génère les méthodes get/set automatiquement" |

---

## 🎓 Questions du professeur

**Q : Pourquoi utiliser @Enumerated(EnumType.STRING) plutôt que ORDINAL ?**
> R : STRING stocke le texte "ACTIF" en BDD. ORDINAL stockerait le numéro (0, 1, 2). Si on ajoute une valeur à l'enum, les numéros changent et les données existantes deviennent incorrectes. STRING est plus sûr et lisible.

**Q : Qu'est-ce que cascade = CascadeType.ALL ?**
> R : Quand on fait une opération sur le parent (sauvegarder, supprimer), la même opération est appliquée aux enfants. Si on supprime un programme, toutes ses séances sont supprimées aussi.

**Q : Qu'est-ce que orphanRemoval = true ?**
> R : Si on retire un enfant de la liste du parent (sans supprimer le parent), l'enfant est quand même supprimé de la BDD. Un "orphelin" est un enfant sans parent.

**Q : Pourquoi SeanceExercice est une entité séparée et pas juste une relation ManyToMany ?**
> R : Parce qu'on a besoin de stocker des informations supplémentaires sur la relation (séries, répétitions, charge, ordre). Une simple relation ManyToMany ne permet pas d'ajouter des attributs.

**Q : Qu'est-ce que @Builder.Default ?**
> R : Quand on utilise le pattern Builder de Lombok, les valeurs par défaut des champs sont ignorées. @Builder.Default dit "utilise cette valeur par défaut même avec le Builder".

**Q : Pourquoi la table User s'appelle "users" avec @Table ?**
> R : "user" est un mot réservé en SQL (c'est une commande). On ne peut pas nommer une table "user", donc on utilise "users".
