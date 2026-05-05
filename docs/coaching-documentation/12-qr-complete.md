# 12 - 100 Questions/Réponses pour le Professeur

## 🏗️ Architecture (20 questions)

### Q1 : Quelles technologies utilisez-vous ?
> Angular (TypeScript) pour le frontend, Spring Boot (Java) pour le backend, Flask (Python) pour le service IA. Communication en REST/JSON.

### Q2 : Pourquoi 3 services séparés ?
> Séparation des responsabilités. Chaque service fait une chose bien. Si l'IA tombe, le reste fonctionne. On utilise le meilleur langage pour chaque tâche.

### Q3 : Comment les services communiquent-ils ?
> Via des requêtes HTTP REST avec des données en format JSON. Angular appelle Spring Boot sur le port 8080, Spring Boot appelle Flask sur le port 5000.

### Q4 : Qu'est-ce qu'une API REST ?
> Un standard pour que deux programmes communiquent via HTTP. On utilise les verbes GET (lire), POST (créer), PUT (modifier), DELETE (supprimer) avec des URLs structurées.

### Q5 : Qu'est-ce qu'un microservice ?
> Un petit service indépendant qui fait une seule chose. Notre service IA est un microservice : il ne fait que des recommandations d'exercices.

### Q6 : Pourquoi JSON et pas XML ?
> JSON est plus léger, plus lisible, et natif en JavaScript. C'est le standard actuel pour les API web.

### Q7 : Qu'est-ce que le pattern MVC ?
> Model-View-Controller. Model = données (Entity), View = interface (Angular), Controller = point d'entrée API. Ça sépare les responsabilités.

### Q8 : Quelle est l'architecture en couches du backend ?
> Controller → Service → Repository → Base de données. Chaque couche a un rôle précis et ne connaît que la couche en dessous.

### Q9 : Pourquoi séparer Controller et Service ?
> Le Controller gère HTTP (requêtes/réponses). Le Service gère la logique métier (règles). Si on change de protocole, on garde le même Service.

### Q10 : Qu'est-ce que l'injection de dépendances ?
> Spring crée et fournit automatiquement les objets dont une classe a besoin. Au lieu de faire `new Service()`, Spring l'injecte via le constructeur.

### Q11 : Qu'est-ce que Spring Boot ?
> Un framework Java qui simplifie la création d'applications web. Il configure automatiquement beaucoup de choses (serveur, BDD, sécurité).

### Q12 : Qu'est-ce qu'Angular ?
> Un framework TypeScript pour créer des interfaces web dynamiques (Single Page Application). La page ne se recharge pas entièrement à chaque action.

### Q13 : Qu'est-ce que Flask ?
> Un micro-framework Python pour créer des API web. Très léger et simple, parfait pour un petit service avec peu de routes.

### Q14 : Pourquoi Python pour l'IA et pas Java ?
> Python est le langage de référence pour l'IA/ML. Il a les meilleures bibliothèques (scikit-learn, TensorFlow). Java est meilleur pour la logique métier et la robustesse.

### Q15 : Qu'est-ce qu'une SPA (Single Page Application) ?
> Une application web qui charge une seule page HTML et met à jour dynamiquement le contenu sans rechargement complet. Angular est un framework SPA.

### Q16 : Comment gérez-vous les erreurs entre services ?
> Avec des try/catch et des mécanismes de fallback. Si le service IA est indisponible, Spring Boot renvoie une réponse de secours au lieu d'une erreur 500.

### Q17 : Qu'est-ce que le port d'un service ?
> Un numéro qui identifie un programme sur une machine. Angular=4200, Spring Boot=8080, Flask=5000. Comme des numéros d'appartement dans un immeuble.

### Q18 : Qu'est-ce que localhost ?
> L'adresse de votre propre machine. localhost:8080 = "le programme qui tourne sur ma machine au port 8080".

### Q19 : Pourquoi utiliser des DTOs ?
> Pour séparer la structure interne (Entity/BDD) de la structure externe (API). Sécurité (pas d'exposition de données sensibles), validation, et flexibilité.

### Q20 : Qu'est-ce que le pattern Repository ?
> Une couche d'abstraction pour accéder à la base de données. Le Service ne sait pas comment les données sont stockées, il demande juste au Repository.

---

## ☕ Backend / Spring Boot (20 questions)

### Q21 : Qu'est-ce qu'une Entity JPA ?
> Une classe Java annotée @Entity qui représente une table en base de données. Chaque instance = une ligne, chaque attribut = une colonne.

### Q22 : Qu'est-ce que @Id et @GeneratedValue ?
> @Id marque la clé primaire. @GeneratedValue(IDENTITY) dit que la BDD génère automatiquement l'ID (auto-increment).

### Q23 : Quelle est la différence entre @ManyToOne et @OneToMany ?
> @ManyToOne = plusieurs de moi vers un de toi (ex: plusieurs séances → un programme). @OneToMany = un de moi vers plusieurs de toi (inverse).

### Q24 : Qu'est-ce que cascade = CascadeType.ALL ?
> Les opérations sur le parent se propagent aux enfants. Si on supprime un programme, ses séances sont supprimées aussi.

### Q25 : Qu'est-ce que orphanRemoval = true ?
> Si un enfant est retiré de la liste du parent, il est supprimé de la BDD. Un orphelin (sans parent) est automatiquement nettoyé.

### Q26 : Qu'est-ce que @Transactional ?
> Toutes les opérations dans la méthode forment une transaction. Si une erreur survient, tout est annulé (rollback). Garantit la cohérence des données.

### Q27 : Qu'est-ce que @Transactional(readOnly = true) ?
> Optimisation pour les lectures. Pas de verrou d'écriture en BDD, meilleures performances.

### Q28 : Comment fonctionne la validation avec @Valid ?
> @Valid active la vérification des annotations (@NotBlank, @Size, @Min...) sur le DTO. Si la validation échoue, Spring renvoie automatiquement une erreur 400.

### Q29 : Qu'est-ce que @NotBlank vs @NotNull ?
> @NotNull refuse null. @NotBlank refuse null, "", et "   " (espaces). Pour un titre, on veut @NotBlank car un titre vide n'a pas de sens.

### Q30 : Qu'est-ce que ResponseEntity ?
> Un objet Spring qui encapsule la réponse HTTP : le corps (JSON), le code de statut (200, 201, 404...), et les headers.

### Q31 : Qu'est-ce que @RestController ?
> @Controller + @ResponseBody. Chaque méthode renvoie directement du JSON (pas une page HTML). Standard pour les API REST.

### Q32 : Qu'est-ce que @RequestMapping ?
> Définit le préfixe d'URL pour toutes les méthodes du controller. Ex: @RequestMapping("/api/programmes") → toutes les URLs commencent par /api/programmes.

### Q33 : Qu'est-ce que @PathVariable ?
> Extrait une valeur de l'URL. Ex: GET /api/programmes/5 → @PathVariable Integer id = 5.

### Q34 : Qu'est-ce que @RequestBody ?
> Parse le corps JSON de la requête en objet Java. Le JSON envoyé par Angular est automatiquement converti en DTO.

### Q35 : Qu'est-ce que Lombok ?
> Une bibliothèque qui génère du code répétitif : @Getter/@Setter (accesseurs), @Builder (construction), @RequiredArgsConstructor (constructeur).

### Q36 : Qu'est-ce que le pattern Builder ?
> Un pattern de création d'objets étape par étape : `Programme.builder().titre("X").dateDebut(...).build()`. Plus lisible que les constructeurs avec beaucoup de paramètres.

### Q37 : Qu'est-ce que JpaRepository ?
> Une interface Spring Data qui fournit automatiquement les méthodes CRUD (save, findById, findAll, deleteById) sans écrire de code SQL.

### Q38 : Comment fonctionnent les méthodes dérivées (findByStatut) ?
> Spring Data génère automatiquement la requête SQL à partir du nom de la méthode. `findByStatut(ACTIF)` → `SELECT * FROM programme WHERE statut = 'ACTIF'`.

### Q39 : Qu'est-ce qu'une BusinessRuleException ?
> Une exception personnalisée lancée quand une règle métier est violée. Ex: "Impossible de modifier un programme terminé". Renvoie un code 400 ou 403.

### Q40 : Qu'est-ce que RestTemplate ?
> Un client HTTP de Spring pour appeler d'autres services. Spring Boot l'utilise pour envoyer des requêtes au service Python IA.

---

## 🅰️ Frontend / Angular (15 questions)

### Q41 : Qu'est-ce qu'un Component Angular ?
> Un morceau d'interface réutilisable avec 3 fichiers : .ts (logique), .html (template), .css (style). Ex: SeanceDetailsComponent.

### Q42 : Qu'est-ce qu'un Module Angular ?
> Un conteneur qui regroupe des composants, services et routes liés. Le CoachingModule regroupe tout ce qui concerne le coaching.

### Q43 : Qu'est-ce que le Lazy Loading ?
> Charger un module seulement quand l'utilisateur navigue vers sa route. Le module coaching n'est téléchargé que quand on va sur /coaching. Améliore le temps de chargement initial.

### Q44 : Qu'est-ce que [(ngModel)] ?
> Two-way binding (liaison bidirectionnelle). La variable TypeScript et le champ HTML sont synchronisés dans les deux sens.

### Q45 : Qu'est-ce que *ngIf ?
> Directive structurelle qui affiche ou cache un élément selon une condition. `*ngIf="showAiPanel"` → l'élément n'existe que si showAiPanel est true.

### Q46 : Qu'est-ce que *ngFor ?
> Directive structurelle qui répète un élément pour chaque item d'un tableau. `*ngFor="let ex of exercices"` → crée un élément par exercice.

### Q47 : Qu'est-ce qu'un Observable ?
> Un flux de données asynchrone. Quand on fait un appel HTTP, on reçoit un Observable. On s'y abonne avec `.subscribe()` pour recevoir la réponse.

### Q48 : Qu'est-ce que HttpClient ?
> Le service Angular pour faire des requêtes HTTP. Équivalent de RestTemplate en Java. Retourne des Observables.

### Q49 : Qu'est-ce qu'un Interceptor ?
> Un middleware qui intercepte TOUTES les requêtes HTTP avant qu'elles partent. Utilisé pour ajouter automatiquement le header X-User-Id.

### Q50 : Qu'est-ce que [ngClass] ?
> Directive qui applique des classes CSS conditionnellement. Ex: badge vert si REALISEE, bleu si PREVUE, rouge si ANNULEE.

### Q51 : Qu'est-ce que (click) ?
> Event binding. Lie un événement DOM (clic) à une méthode du composant. `(click)="generateAIExercises()"` → appelle la méthode au clic.

### Q52 : Qu'est-ce que {{ }} (interpolation) ?
> Affiche la valeur d'une variable dans le HTML. `{{ seance.titreSeance }}` → affiche le titre de la séance.

### Q53 : Qu'est-ce que @Injectable({ providedIn: 'root' }) ?
> Déclare un service comme Singleton (une seule instance pour toute l'app). Tous les composants partagent la même instance.

### Q54 : Pourquoi FormsModule et pas ReactiveFormsModule ?
> FormsModule = formulaires template-driven (simples, avec ngModel). ReactiveFormsModule = formulaires réactifs (plus puissants mais plus complexes). Template-driven suffit ici.

### Q55 : Comment Angular gère-t-il les routes ?
> Le RouterModule associe des URLs à des composants. Quand l'URL change, Angular affiche le composant correspondant sans recharger la page.

---

## 🤖 IA / Python (20 questions)

### Q56 : Comment fonctionne l'algorithme de recommandation ?
> C'est un algorithme de scoring. Chaque exercice reçoit des points selon 4 critères : type (+30), intensité (+20), objectif (+15), sans équipement (+10). Les 6 meilleurs sont retournés.

### Q57 : Pourquoi un algorithme de scoring et pas du Machine Learning ?
> Pour un projet académique, un scoring basé sur des règles est suffisant, explicable et déterministe. Le ML nécessiterait des données d'entraînement massives.

### Q58 : Combien d'exercices sont dans la base ?
> 17 exercices répartis en 4 catégories : FORCE (4), CARDIO (4), MOBILITE (4), TECHNIQUE (5).

### Q59 : Combien de recommandations sont retournées ?
> 6 exercices, triés par score décroissant. C'est un bon compromis entre choix et surcharge d'information.

### Q60 : Quel est le score maximum possible ?
> 75 points (30+20+15+10). Un exercice qui correspond parfaitement au type, à l'intensité, à l'objectif, et qui ne nécessite pas d'équipement.

### Q61 : Comment le mapping d'intensité fonctionne-t-il ?
> FAIBLE→DEBUTANT, MOYENNE/MODEREE→INTERMEDIAIRE, ELEVEE/HAUTE→AVANCE. Ça permet de comparer l'intensité de la séance avec la difficulté de l'exercice.

### Q62 : Comment les mots-clés de l'objectif sont-ils comparés ?
> L'objectif est découpé en mots. Chaque mot de plus de 3 caractères est cherché dans le champ "objectif" de l'exercice. Un seul match suffit pour +15 points.

### Q63 : Pourquoi ignorer les mots de 3 caractères ou moins ?
> Pour éviter les faux positifs avec des mots courants comme "de", "le", "et", "un" qui n'ont pas de valeur sémantique.

### Q64 : Qu'est-ce que Flask-CORS ?
> Une extension Flask qui ajoute les headers CORS aux réponses. Permet à Spring Boot (autre origine) d'appeler le service Python.

### Q65 : Comment tester le service IA indépendamment ?
> Avec curl : `curl -X POST http://localhost:5000/api/ai/recommend -H "Content-Type: application/json" -d '{"typeSeance":"FORCE","intensite":"MOYENNE","objectifProgramme":"force"}'`

### Q66 : Que se passe-t-il si aucun exercice ne correspond ?
> Tous les exercices reçoivent un score (même 0). Les 6 "meilleurs" sont retournés avec la raison "exercice complémentaire recommandé".

### Q67 : Pourquoi la base d'exercices est-elle en mémoire ?
> Simplicité pour le prototype. Pas de dépendance à une BDD supplémentaire. Les 17 exercices sont fixes. En production, on utiliserait une vraie BDD.

### Q68 : Qu'est-ce que jsonify() en Flask ?
> Convertit un dictionnaire Python en réponse JSON avec le bon Content-Type. Équivalent de ResponseEntity en Spring Boot.

### Q69 : Qu'est-ce que request.get_json() ?
> Récupère et parse le corps JSON de la requête HTTP. Équivalent de @RequestBody en Spring Boot.

### Q70 : Comment le service gère-t-il une requête sans body ?
> Il vérifie `if not context` et renvoie une erreur 400 avec un message explicatif.

### Q71 : Qu'est-ce que le fallback côté Spring Boot ?
> Si le service Python est indisponible, Spring Boot renvoie une réponse avec status "fallback" et une liste vide. L'application continue de fonctionner.

### Q72 : Comment Spring Boot sait-il que Python est en panne ?
> RestTemplate lance une exception (ConnectException, TimeoutException). Le catch dans AIRecommendationService active le fallback.

### Q73 : Pourquoi le health check existe-t-il ?
> Pour vérifier proactivement si l'IA est disponible AVANT de tenter une recommandation. Le frontend peut afficher un indicateur.

### Q74 : L'algorithme est-il déterministe ?
> Oui. Pour le même contexte, il retourne toujours les mêmes résultats dans le même ordre. Pas d'aléatoire.

### Q75 : Comment améliorer l'algorithme ?
> Ajouter plus de critères (historique du sportif, météo, fatigue), utiliser du NLP pour les objectifs, ou intégrer un vrai modèle ML entraîné sur des données réelles.

---

## 🔒 Sécurité / Rôles (10 questions)

### Q76 : Quels rôles existent dans l'application ?
> ADMIN, COACH, SPORTIF, JOUEUR, TERRAIN_MANAGER. Pour le coaching, seuls COACH et ADMIN ont accès.

### Q77 : Comment l'authentification fonctionne-t-elle ?
> JWT (JSON Web Token). L'utilisateur se connecte, reçoit un token. Ce token est envoyé dans chaque requête suivante (header Authorization: Bearer ...).

### Q78 : Qu'est-ce que CORS et pourquoi est-ce nécessaire ?
> Cross-Origin Resource Sharing. Le navigateur bloque les requêtes entre origines différentes (4200→8080). CORS autorise explicitement ces requêtes.

### Q79 : Pourquoi CSRF est-il désactivé ?
> CSRF protège les applications avec sessions/cookies. Notre API est stateless (JWT dans les headers), donc pas vulnérable aux attaques CSRF.

### Q80 : Qu'est-ce que BCrypt ?
> Un algorithme de hachage de mots de passe. Il est lent exprès (pour ralentir les attaques brute-force) et utilise un salt (valeur aléatoire) pour que deux mêmes mots de passe aient des hash différents.

### Q81 : Qu'est-ce que le header X-User-Id ?
> Un header HTTP personnalisé envoyé par Angular contenant l'ID de l'utilisateur connecté. Le backend l'utilise pour vérifier le rôle dans la BDD.

### Q82 : Que signifie STATELESS ?
> Le serveur ne stocke pas de session. Chaque requête contient toutes les informations nécessaires (le JWT). Plus scalable et simple.

### Q83 : Que se passe-t-il si un SPORTIF essaie d'accéder au coaching ?
> Spring Security renvoie une erreur 403 (Forbidden) car la route nécessite le rôle COACH ou ADMIN.

### Q84 : Comment le frontend sait-il quel rôle a l'utilisateur ?
> Le JWT contient le rôle. Angular le décode et stocke le rôle dans AuthService. La propriété `canManageExercises` vérifie ce rôle.

### Q85 : Pourquoi la sécurité est-elle à deux niveaux ?
> Spring Security vérifie au niveau HTTP (routes). CoachingRoleService vérifie au niveau métier (header X-User-Id). Double protection.

---

## 🗄️ Base de données / JPA (15 questions)

### Q86 : Quelle base de données utilisez-vous ?
> H2 en développement (base en mémoire, pratique pour les tests). En production, on utiliserait PostgreSQL ou MySQL.

### Q87 : Qu'est-ce que JPA ?
> Java Persistence API. Un standard Java pour mapper des objets Java vers des tables relationnelles (ORM = Object-Relational Mapping).

### Q88 : Qu'est-ce que Hibernate ?
> L'implémentation de JPA utilisée par Spring Boot. C'est le moteur qui traduit les opérations Java en requêtes SQL.

### Q89 : Qu'est-ce qu'une clé primaire ?
> Un identifiant unique pour chaque ligne d'une table. Marqué par @Id. Deux lignes ne peuvent pas avoir le même ID.

### Q90 : Qu'est-ce qu'une clé étrangère (FK) ?
> Une colonne qui référence la clé primaire d'une autre table. @JoinColumn crée une FK. Ex: programme_id dans SeanceEntrainement référence ProgrammeEntrainement.

### Q91 : Qu'est-ce que @Enumerated(EnumType.STRING) ?
> Stocke l'enum comme texte en BDD ("ACTIF") au lieu d'un nombre (1). Plus lisible et résistant aux changements d'ordre.

### Q92 : Qu'est-ce que mappedBy ?
> Indique quel côté de la relation est le "propriétaire". `mappedBy = "programme"` signifie que SeanceEntrainement possède la FK (programme_id).

### Q93 : Qu'est-ce qu'une relation OneToOne ?
> Une ligne dans la table A correspond à exactement une ligne dans la table B. Ex: une séance a au plus un suivi.

### Q94 : Qu'est-ce qu'une table de liaison ?
> SeanceExercice est une table de liaison entre Seance et Exercice. Elle permet une relation ManyToMany avec des attributs supplémentaires (séries, reps...).

### Q95 : Pourquoi @Column(unique = true) sur l'email ?
> Pour garantir qu'il n'y a pas deux utilisateurs avec le même email. La BDD refuse l'insertion si l'email existe déjà.

### Q96 : Qu'est-ce que @Builder.Default ?
> Avec Lombok @Builder, les valeurs par défaut des champs sont ignorées. @Builder.Default force l'utilisation de la valeur par défaut même avec le Builder.

### Q97 : Comment Spring Data génère-t-il les requêtes ?
> À partir du nom de la méthode. `findByProgrammeIdProgramme(5)` → `SELECT * FROM seance WHERE programme_id = 5`. C'est automatique.

### Q98 : Qu'est-ce que CascadeType.ALL ?
> Toutes les opérations (persist, merge, remove, refresh, detach) sur le parent se propagent aux enfants. Simplifie la gestion des relations.

### Q99 : Qu'est-ce qu'un rollback ?
> L'annulation de toutes les modifications d'une transaction en cas d'erreur. La BDD revient à son état précédent. Garanti par @Transactional.

### Q100 : Comment la base H2 est-elle initialisée ?
> Le DemoDataLoader (classe @Configuration) crée des données de test au démarrage : utilisateurs, exercices, programmes. En mémoire = tout est recréé à chaque redémarrage.
