# 10 - Composant Seance-Details (le plus complexe)

## 🎯 Vue d'ensemble

Le composant `seance-details` est le **cœur** du module coaching. Il affiche :
1. Les informations de la séance (titre, date, durée, intensité)
2. La liste des exercices programmés
3. Le **panneau IA** pour recommander des exercices
4. Le **formulaire d'ajout manuel** d'exercices
5. Le **suivi** (ressenti, fatigue) après réalisation

### 📍 Fichiers :
- `frontend/src/app/features/coaching/seances/seance-details/seance-details.component.ts`
- `frontend/src/app/features/coaching/seances/seance-details/seance-details.component.html`

---

## 🤖 Le panneau IA : comment ça marche

### Étape 1 : Le coach ouvre le panneau
```typescript
toggleAiPanel(): void {
  this.showAiPanel = !this.showAiPanel;  // Bascule visible/caché
  if (this.showAiPanel) {
    this.showAddForm = false;  // Ferme le formulaire manuel
  }
}
```
→ Le bouton "🤖 IA" bascule l'affichage du panneau

### Étape 2 : Le coach configure le contexte
Le panneau affiche des champs pré-remplis :
- **Objectif** : pré-rempli avec le titre du programme + séance
- **Type** : FORCE, CARDIO, MOBILITE, TECHNIQUE
- **Niveau** : Débutant, Tous niveaux, Intermédiaire, Avancé
- **Intensité** : prise de la séance (non modifiable)
- **Durée** : prise de la séance (non modifiable)

### Étape 3 : Le coach clique "✨ Proposer avec IA"
```typescript
generateAIExercises(): void {
  // Vérifications
  if (!this.canManageExercises) { return; }  // Doit être COACH/ADMIN
  if (!this.seance?.idSeance) { return; }    // Séance doit exister

  this.aiLoading = true;        // Active le spinner
  this.aiRecommendations = [];  // Vide les anciennes recommandations

  // Construit le contexte pour l'IA
  const context = {
    objectifProgramme: this.aiContext.objectifProgramme,
    typeSeance: this.aiContext.typeSeance,
    intensite: this.seance.intensite || 'MOYENNE',
    nbParticipants: 5,
    dureeSeanceMinutes: this.seance.dureeMinutes || 60,
    niveauJoueurs: this.aiContext.niveauJoueurs || 'Tous niveaux'
  };

  // Appelle le service IA
  this.aiService.recommend(context).subscribe({
    next: (res) => {
      this.aiLoading = false;
      this.aiRecommendations = res.recommandations || [];
      // Si fallback → affiche un message d'avertissement
      this.aiMessage = res.status === 'fallback' ? res.message : '';
    },
    error: (err) => {
      this.aiLoading = false;
      this.errorMessage = 'Erreur lors de la génération IA';
    }
  });
}
```

### Étape 4 : Les recommandations s'affichent
6 cartes apparaissent, chacune avec :
- Nom de l'exercice
- Type (badge coloré)
- Difficulté
- Score de pertinence
- Description
- Durée, équipement, niveau
- Raison de la recommandation
- Consigne de sécurité
- Bouton "Valider et ajouter à la séance"

### Étape 5 : Le coach valide un exercice
```typescript
acceptAIExercise(recommendation: AIExerciseRecommendation, index: number): void {
  this.acceptingAiIndex = index;  // Désactive le bouton (loading)

  // 1. Cherche si l'exercice existe déjà dans le catalogue
  const existing = this.findExistingExercise(recommendation);

  if (existing?.idExercice) {
    // L'exercice existe → on le lie directement à la séance
    this.attachAIExerciseToSeance(existing.idExercice, recommendation, index);
  } else {
    // L'exercice n'existe pas → on le crée d'abord
    this.exerciceService.create({
      nom: recommendation.nom,
      description: recommendation.description,
      type: this.toTypeExercice(recommendation.type)
    }).subscribe({
      next: (created) => {
        this.catalogueExercices.push(created);  // Ajoute au catalogue local
        this.attachAIExerciseToSeance(created.idExercice!, recommendation, index);
      }
    });
  }
}
```

---

## 🔄 Prévention des doublons

### Comment ça marche :

```typescript
private attachAIExerciseToSeance(exerciceId, recommendation, index): void {
  // Vérifie si l'exercice est DÉJÀ dans la séance
  const alreadyAttached = (this.seance.exercices || []).some(ex =>
    Number(ex.exerciceId) === Number(exerciceId) ||
    this.normalizeText(ex.exerciceNom) === this.normalizeText(recommendation.nom)
  );

  if (alreadyAttached) {
    // Déjà présent → on retire la carte et on informe
    this.aiRecommendations.splice(index, 1);
    this.showSuccess(`L'exercice "${recommendation.nom}" est déjà programmé`);
    return;
  }

  // Pas de doublon → on ajoute
  // ...
}
```

### Deux vérifications :
1. **Par ID** : même exerciceId dans la séance
2. **Par nom** : même nom (normalisé en minuscules) → évite les doublons si l'exercice a été créé deux fois

---

## 📝 Le formulaire d'ajout manuel

### Comment ça marche :

```typescript
// Variables du formulaire
newExercice: SeanceExercice = {
  seanceId: 0,       // Rempli automatiquement
  exerciceId: 0,     // Choisi dans le select
  series: 3,         // Valeur par défaut
  repetitions: 10,   // Valeur par défaut
  charge: 0,
  tempsSecondes: 0,
  ordre: 1           // Calculé automatiquement
};
```

### Le flux :
1. Le coach clique "+ Ajouter"
2. Le formulaire apparaît avec un select (liste déroulante) des exercices du catalogue
3. Le coach choisit un exercice et configure les paramètres
4. Le coach clique "Ajouter à la séance"
5. Validation : exercice sélectionné ? Répétitions OU temps renseigné ?
6. Appel API : `POST /api/seance-exercices`
7. Succès : formulaire fermé, séance rechargée

```typescript
onAddExercice(): void {
  // Validations
  if (!this.canManageExercises) { return; }
  if (Number(this.newExercice.exerciceId) === 0) {
    this.errorMessage = 'Veuillez sélectionner un exercice';
    return;
  }

  const payload = this.buildSeanceExercicePayload(this.newExercice);
  if (!payload.repetitions && !payload.tempsSecondes) {
    this.errorMessage = 'Indiquez au moins des répétitions ou un temps';
    return;
  }

  this.exerciceService.addToSeance(payload).subscribe({
    next: () => {
      this.showAddForm = false;
      this.resetManualForm();
      this.showSuccess('Exercice ajouté avec succès');
      this.loadSeance();  // Recharge pour voir le nouvel exercice
    }
  });
}
```

---

## 🔐 Visibilité basée sur les rôles

### La propriété `canManageExercises` :
```typescript
get canManageExercises(): boolean {
  const role = this.authService.userRole;
  return (role === 'COACH' || role === 'ADMIN') && this.seance?.statut !== 'REALISEE';
}
```

### Ce que chaque rôle voit :

| Élément | COACH/ADMIN (séance PREVUE) | COACH (séance REALISEE) | SPORTIF |
|---------|---------------------------|------------------------|---------|
| Bouton "🤖 IA" | ✅ | ❌ | ❌ |
| Bouton "+ Ajouter" | ✅ | ❌ | ❌ |
| Bouton "✕" (retirer) | ✅ | ❌ | ❌ |
| Liste exercices | ✅ | ✅ | ✅ |
| Message "lecture seule" | ❌ | ❌ | ✅ |

### Dans le HTML :
```html
<!-- Boutons visibles seulement pour COACH/ADMIN + séance non réalisée -->
<div *ngIf="canManageExercises">
  <button (click)="toggleAiPanel()">🤖 IA</button>
  <button (click)="toggleAddForm()">+ Ajouter</button>
</div>

<!-- Message pour les sportifs -->
<div *ngIf="!canManageExercises && seance.statut !== 'REALISEE'">
  ℹ️ Seul le coach peut ajouter des exercices
</div>
```

---

## 🎯 Paramètres intelligents pour les exercices IA

Quand le coach valide un exercice IA, les paramètres sont calculés automatiquement :

```typescript
const type = this.toTypeExercice(recommendation.type);
const isTimed = type === TypeExercice.CARDIO || type === TypeExercice.MOBILITE;
const durationSeconds = Math.max(60, (recommendation.dureeMinutes || 5) * 60);

const link: SeanceExercice = {
  seanceId: this.seance.idSeance!,
  exerciceId,
  series: isTimed ? 1 : 3,              // Cardio/Mobilité = 1 série, Force/Technique = 3
  repetitions: isTimed ? undefined : 12, // Cardio/Mobilité = pas de reps
  charge: 0,
  tempsSecondes: isTimed ? durationSeconds : undefined,  // Cardio/Mobilité = temps
  ordre: this.getNextOrder(this.seance)  // Position suivante
};
```

### Logique :
- **FORCE/TECHNIQUE** → 3 séries × 12 répétitions (classique musculation)
- **CARDIO/MOBILITE** → 1 série × durée en secondes (exercice chronométré)

---

## 📊 Le parcours utilisateur complet

```
1. Coach navigue vers /coaching/seances/5
2. La séance se charge avec ses exercices existants
3. Coach clique "🤖 IA"
4. Le panneau IA s'ouvre avec le contexte pré-rempli
5. Coach ajuste l'objectif : "explosivité membres inférieurs"
6. Coach sélectionne type : FORCE
7. Coach clique "✨ Proposer avec IA"
8. Spinner pendant 1-2 secondes
9. 6 cartes de recommandation apparaissent
10. Coach lit les descriptions et scores
11. Coach clique "Valider et ajouter" sur "Squats sautés" (score 75)
12. L'exercice est créé dans le catalogue + lié à la séance
13. La carte disparaît, message de succès
14. Coach valide un 2ème exercice
15. Coach ferme le panneau IA
16. Les 2 nouveaux exercices apparaissent dans la liste
```

---

## ⚠️ Gestion des erreurs dans le composant

```typescript
private extractErrorMessage(err: any, fallback: string): string {
  if (err?.error?.message) return err.error.message;  // Message du backend
  if (err?.error?.error) return err.error.error;      // Erreur Spring
  if (typeof err?.error === 'string') return err.error;
  return fallback;  // Message par défaut
}
```

### Affichage des erreurs :
```html
<div class="sl-error-banner" *ngIf="errorMessage">
  ⚠️ {{ errorMessage }}
  <button (click)="errorMessage = ''">✕</button>
</div>
```

### Affichage du succès :
```html
<div class="sl-toast" *ngIf="successMessage">
  ✅ {{ successMessage }}
</div>
```
Le message de succès disparaît automatiquement après 3.5 secondes :
```typescript
private showSuccess(message: string): void {
  this.successMessage = message;
  setTimeout(() => this.successMessage = '', 3500);
}
```

---

## 🎓 Questions du professeur

**Q : Pourquoi le composant seance-details est-il si complexe ?**
> R : Il gère plusieurs fonctionnalités : affichage, ajout manuel, IA, suppression, suivi. C'est le point central de l'expérience coaching. En production, on pourrait le découper en sous-composants.

**Q : Comment fonctionne la détection de doublons ?**
> R : On compare par ID ET par nom normalisé (minuscules, sans espaces). Si l'exercice est déjà dans la séance, on informe le coach et on retire la carte de recommandation.

**Q : Pourquoi splice(index, 1) sur aiRecommendations ?**
> R : `splice` retire un élément du tableau à l'index donné. Quand le coach valide un exercice, la carte disparaît de la liste des suggestions (elle a été traitée).

**Q : Comment l'ordre des exercices est-il calculé ?**
> R : `getNextOrder()` prend le plus grand ordre existant et ajoute 1. Si la séance a des exercices aux ordres 1, 2, 3, le prochain sera 4.

**Q : Pourquoi le panneau IA et le formulaire ne peuvent pas être ouverts en même temps ?**
> R : Pour éviter la confusion. Quand on ouvre l'un, l'autre se ferme (`this.showAddForm = false` dans `toggleAiPanel()`). L'utilisateur se concentre sur une action à la fois.

**Q : Que fait `this.toTypeExercice(recommendation.type)` ?**
> R : Convertit la chaîne de caractères reçue de l'IA (ex: "FORCE") en enum TypeScript TypeExercice. Si la valeur n'est pas reconnue, elle retourne FORCE par défaut.
