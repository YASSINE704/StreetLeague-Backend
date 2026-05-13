# Code hyper commenté — `frontend/src/app/features/coaching/programmes/programme-details/programme-details.component.html`

## 1. Rôle du fichier

Template Angular HTML : structure visuelle de la page.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **HTML Angular**.
- Utilise les directives Angular comme `*ngIf`, `*ngFor`, `(click)` et `[(ngModel)]`.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Appelé par Angular Router lorsque l’utilisateur ouvre la page correspondante.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `<div class="sl-page sl-page--medium" *ngIf="programme">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 2 | `  <div class="sl-error-banner" *ngIf="errorMessage">{{ errorMessage }}</div>` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 3 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 4 | `  <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 5 | `    <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 6 | `      <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `        <h2>{{ programme.titre }}</h2>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `        <span class="sl-badge" [ngClass]="{` | Ajoute des classes CSS dynamiquement selon une condition. |
| 9 | `          'sl-badge--orange': programme.statut === 'BROUILLON',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `          'sl-badge--green': programme.statut === 'ACTIF',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 11 | `          'sl-badge--violet': programme.statut === 'TERMINE'` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 12 | `        }">{{ programme.statut }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 13 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 14 | `      <button (click)="onBack()" class="sl-btn sl-btn--secondary">← Retour</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 15 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 16 | `    <div class="programme-header-actions">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `      <button (click)="exportPDF()" class="sl-btn sl-btn--secondary sl-btn--sm">📄 Export PDF</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 18 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 19 | `    <div class="sl-info-grid">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `      <div class="sl-info-item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `        <span class="sl-info-label">Description</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `        <span class="sl-info-value">{{ programme.description \|\| '—' }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 24 | `      <div class="sl-info-item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `        <span class="sl-info-label">Période</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `        <span class="sl-info-value">{{ programme.dateDebut }} → {{ programme.dateFin }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 28 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 29 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `  <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `    <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `      <div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `        <h3>Séances du programme</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `        <span class="programme-section-hint">Gère toutes les séances depuis ce programme</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 37 | `      <button (click)="onAddSeance()" class="sl-btn sl-btn--primary sl-btn--sm">+ Ajouter Séance</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 38 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 39 | `    <table class="sl-table" *ngIf="programme.seances && programme.seances.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 40 | `      <thead>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `        <tr><th>Titre</th><th>Date</th><th>Durée</th><th>Intensité</th><th>Statut</th><th>Actions</th></tr>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 42 | `      </thead>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `      <tbody>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 44 | `        <tr *ngFor="let s of programme.seances">` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 45 | `          <td class="sl-td-title">{{ s.titreSeance }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 46 | `          <td>{{ s.dateSeance }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 47 | `          <td>{{ s.dureeMinutes }} min</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `          <td>{{ s.intensite }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `          <td>{{ s.statut }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `          <td class="sl-actions">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `            <button (click)="onSeanceDetails(s.idSeance!)" class="sl-btn-icon" title="Détails">👁️</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 52 | `            <button (click)="onEditSeance(s.idSeance!)" class="sl-btn-icon" title="Modifier">✏️</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 53 | `            <button (click)="onDeleteSeance(s.idSeance!)" class="sl-btn-icon sl-btn-icon--danger" title="Supprimer">🗑️</button>` | Événement Angular : appelle une méthode TypeScript quand l’utilisateur clique. |
| 54 | `          </td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `        </tr>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `      </tbody>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `    </table>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `    <div class="sl-empty" *ngIf="!programme.seances \|\| programme.seances.length === 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 59 | `      <p>Aucune séance dans ce programme</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 61 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 62 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 63 | `  <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 64 | `    <div class="sl-card-header"><h3>Affectations</h3></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `    <table class="sl-table" *ngIf="programme.affectations && programme.affectations.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 66 | `      <thead><tr><th>Type</th><th>Utilisateur</th><th>Date</th></tr></thead>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `      <tbody>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `        <tr *ngFor="let a of programme.affectations">` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 69 | `          <td><span class="sl-badge" [ngClass]="{'sl-badge--green': a.type==='COACH', 'sl-badge--violet': a.type==='SPORTIF'}">{{ a.type }}</span></td>` | Ajoute des classes CSS dynamiquement selon une condition. |
| 70 | `          <td>{{ a.userNom }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `          <td>{{ a.dateAffectation }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `        </tr>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 73 | `      </tbody>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 74 | `    </table>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `    <div class="sl-empty" *ngIf="!programme.affectations \|\| programme.affectations.length === 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 76 | `      <p>Aucune affectation</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 77 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 78 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 79 | `</div>` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `programme-details.component.html` construit l’interface affichée à l’utilisateur. Les directives Angular permettent d’afficher ou cacher des blocs selon le rôle, l’état de la séance ou les données chargées.