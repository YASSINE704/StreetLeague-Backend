# Code hyper commenté — `frontend/src/app/features/coaching/seances/seance-details/seance-details.component.html`

## 1. Rôle du fichier

Template Angular HTML : structure visuelle de la page.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **HTML Angular**.
- Utilise les directives Angular comme `*ngIf`, `*ngFor`, `(click)` et `[(ngModel)]`.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Point central de l’interface où le coach gère les exercices d’une séance, manuellement ou avec l’IA.
- Appelé par Angular Router lorsque l’utilisateur ouvre la page correspondante.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `<div class="sl-page sl-page--medium" *ngIf="seance">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 2 | `  <!-- Hero Banner -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 3 | `  <div class="seance-hero-banner">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 4 | `    <div class="seance-hero-top">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 5 | `      <button (click)="onBack()" class="sl-btn sl-btn--ghost sl-btn--sm">← Retour aux séances</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 6 | `      <span class="sl-badge" [ngClass]="{` | Ajoute des classes CSS dynamiquement selon une condition. |
| 7 | `        'sl-badge--blue': seance.statut === 'PREVUE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `        'sl-badge--green': seance.statut === 'REALISEE',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 9 | `        'sl-badge--red': seance.statut === 'ANNULEE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `      }">{{ seance.statut }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 11 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 12 | `    <h1 class="seance-hero-title">{{ seance.titreSeance }}</h1>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 13 | `    <div class="seance-hero-meta">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `      <div class="seance-meta-chip"><span class="seance-meta-icon">📅</span> {{ seance.dateSeance }}</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `      <div class="seance-meta-chip"><span class="seance-meta-icon">⏱️</span> {{ seance.dureeMinutes }} min</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `      <div class="seance-meta-chip"><span class="seance-meta-icon">🔥</span> {{ seance.intensite }}</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `      <div class="seance-meta-chip" *ngIf="seance.programmeTitre"><span class="seance-meta-icon">📋</span> {{ seance.programmeTitre }}</div>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 18 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 19 | `    <!-- Stats bar -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `    <div class="seance-stats-bar">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `      <div class="seance-stat-item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `        <span class="seance-stat-number">{{ seance.exercices?.length \|\| 0 }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `        <span class="sl-stat-label">Exercices</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 25 | `      <div class="seance-stat-divider"></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `      <div class="seance-stat-item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `        <span class="seance-stat-number">{{ getTotalSeries() }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `        <span class="sl-stat-label">Séries totales</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 30 | `      <div class="seance-stat-divider"></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `      <div class="seance-stat-item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `        <span class="seance-stat-number">{{ seance.dureeMinutes \|\| 0 }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `        <span class="sl-stat-label">Minutes</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 35 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 36 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `  <div class="sl-error-banner" *ngIf="errorMessage">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 39 | `    <span>⚠️</span> {{ errorMessage }}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `    <button class="sl-error-close" (click)="errorMessage = ''">✕</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 41 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 42 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 43 | `  <div class="sl-toast" *ngIf="successMessage">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 44 | `    <span>✅</span> {{ successMessage }}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 45 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 46 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 47 | `  <!-- Exercices Section -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `  <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `    <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `      <div class="seance-section-title-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `        <span class="seance-section-icon">🏋️</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `        <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `          <h2>Exercices de la séance</h2>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `          <p class="seance-section-subtitle">{{ seance.exercices?.length \|\| 0 }} exercice(s) programmé(s)</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 57 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 58 | `      <div class="seance-header-actions" *ngIf="canManageExercises">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 59 | `        <button (click)="toggleAiPanel()" class="sl-btn sl-btn--secondary">` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 60 | `          <span *ngIf="!showAiPanel">🤖 IA</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 61 | `          <span *ngIf="showAiPanel">✕ Fermer IA</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 62 | `        </button>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `        <button (click)="toggleAddForm()" class="sl-btn sl-btn--primary">` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 64 | `          <span *ngIf="!showAddForm">+ Ajouter</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 65 | `          <span *ngIf="showAddForm">✕ Fermer</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 66 | `        </button>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 68 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 69 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 70 | `    <div class="seance-readonly-note" *ngIf="!canManageExercises && seance.statut !== 'REALISEE'">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 71 | `      <span>ℹ️</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `      Le sportif peut consulter les exercices, mais seul le coach peut les ajouter, les retirer ou utiliser l'IA.` | Ligne liée aux rôles et aux permissions utilisateur. |
| 73 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 74 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 75 | `    <!-- AI Assistant Panel -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `    <div class="seance-ai-panel" *ngIf="showAiPanel && canManageExercises">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 77 | `      <div class="seance-ai-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 78 | `        <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `          <span class="seance-form-badge seance-form-badge--ai">IA</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 80 | `          <span class="seance-ai-title">Assistant intelligent d'exercices</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 82 | `        <p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 83 | `          L'IA propose des exercices selon le contexte de la séance. Le coach garde toujours le contrôle : rien n'est ajouté sans validation.` | Ligne liée aux rôles et aux permissions utilisateur. |
| 84 | `        </p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 85 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 86 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 87 | `      <div class="sl-form-row-3">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 88 | `        <div class="sl-form-group sl-span-2">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 89 | `          <label>Objectif envoyé à l'IA</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 90 | `          <input type="text" class="sl-input" [(ngModel)]="aiContext.objectifProgramme"` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 91 | `                 placeholder="Ex: renforcement musculaire, cardio, mobilité...">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 92 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 93 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 94 | `          <label>Type à privilégier</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 95 | `          <select class="sl-select" [(ngModel)]="aiContext.typeSeance">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 96 | `            <option *ngFor="let type of aiTypes" [value]="type">{{ type }}</option>` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 97 | `          </select>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 98 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 99 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 100 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 101 | `      <div class="sl-form-row-3">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 102 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 103 | `          <label>Niveau joueurs</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 104 | `          <select class="sl-select" [(ngModel)]="aiContext.niveauJoueurs">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 105 | `            <option value="Débutant">Débutant</option>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 106 | `            <option value="Tous niveaux">Tous niveaux</option>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 107 | `            <option value="Intermédiaire">Intermédiaire</option>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 108 | `            <option value="Avancé">Avancé</option>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 109 | `          </select>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 110 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 111 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 112 | `          <label>Intensité séance</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 113 | `          <input type="text" class="sl-input" [value]="seance.intensite \|\| 'MOYENNE'" disabled>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 114 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 115 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 116 | `          <label>Durée</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 117 | `          <input type="text" class="sl-input" [value]="(seance.dureeMinutes \|\| 60) + ' min'" disabled>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 118 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 119 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 120 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 121 | `      <div class="seance-ai-actions">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 122 | `        <button class="sl-btn sl-btn--primary" (click)="generateAIExercises()" [disabled]="aiLoading">` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 123 | `          <span *ngIf="!aiLoading">✨ Proposer avec IA</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 124 | `          <span *ngIf="aiLoading">Génération en cours...</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 125 | `        </button>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 126 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 127 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 128 | `      <div class="seance-ai-message seance-ai-message--warning" *ngIf="aiMessage">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 129 | `        <span>ℹ️</span> {{ aiMessage }}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 130 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 131 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 132 | `      <div class="seance-ai-results-head" *ngIf="aiRecommendations.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 133 | `        <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 134 | `          <h3>Suggestions IA</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 135 | `          <p>Choisissez uniquement les exercices que vous voulez ajouter à la séance.</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 136 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 137 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 138 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 139 | `      <div class="seance-ai-grid" *ngIf="aiRecommendations.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 140 | `        <div class="seance-ai-card" *ngFor="let rec of aiRecommendations; let i = index">` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 141 | `          <div class="seance-ai-card-top">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 142 | `            <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 143 | `              <h3>{{ rec.nom }}</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 144 | `              <span class="seance-param-tag purple">{{ rec.type }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 145 | `              <span class="seance-param-tag orange" *ngIf="rec.difficulte">{{ rec.difficulte }}</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 146 | `            </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 147 | `            <span class="seance-ai-score" *ngIf="rec.scoreRelevance">Score {{ rec.scoreRelevance }}</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 148 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 149 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 150 | `          <p class="seance-ai-desc">{{ rec.description \|\| rec.objectif \|\| 'Exercice recommandé selon le contexte de la séance.' }}</p>` | Ligne liée à la recommandation IA d’exercices. |
| 151 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 152 | `          <div class="seance-ai-info">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 153 | `            <span *ngIf="rec.dureeMinutes">⏱️ {{ rec.dureeMinutes }} min</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 154 | `            <span *ngIf="rec.equipement">🎒 {{ rec.equipement }}</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 155 | `            <span *ngIf="rec.niveauRecommande">🎯 {{ rec.niveauRecommande }}</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 156 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 157 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 158 | `          <div class="seance-ai-reason" *ngIf="rec.raison">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 159 | `            <strong>Pourquoi :</strong> {{ rec.raison }}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 160 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 161 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 162 | `          <div class="seance-ai-safety" *ngIf="rec.consigneSecurite">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 163 | `            <strong>Sécurité :</strong> {{ rec.consigneSecurite }}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 164 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 165 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 166 | `          <button class="sl-btn sl-btn--primary seance-ai-add-btn"` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 167 | `                  (click)="acceptAIExercise(rec, i)"` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 168 | `                  [disabled]="acceptingAiIndex === i">` | Binding Angular : désactive le bouton selon une condition. |
| 169 | `            <span *ngIf="acceptingAiIndex !== i">Valider et ajouter à la séance</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 170 | `            <span *ngIf="acceptingAiIndex === i">Ajout...</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 171 | `          </button>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 172 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 173 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 174 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 175 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 176 | `    <!-- Inline Add Form -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 177 | `    <div class="seance-add-form-panel" *ngIf="showAddForm && canManageExercises">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 178 | `      <div class="seance-add-form-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 179 | `        <span class="seance-form-badge">Nouveau</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 180 | `        <span>Ajouter un exercice depuis le catalogue</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 181 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 182 | `      <div class="sl-form-row-3">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 183 | `        <div class="sl-form-group sl-span-2">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 184 | `          <label>Exercice</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 185 | `          <select [(ngModel)]="newExercice.exerciceId" class="sl-select">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 186 | `            <option [value]="0" disabled>-- Choisir un exercice --</option>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 187 | `            <option *ngFor="let ex of catalogueExercices" [value]="ex.idExercice">{{ ex.nom }} ({{ ex.type }})</option>` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 188 | `          </select>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 189 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 190 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 191 | `          <label>Ordre</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 192 | `          <input type="number" class="sl-input" [(ngModel)]="newExercice.ordre" min="1">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 193 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 194 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 195 | `      <div class="sl-form-row-4">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 196 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 197 | `          <label>Séries</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 198 | `          <input type="number" class="sl-input" [(ngModel)]="newExercice.series" min="1">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 199 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 200 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 201 | `          <label>Répétitions</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 202 | `          <input type="number" class="sl-input" [(ngModel)]="newExercice.repetitions" min="0">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 203 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 204 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 205 | `          <label>Charge (kg)</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 206 | `          <input type="number" class="sl-input" [(ngModel)]="newExercice.charge" min="0" step="0.5">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 207 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 208 | `        <div class="sl-form-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 209 | `          <label>Temps (s)</label>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 210 | `          <input type="number" class="sl-input" [(ngModel)]="newExercice.tempsSecondes" min="0">` | Double binding Angular : relie un champ HTML à une variable TypeScript. |
| 211 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 212 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 213 | `      <div class="sl-form-actions">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 214 | `        <button (click)="toggleAddForm()" class="sl-btn sl-btn--ghost">Annuler</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 215 | `        <button (click)="onAddExercice()" class="sl-btn sl-btn--primary">Ajouter à la séance</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 216 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 217 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 218 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 219 | `    <!-- Exercice List -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 220 | `    <div class="seance-exercice-list" *ngIf="seance.exercices && seance.exercices.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 221 | `      <div class="seance-exercice-row" *ngFor="let ex of seance.exercices; let i = index">` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 222 | `        <div class="seance-exercice-order">{{ ex.ordre }}</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 223 | `        <div class="seance-exercice-main">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 224 | `          <span class="seance-exercice-name">{{ ex.exerciceNom }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 225 | `          <div class="seance-exercice-params">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 226 | `            <span class="seance-param-tag blue">{{ ex.series }} séries</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 227 | `            <span class="seance-param-tag green" *ngIf="ex.repetitions">{{ ex.repetitions }} reps</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 228 | `            <span class="seance-param-tag orange" *ngIf="ex.charge">{{ ex.charge }} kg</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 229 | `            <span class="seance-param-tag purple" *ngIf="ex.tempsSecondes">{{ ex.tempsSecondes }}s</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 230 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 231 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 232 | `        <button *ngIf="canManageExercises"` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 233 | `                (click)="onRemoveExercice(ex.idSeanceExercice!)"` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 234 | `                class="seance-btn-remove" title="Retirer">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 235 | `          ✕` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 236 | `        </button>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 237 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 238 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 239 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 240 | `    <!-- Empty -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 241 | `    <div class="sl-empty" *ngIf="!seance.exercices \|\| seance.exercices.length === 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 242 | `      <span class="sl-empty-icon">🏋️</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 243 | `      <p>Aucun exercice programmé</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 244 | `      <span class="sl-empty-hint" *ngIf="canManageExercises">Ajoutez des exercices depuis le catalogue ou utilisez l'IA pour construire cette séance</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 245 | `      <span class="sl-empty-hint" *ngIf="!canManageExercises">Les exercices seront visibles ici après planification par le coach</span>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 246 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 247 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 248 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 249 | `  <!-- Suivi Section -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 250 | `  <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 251 | `    <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 252 | `      <div class="seance-section-title-group">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 253 | `        <span class="seance-section-icon">📊</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 254 | `        <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 255 | `          <h2>Suivi de la séance</h2>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 256 | `          <p class="seance-section-subtitle">Ressenti et performance</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 257 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 258 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 259 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 260 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 261 | `    <div class="seance-suivi-grid" *ngIf="seance.suiviSeance">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 262 | `      <div class="seance-suivi-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 263 | `        <span class="seance-suivi-card-label">Ressenti</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 264 | `        <div class="seance-suivi-card-value">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 265 | `          <span class="seance-suivi-number">{{ seance.suiviSeance.ressenti }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 266 | `          <span class="seance-suivi-max">/10</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 267 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 268 | `        <div class="seance-suivi-bar">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 269 | `          <div class="seance-suivi-bar-fill" [style.width.%]="(seance.suiviSeance.ressenti \|\| 0) * 10"></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 270 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 271 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 272 | `      <div class="seance-suivi-card seance-suivi-fatigue">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 273 | `        <span class="seance-suivi-card-label">Fatigue</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 274 | `        <div class="seance-suivi-card-value">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 275 | `          <span class="seance-suivi-number seance-suivi-number--fatigue">{{ seance.suiviSeance.fatigue }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 276 | `          <span class="seance-suivi-max">/10</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 277 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 278 | `        <div class="seance-suivi-bar">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 279 | `          <div class="seance-suivi-bar-fill seance-suivi-bar-fill--fatigue" [style.width.%]="(seance.suiviSeance.fatigue \|\| 0) * 10"></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 280 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 281 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 282 | `      <div class="seance-suivi-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 283 | `        <span class="seance-suivi-card-label">Commentaire</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 284 | `        <p class="seance-suivi-comment">{{ seance.suiviSeance.commentaire \|\| '—' }}</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 285 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 286 | `      <div class="seance-suivi-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 287 | `        <span class="seance-suivi-card-label">Date de validation</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 288 | `        <span class="seance-suivi-date">{{ seance.suiviSeance.dateValidation }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 289 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 290 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 291 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 292 | `    <div class="sl-empty" *ngIf="!seance.suiviSeance && seance.statut === 'REALISEE'">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 293 | `      <button (click)="onAddSuivi()" class="sl-btn sl-btn--primary">+ Ajouter le suivi</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 294 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 295 | `    <div class="sl-empty" *ngIf="!seance.suiviSeance && seance.statut !== 'REALISEE'">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 296 | `      <span class="sl-empty-icon">🔒</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 297 | `      <p>Le suivi sera disponible une fois la séance réalisée</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 298 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 299 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 300 | `</div>` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `seance-details.component.html` construit l’interface affichée à l’utilisateur. Les directives Angular permettent d’afficher ou cacher des blocs selon le rôle, l’état de la séance ou les données chargées.