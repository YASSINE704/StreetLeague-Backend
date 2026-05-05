# 09 - Frontend Angular (Module Coaching)

## 🎯 Qu'est-ce qu'Angular ?

**Angular** = un framework JavaScript/TypeScript pour créer des interfaces web interactives.

### En mots simples :
Angular transforme du code TypeScript en pages web dynamiques. Quand l'utilisateur clique sur un bouton, Angular met à jour la page SANS la recharger entièrement.

### Concepts clés :

| Concept | Définition | Analogie |
|---------|-----------|----------|
| **Component** | Un morceau d'interface réutilisable | Une brique LEGO |
| **Module** | Un groupe de composants liés | Une boîte de LEGO |
| **Service** | Code partagé (appels API, logique) | Un assistant commun |
| **Template** | Le HTML du composant | Le dessin de la brique |
| **Route** | L'URL qui affiche un composant | L'adresse d'une page |

---

## 🏗️ Structure du module Coaching

```
frontend/src/app/features/coaching/
├── coaching.module.ts              ← Déclaration du module
├── coaching-routing.module.ts      ← Routes (URLs)
├── dashboard/                      ← Tableau de bord
├── programmes/                     ← Gestion des programmes
│   ├── programme-list/             ← Liste des programmes
│   ├── programme-create/           ← Formulaire de création
│   ├── programme-edit/             ← Formulaire de modification
│   └── programme-details/          ← Détails d'un programme
├── seances/                        ← Gestion des séances
│   ├── seance-list/                ← Liste des séances
│   ├── seance-create/              ← Formulaire de création
│   ├── seance-edit/                ← Formulaire de modification
│   └── seance-details/             ← Détails + IA (le plus complexe)
├── exercices/                      ← Gestion des exercices
│   ├── exercice-list/              ← Liste/catalogue
│   ├── exercice-create/            ← Création
│   └── exercice-edit/              ← Modification
└── suivis/                         ← Suivi post-séance
    └── suivi-create/               ← Formulaire de suivi
```

---

## 📦 Le Module (coaching.module.ts)

```typescript
@NgModule({
  declarations: [
    // Tous les composants du module
    ProgrammeListComponent,
    ProgrammeCreateComponent,
    ProgrammeEditComponent,
    ProgrammeDetailsComponent,
    SeanceListComponent,
    SeanceCreateComponent,
    SeanceEditComponent,
    SeanceDetailsComponent,
    ExerciceListComponent,
    ExerciceCreateComponent,
    ExerciceEditComponent,
    SuiviCreateComponent,
    DashboardComponent
  ],
  imports: [
    CommonModule,           // Directives de base (*ngIf, *ngFor, etc.)
    FormsModule,            // Formulaires template-driven (ngModel)
    CoachingRoutingModule   // Les routes du module
  ]
})
export class CoachingModule {}
```

### En mots simples :
Le module est comme un **registre** : il liste tous les composants disponibles et les outils qu'ils utilisent.

---

## 🚀 Lazy Loading (Chargement paresseux)

### Le problème :
Si on charge TOUT le code Angular au démarrage, la page met longtemps à s'afficher.

### La solution : Lazy Loading
On ne charge le module Coaching que quand l'utilisateur navigue vers `/coaching`.

### Comment ça marche :
Dans le fichier de routes principal (app-routing) :
```typescript
{
  path: 'coaching',
  loadChildren: () => import('./features/coaching/coaching.module')
    .then(m => m.CoachingModule)
}
```

### En mots simples :
C'est comme un restaurant qui ne prépare votre plat que quand vous le commandez, au lieu de tout préparer à l'avance.

---

## 🛣️ Les Routes (coaching-routing.module.ts)

```typescript
const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },

  // Programmes
  { path: 'programmes', component: ProgrammeListComponent },
  { path: 'programmes/create', component: ProgrammeCreateComponent },
  { path: 'programmes/edit/:id', component: ProgrammeEditComponent },
  { path: 'programmes/:id', component: ProgrammeDetailsComponent },

  // Séances
  { path: 'seances', component: SeanceListComponent },
  { path: 'seances/create', component: SeanceCreateComponent },
  { path: 'seances/create/:programmeId', component: SeanceCreateComponent },
  { path: 'seances/edit/:id', component: SeanceEditComponent },
  { path: 'seances/:id', component: SeanceDetailsComponent },

  // Exercices
  { path: 'exercices', component: ExerciceListComponent },
  { path: 'exercices/create', component: ExerciceCreateComponent },
  { path: 'exercices/edit/:id', component: ExerciceEditComponent },

  // Suivi
  { path: 'suivis/create/:seanceId', component: SuiviCreateComponent },

  // Par défaut → dashboard
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];
```

### Explication des paramètres de route :
- `:id` = paramètre dynamique (ex: `/programmes/5` → id = 5)
- `:programmeId` = ID du programme pour pré-remplir le formulaire
- `redirectTo` = redirection automatique

---

## 📡 Les Services (appels API)

### Comment Angular appelle le backend :

```typescript
@Injectable({ providedIn: 'root' })  // Disponible partout dans l'app
export class AiRecommendationService {
  private apiUrl = 'http://localhost:8080/api/coaching/ai';

  constructor(private http: HttpClient) {}
  // HttpClient = l'outil Angular pour faire des requêtes HTTP

  // Vérifie si l'IA est disponible
  health(): Observable<{ available: boolean; message: string }> {
    return this.http.get<{ available: boolean; message: string }>(`${this.apiUrl}/health`);
  }

  // Demande des recommandations
  recommend(context: AIRecommendationRequest): Observable<AIRecommendationResponse> {
    return this.http.post<AIRecommendationResponse>(`${this.apiUrl}/recommend`, context);
  }
}
```

### Qu'est-ce qu'un Observable ?
Un **Observable** = une "promesse améliorée". C'est un flux de données asynchrone.

```typescript
// Utilisation dans un composant :
this.aiService.recommend(context).subscribe({
  next: (response) => {
    // Succès : on a les recommandations
    this.aiRecommendations = response.recommandations;
  },
  error: (err) => {
    // Erreur : on affiche un message
    this.errorMessage = 'Erreur lors de la génération IA';
  }
});
```

### En mots simples :
- `subscribe` = "je m'abonne pour recevoir la réponse quand elle arrive"
- `next` = "voici ce que je fais quand la réponse arrive"
- `error` = "voici ce que je fais si ça échoue"

---

## 🔑 L'Interceptor (ajout automatique de X-User-Id)

Un **interceptor** = un middleware qui modifie TOUTES les requêtes HTTP avant qu'elles partent.

### Ce qu'il fait :
```typescript
// Simplifié
intercept(req, next) {
  const userId = this.authService.userId;
  if (userId) {
    req = req.clone({
      setHeaders: { 'X-User-Id': userId.toString() }
    });
  }
  return next.handle(req);
}
```

### En mots simples :
L'interceptor est comme un **tampon automatique** : il ajoute l'identifiant de l'utilisateur sur chaque requête, sans qu'on ait à le faire manuellement à chaque fois.

---

## 📝 Template-Driven Forms (Formulaires)

### Qu'est-ce que ngModel ?
`[(ngModel)]` = **two-way binding** (liaison bidirectionnelle)

```html
<input type="text" [(ngModel)]="aiContext.objectifProgramme">
```

### Comment ça marche :
```
Variable TypeScript  ←→  Champ HTML
aiContext.objectifProgramme  ←→  <input>
```

- Si l'utilisateur tape dans l'input → la variable est mise à jour
- Si le code modifie la variable → l'input est mis à jour

### En mots simples :
C'est comme un **miroir** : ce qui est dans la variable est toujours identique à ce qui est affiché dans le champ.

---

## 🎨 Directives Angular expliquées

### `*ngIf` - Affichage conditionnel
```html
<div *ngIf="showAiPanel">
  <!-- Ce bloc n'existe que si showAiPanel est true -->
</div>

<div *ngIf="!canManageExercises">
  <!-- Affiché seulement si l'utilisateur N'EST PAS coach -->
</div>
```

### `*ngFor` - Boucle (répétition)
```html
<div *ngFor="let rec of aiRecommendations; let i = index">
  <!-- Répété pour chaque recommandation -->
  <h3>{{ rec.nom }}</h3>
  <span>Score {{ rec.scoreRelevance }}</span>
</div>
```

### `[ngClass]` - Classes CSS conditionnelles
```html
<span [ngClass]="{
  'sl-badge--blue': seance.statut === 'PREVUE',
  'sl-badge--green': seance.statut === 'REALISEE',
  'sl-badge--red': seance.statut === 'ANNULEE'
}">{{ seance.statut }}</span>
```
→ Applique une classe CSS différente selon le statut

### `{{ }}` - Interpolation
```html
<h1>{{ seance.titreSeance }}</h1>
<!-- Affiche la valeur de la variable -->
```

### `(click)` - Événement
```html
<button (click)="generateAIExercises()">✨ Proposer avec IA</button>
<!-- Quand on clique → appelle la méthode -->
```

### `[disabled]` - Désactivation conditionnelle
```html
<button [disabled]="aiLoading">
  <!-- Le bouton est grisé pendant le chargement -->
</button>
```

### `[value]` - Liaison unidirectionnelle
```html
<input [value]="seance.intensite" disabled>
<!-- Affiche la valeur mais ne peut pas être modifié -->
```

---

## 🔐 Gestion des rôles côté Angular

```typescript
get canManageExercises(): boolean {
  const role = this.authService.userRole;
  return (role === 'COACH' || role === 'ADMIN') && this.seance?.statut !== 'REALISEE';
}
```

### Ce que ça contrôle :
- **COACH/ADMIN** : voit les boutons "Ajouter", "IA", "Retirer"
- **SPORTIF** : voit les exercices en lecture seule + message informatif
- **Séance REALISEE** : personne ne peut modifier (même le coach)

---

## 🎓 Questions du professeur

**Q : Qu'est-ce que TypeScript ?**
> R : TypeScript = JavaScript + types. On déclare le type des variables (string, number, etc.), ce qui permet de détecter les erreurs avant l'exécution. Angular utilise TypeScript par défaut.

**Q : Quelle est la différence entre un Component et un Service ?**
> R : Un Component a une interface visuelle (HTML + CSS + logique). Un Service n'a PAS d'interface, il contient de la logique partagée (appels API, calculs). Plusieurs composants peuvent utiliser le même service.

**Q : Pourquoi FormsModule et pas ReactiveFormsModule ?**
> R : FormsModule = formulaires template-driven (plus simples, avec ngModel). ReactiveFormsModule = formulaires réactifs (plus puissants mais plus complexes). Pour ce projet, template-driven suffit.

**Q : Qu'est-ce que @Injectable({ providedIn: 'root' }) ?**
> R : Ça dit à Angular "crée UNE seule instance de ce service pour toute l'application" (Singleton). Tous les composants partagent la même instance.

**Q : Pourquoi Observable et pas Promise ?**
> R : Observable peut émettre plusieurs valeurs dans le temps (flux), est annulable, et supporte des opérateurs puissants (map, filter, retry). Promise ne peut émettre qu'une seule valeur et n'est pas annulable.

**Q : Qu'est-ce que le lazy loading apporte concrètement ?**
> R : Le bundle initial (le fichier JS chargé au démarrage) est plus petit. Le module coaching n'est téléchargé que quand l'utilisateur navigue vers /coaching. Résultat : la page d'accueil se charge plus vite.
