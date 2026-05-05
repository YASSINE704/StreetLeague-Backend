# Code hyper commenté — `frontend/src/app/features/coaching/dashboard/dashboard.component.html`

## 1. Rôle du fichier

Template Angular HTML : structure visuelle de la page.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **HTML Angular**.
- Utilise les directives Angular comme `*ngIf`, `*ngFor`, `(click)` et `[(ngModel)]`.

## 3. Comment il communique avec les autres fichiers

- Appelé par Angular Router lorsque l’utilisateur ouvre la page correspondante.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `<div class="sl-page">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `  <!-- Hero Banner -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 4 | `  <div class="db-hero">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 5 | `    <div class="db-hero__gfx"></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 6 | `    <div class="db-hero__left">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `      <p class="db-hero__label">MODULE COACHING</p>` | Ligne liée aux rôles et aux permissions utilisateur. |
| 8 | `      <h1 class="db-hero__title">Tableau de Bord</h1>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 9 | `      <div class="db-hero__chips">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `        <span class="db-hero__chip">📋 {{ totalProgrammes }} programmes</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 11 | `        <span class="db-hero__chip">🏋️ {{ totalSeances }} séances</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 12 | `        <span class="db-hero__chip">💪 {{ totalExercices }} exercices</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 13 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 14 | `      <div class="db-hero__progress">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `        <div class="db-hero__progress-label">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `          <span>Progression globale</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `          <strong>{{ tauxCompletion }}%</strong>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 19 | `        <div class="db-hero__progress-track">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `          <div class="db-hero__progress-fill" [style.width.%]="tauxCompletion"></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 22 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 23 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 24 | `    <div class="db-hero__right">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `      <div class="db-hero__ring">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `        <span class="db-hero__ring-value">{{ tauxCompletion }}%</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `        <span class="db-hero__ring-label">Complétion</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 28 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 29 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 30 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 31 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 32 | `  <!-- Stats Cards -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `  <div class="db-stats">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `    <div class="db-stat db-stat--blue">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `      <div class="db-stat__icon">📋</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `      <div class="db-stat__body">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `        <span class="db-stat__value">{{ totalProgrammes }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 38 | `        <span class="db-stat__label">Programmes</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 40 | `      <span class="db-stat__sub">{{ programmesActifs }} actifs</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 42 | `    <div class="db-stat db-stat--green">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `      <div class="db-stat__icon">✅</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 44 | `      <div class="db-stat__body">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 45 | `        <span class="db-stat__value">{{ seancesRealisees }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 46 | `        <span class="db-stat__label">Réalisées</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 47 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 48 | `      <span class="db-stat__sub">sur {{ totalSeances }} séances</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 50 | `    <div class="db-stat db-stat--orange">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `      <div class="db-stat__icon">⏳</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `      <div class="db-stat__body">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 53 | `        <span class="db-stat__value">{{ seancesPrevues }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `        <span class="db-stat__label">Prévues</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `      <span class="db-stat__sub">à venir</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 58 | `    <div class="db-stat db-stat--violet">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 59 | `      <div class="db-stat__icon">💪</div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `      <div class="db-stat__body">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `        <span class="db-stat__value">{{ totalExercices }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `        <span class="db-stat__label">Exercices</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 64 | `      <span class="db-stat__sub">dans le catalogue</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 66 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 67 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 68 | `  <!-- Two columns -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `  <div class="db-two-col">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 71 | `    <!-- Left: Séances progress + Exercices by type -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `    <div class="db-col">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 73 | `      <!-- Séances Breakdown -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 74 | `      <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `        <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `          <h3>📊 Répartition des séances</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 77 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 78 | `        <div class="db-breakdown">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `          <div class="db-breakdown__item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 80 | `            <div class="db-breakdown__header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `              <span class="db-breakdown__label">Réalisées</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 82 | `              <span class="db-breakdown__count">{{ seancesRealisees }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 83 | `            </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 84 | `            <div class="db-bar"><div class="db-bar__fill db-bar__fill--green" [style.width.%]="totalSeances ? (seancesRealisees/totalSeances)*100 : 0"></div></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 85 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 86 | `          <div class="db-breakdown__item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 87 | `            <div class="db-breakdown__header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 88 | `              <span class="db-breakdown__label">Prévues</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 89 | `              <span class="db-breakdown__count">{{ seancesPrevues }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 90 | `            </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 91 | `            <div class="db-bar"><div class="db-bar__fill db-bar__fill--blue" [style.width.%]="totalSeances ? (seancesPrevues/totalSeances)*100 : 0"></div></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 92 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 93 | `          <div class="db-breakdown__item">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 94 | `            <div class="db-breakdown__header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 95 | `              <span class="db-breakdown__label">Annulées</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 96 | `              <span class="db-breakdown__count">{{ seancesAnnulees }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 97 | `            </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 98 | `            <div class="db-bar"><div class="db-bar__fill db-bar__fill--red" [style.width.%]="totalSeances ? (seancesAnnulees/totalSeances)*100 : 0"></div></div>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 99 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 100 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 101 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 102 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 103 | `      <!-- Exercices by type -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 104 | `      <div class="sl-card" *ngIf="exercicesByType.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 105 | `        <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 106 | `          <h3>💪 Exercices par type</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 107 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 108 | `        <div class="db-type-grid">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 109 | `          <div class="db-type-card" *ngFor="let t of exercicesByType" [style.border-left-color]="t.color">` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 110 | `            <span class="db-type-icon">{{ t.icon }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 111 | `            <div class="db-type-info">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 112 | `              <span class="db-type-count">{{ t.count }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 113 | `              <span class="db-type-name">{{ t.type }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 114 | `            </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 115 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 116 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 117 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 118 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 119 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 120 | `    <!-- Right: Suivi averages + Quick actions -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 121 | `    <div class="db-col">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 122 | `      <!-- Suivi Moyennes -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 123 | `      <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 124 | `        <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 125 | `          <h3>🎯 Moyennes Suivi</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 126 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 127 | `        <div class="db-avg-grid">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 128 | `          <div class="db-avg-card db-avg-card--blue">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 129 | `            <span class="db-avg-emoji">😊</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 130 | `            <span class="db-avg-value">{{ moyenneRessenti }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 131 | `            <span class="db-avg-label">Ressenti /10</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 132 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 133 | `          <div class="db-avg-card db-avg-card--orange">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 134 | `            <span class="db-avg-emoji">😓</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 135 | `            <span class="db-avg-value">{{ moyenneFatigue }}</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 136 | `            <span class="db-avg-label">Fatigue /10</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 137 | `          </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 138 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 139 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 140 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 141 | `      <!-- Quick Actions -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 142 | `      <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 143 | `        <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 144 | `          <h3>⚡ Actions rapides</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 145 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 146 | `        <div class="db-quick-actions">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 147 | `          <a routerLink="/coaching/programmes/create" class="db-action-btn">` | Ligne liée aux rôles et aux permissions utilisateur. |
| 148 | `            <span class="db-action-icon">📋</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 149 | `            <span>Nouveau programme</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 150 | `          </a>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 151 | `          <a routerLink="/coaching/exercices/create" class="db-action-btn">` | Ligne liée aux rôles et aux permissions utilisateur. |
| 152 | `            <span class="db-action-icon">💪</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 153 | `            <span>Nouvel exercice</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 154 | `          </a>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 155 | `          <a routerLink="/coaching/programmes" class="db-action-btn">` | Ligne liée aux rôles et aux permissions utilisateur. |
| 156 | `            <span class="db-action-icon">📊</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 157 | `            <span>Voir programmes</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 158 | `          </a>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 159 | `          <a routerLink="/coaching/exercices" class="db-action-btn">` | Ligne liée aux rôles et aux permissions utilisateur. |
| 160 | `            <span class="db-action-icon">🔍</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 161 | `            <span>Catalogue exercices</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 162 | `          </a>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 163 | `        </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 164 | `      </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 165 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 166 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 167 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 168 | `  <!-- Recent Séances -->` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 169 | `  <div class="sl-card">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 170 | `    <div class="sl-card-header">` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 171 | `      <h3>🕐 Dernières séances</h3>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 172 | `      <a routerLink="/coaching/programmes" class="db-view-all">Voir tout →</a>` | Ligne liée aux rôles et aux permissions utilisateur. |
| 173 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 174 | `    <table class="sl-table" *ngIf="recentSeances.length > 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 175 | `      <thead>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 176 | `        <tr><th>Titre</th><th>Programme</th><th>Date</th><th>Durée</th><th>Intensité</th><th>Statut</th></tr>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 177 | `      </thead>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 178 | `      <tbody>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 179 | `        <tr *ngFor="let s of recentSeances">` | Directive Angular : répète cet élément pour chaque élément d’une liste. |
| 180 | `          <td class="sl-td-title">{{ s.titreSeance }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 181 | `          <td>{{ s.programmeTitre }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 182 | `          <td>{{ s.dateSeance }}</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 183 | `          <td>{{ s.dureeMinutes }} min</td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 184 | `          <td><span class="sl-badge sl-badge--orange">{{ s.intensite }}</span></td>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 185 | `          <td><span class="sl-badge" [ngClass]="{'sl-badge--blue':s.statut==='PREVUE','sl-badge--green':s.statut==='REALISEE','sl-badge--red':s.statut==='ANNULEE'}">{{ s.statut }}</span></td>` | Ajoute des classes CSS dynamiquement selon une condition. |
| 186 | `        </tr>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 187 | `      </tbody>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 188 | `    </table>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 189 | `    <div class="sl-empty" *ngIf="recentSeances.length === 0">` | Directive Angular : affiche cet élément seulement si la condition est vraie. |
| 190 | `      <span class="sl-empty-icon">🏋️</span>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 191 | `      <p>Aucune séance pour le moment</p>` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 192 | `    </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 193 | `  </div>` | Fin d’un bloc de code ou d’un élément HTML. |
| 194 | `</div>` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `dashboard.component.html` construit l’interface affichée à l’utilisateur. Les directives Angular permettent d’afficher ou cacher des blocs selon le rôle, l’état de la séance ou les données chargées.