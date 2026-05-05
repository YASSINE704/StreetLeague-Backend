# Code hyper commenté — `frontend/src/app/features/coaching/seances/seance-details/seance-details.component.css`

## 1. Rôle du fichier

CSS Angular : style visuel local du composant.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **CSS**.
- Utilise du CSS custom pour garder le design cohérent avec le projet.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Point central de l’interface où le coach gère les exercices d’une séance, manuellement ou avec l’IA.
- Appelé par Angular Router lorsque l’utilisateur ouvre la page correspondante.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `/* ── Hero Banner (component-specific) ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 2 | `.seance-hero-banner {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 3 | `  background: var(--sl-surface);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 4 | `  border: 1px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 5 | `  border-radius: var(--sl-radius-lg);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 6 | `  box-shadow: var(--sl-shadow-xs);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 7 | `  padding: 2rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 8 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `.seance-hero-top {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 11 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 12 | `  justify-content: space-between;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 13 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 14 | `  margin-bottom: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 15 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `.seance-hero-title {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 18 | `  margin: 0 0 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 19 | `  font-size: 1.6rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 20 | `  font-weight: 700;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 21 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 22 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `.seance-hero-meta {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 25 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 26 | `  flex-wrap: wrap;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 27 | `  gap: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 28 | `  margin-bottom: 1.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 29 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `.seance-meta-chip {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 32 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 33 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 34 | `  gap: 0.4rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 35 | `  background: var(--sl-surface2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 36 | `  border: 1px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 37 | `  border-radius: 10px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 38 | `  padding: 0.5rem 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 39 | `  font-size: 0.85rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 40 | `  color: var(--sl-text2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 41 | `  font-weight: 500;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 42 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 43 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 44 | `.seance-meta-icon { font-size: 1rem; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 45 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 46 | `.seance-stats-bar {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 47 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 48 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 49 | `  gap: 2rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 50 | `  padding-top: 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 51 | `  border-top: 1px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 52 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 53 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 54 | `.seance-stat-item {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 55 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 56 | `  flex-direction: column;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 57 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 58 | `  gap: 0.15rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 59 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 60 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 61 | `.seance-stat-number {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 62 | `  font-size: 1.4rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 63 | `  font-weight: 700;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 64 | `  color: var(--sl-blue);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 65 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 66 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 67 | `.seance-stat-divider {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 68 | `  width: 1px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 69 | `  height: 36px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 70 | `  background: var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 71 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 72 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 73 | `/* ── Section title group ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 74 | `.seance-section-title-group {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 75 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 76 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 77 | `  gap: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 78 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 79 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 80 | `.seance-section-icon { font-size: 1.5rem; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 81 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 82 | `.seance-section-title-group h2 {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 83 | `  margin: 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 84 | `  font-size: 1.2rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 85 | `  font-weight: 700;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 86 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 87 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 88 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 89 | `.seance-section-subtitle {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 90 | `  margin: 0.15rem 0 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 91 | `  font-size: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 92 | `  color: var(--sl-text4);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 93 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 94 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 95 | `/* ── Add Form Panel ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 96 | `.seance-add-form-panel {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 97 | `  background: var(--sl-surface2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 98 | `  border: 1.5px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 99 | `  border-radius: 16px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 100 | `  padding: 1.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 101 | `  margin-bottom: 1.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 102 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 103 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 104 | `.seance-add-form-header {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 105 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 106 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 107 | `  gap: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 108 | `  margin-bottom: 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 109 | `  font-size: 0.9rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 110 | `  color: var(--sl-text2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 111 | `  font-weight: 500;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 112 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 113 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 114 | `.seance-form-badge {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 115 | `  background: var(--sl-blue);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 116 | `  color: #fff;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 117 | `  padding: 0.2rem 0.6rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 118 | `  border-radius: 8px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 119 | `  font-size: 0.7rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 120 | `  font-weight: 700;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 121 | `  text-transform: uppercase;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 122 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 123 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 124 | `/* ── Exercice List (component-specific) ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 125 | `.seance-exercice-list {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 126 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 127 | `  flex-direction: column;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 128 | `  gap: 0.6rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 129 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 130 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 131 | `.seance-exercice-row {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 132 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 133 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 134 | `  gap: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 135 | `  padding: 1rem 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 136 | `  background: var(--sl-surface2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 137 | `  border: 1px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 138 | `  border-radius: 14px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 139 | `  transition: transform 0.15s, box-shadow 0.15s;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 140 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 141 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 142 | `.seance-exercice-row:hover {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 143 | `  transform: translateX(4px);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 144 | `  box-shadow: var(--sl-shadow-xs);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 145 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 146 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 147 | `.seance-exercice-order {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 148 | `  width: 36px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 149 | `  height: 36px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 150 | `  background: var(--sl-blue);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 151 | `  color: #fff;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 152 | `  border-radius: 10px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 153 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 154 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 155 | `  justify-content: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 156 | `  font-weight: 700;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 157 | `  font-size: 0.9rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 158 | `  flex-shrink: 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 159 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 160 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 161 | `.seance-exercice-main {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 162 | `  flex: 1;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 163 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 164 | `  flex-direction: column;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 165 | `  gap: 0.4rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 166 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 167 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 168 | `.seance-exercice-name {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 169 | `  font-weight: 600;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 170 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 171 | `  font-size: 0.95rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 172 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 173 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 174 | `.seance-exercice-params {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 175 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 176 | `  flex-wrap: wrap;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 177 | `  gap: 0.4rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 178 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 179 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 180 | `.seance-param-tag {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 181 | `  display: inline-block;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 182 | `  padding: 0.2rem 0.6rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 183 | `  border-radius: 8px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 184 | `  font-size: 0.72rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 185 | `  font-weight: 600;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 186 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 187 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 188 | `.seance-param-tag.blue { background: var(--sl-blue-l); color: var(--sl-blue); }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 189 | `.seance-param-tag.green { background: var(--sl-green-l); color: var(--sl-green); }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 190 | `.seance-param-tag.orange { background: var(--sl-orange-l); color: var(--sl-orange); }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 191 | `.seance-param-tag.purple { background: var(--sl-violet-l); color: var(--sl-violet); }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 192 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 193 | `.seance-btn-remove {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 194 | `  background: var(--sl-surface2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 195 | `  border: 1px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 196 | `  width: 32px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 197 | `  height: 32px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 198 | `  border-radius: 8px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 199 | `  cursor: pointer;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 200 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 201 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 202 | `  justify-content: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 203 | `  color: var(--sl-text4);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 204 | `  font-size: 0.85rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 205 | `  transition: all 0.2s;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 206 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 207 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 208 | `.seance-btn-remove:hover {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 209 | `  background: var(--sl-red-l);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 210 | `  border-color: var(--sl-red);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 211 | `  color: var(--sl-red);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 212 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 213 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 214 | `/* ── Suivi Grid (component-specific progress bars) ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 215 | `.seance-suivi-grid {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 216 | `  display: grid;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 217 | `  grid-template-columns: 1fr 1fr;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 218 | `  gap: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 219 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 220 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 221 | `.seance-suivi-card {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 222 | `  background: var(--sl-surface2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 223 | `  border: 1px solid var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 224 | `  border-radius: 14px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 225 | `  padding: 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 226 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 227 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 228 | `.seance-suivi-card-label {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 229 | `  font-size: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 230 | `  color: var(--sl-text3);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 231 | `  font-weight: 500;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 232 | `  display: block;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 233 | `  margin-bottom: 0.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 234 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 235 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 236 | `.seance-suivi-card-value {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 237 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 238 | `  align-items: baseline;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 239 | `  gap: 0.2rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 240 | `  margin-bottom: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 241 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 242 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 243 | `.seance-suivi-number {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 244 | `  font-size: 1.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 245 | `  font-weight: 700;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 246 | `  color: var(--sl-blue);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 247 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 248 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 249 | `.seance-suivi-number--fatigue { color: var(--sl-orange); }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 250 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 251 | `.seance-suivi-max {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 252 | `  font-size: 0.9rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 253 | `  color: var(--sl-text4);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 254 | `  font-weight: 500;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 255 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 256 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 257 | `.seance-suivi-bar {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 258 | `  height: 6px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 259 | `  background: var(--sl-border);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 260 | `  border-radius: 3px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 261 | `  overflow: hidden;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 262 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 263 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 264 | `.seance-suivi-bar-fill {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 265 | `  height: 100%;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 266 | `  background: var(--sl-blue);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 267 | `  border-radius: 3px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 268 | `  transition: width 0.6s ease-out;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 269 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 270 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 271 | `.seance-suivi-bar-fill--fatigue {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 272 | `  background: var(--sl-orange);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 273 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 274 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 275 | `.seance-suivi-comment {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 276 | `  margin: 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 277 | `  font-size: 0.9rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 278 | `  color: var(--sl-text2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 279 | `  line-height: 1.5;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 280 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 281 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 282 | `.seance-suivi-date {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 283 | `  font-size: 0.95rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 284 | `  font-weight: 600;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 285 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 286 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 287 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 288 | `@media (max-width: 768px) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 289 | `  .seance-hero-banner { padding: 1.25rem; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 290 | `  .seance-stats-bar { gap: 1rem; flex-wrap: wrap; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 291 | `  .seance-suivi-grid { grid-template-columns: 1fr; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 292 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 293 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 294 | `/* ── Header actions ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 295 | `.seance-header-actions {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 296 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 297 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 298 | `  gap: 0.75rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 299 | `  flex-wrap: wrap;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 300 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 301 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 302 | `.seance-readonly-note {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 303 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 304 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 305 | `  gap: 0.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 306 | `  background: var(--sl-blue-l);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 307 | `  border: 1px solid rgba(37, 99, 235, 0.18);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 308 | `  color: var(--sl-blue);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 309 | `  border-radius: 14px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 310 | `  padding: 0.85rem 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 311 | `  margin-bottom: 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 312 | `  font-size: 0.9rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 313 | `  font-weight: 500;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 314 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 315 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 316 | `/* ── AI Assistant Panel ── */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 317 | `.seance-ai-panel {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 318 | `  position: relative;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 319 | `  background:` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 320 | `    radial-gradient(circle at top left, rgba(37, 99, 235, 0.10), transparent 34%),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 321 | `    linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 322 | `  border: 1px solid rgba(37, 99, 235, 0.18);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 323 | `  border-radius: 20px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 324 | `  padding: 1.35rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 325 | `  margin-bottom: 1.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 326 | `  box-shadow: 0 10px 28px rgba(15, 23, 42, 0.07);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 327 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 328 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 329 | `.seance-ai-header {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 330 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 331 | `  justify-content: space-between;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 332 | `  align-items: flex-start;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 333 | `  gap: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 334 | `  margin-bottom: 1.2rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 335 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 336 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 337 | `.seance-ai-header > div {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 338 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 339 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 340 | `  gap: 0.75rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 341 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 342 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 343 | `.seance-ai-title {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 344 | `  font-size: 1.02rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 345 | `  font-weight: 800;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 346 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 347 | `  letter-spacing: -0.01em;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 348 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 349 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 350 | `.seance-ai-header p {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 351 | `  max-width: 720px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 352 | `  margin: 0.55rem 0 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 353 | `  color: var(--sl-text3);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 354 | `  font-size: 0.88rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 355 | `  line-height: 1.55;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 356 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 357 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 358 | `.seance-form-badge--ai {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 359 | `  background: linear-gradient(135deg, #2563eb, #7c3aed);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 360 | `  box-shadow: 0 8px 18px rgba(37, 99, 235, 0.18);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 361 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 362 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 363 | `.seance-ai-actions {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 364 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 365 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 366 | `  justify-content: flex-start;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 367 | `  gap: 0.75rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 368 | `  margin-top: 1.15rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 369 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 370 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 371 | `.seance-ai-actions .sl-btn {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 372 | `  min-width: 190px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 373 | `  justify-content: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 374 | `  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.20);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 375 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 376 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 377 | `.seance-ai-message {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 378 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 379 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 380 | `  gap: 0.55rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 381 | `  margin-top: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 382 | `  padding: 0.85rem 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 383 | `  border-radius: 14px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 384 | `  font-weight: 600;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 385 | `  font-size: 0.86rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 386 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 387 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 388 | `.seance-ai-message--warning {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 389 | `  background: #fff7ed;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 390 | `  border: 1px solid rgba(249, 115, 22, 0.24);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 391 | `  color: #9a3412;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 392 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 393 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 394 | `.seance-ai-results-head {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 395 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 396 | `  justify-content: space-between;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 397 | `  align-items: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 398 | `  margin-top: 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 399 | `  padding-top: 1.25rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 400 | `  border-top: 1px solid rgba(148, 163, 184, 0.22);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 401 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 402 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 403 | `.seance-ai-results-head h3 {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 404 | `  margin: 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 405 | `  font-size: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 406 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 407 | `  font-weight: 800;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 408 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 409 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 410 | `.seance-ai-results-head p {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 411 | `  margin: 0.25rem 0 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 412 | `  color: var(--sl-text4);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 413 | `  font-size: 0.84rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 414 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 415 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 416 | `.seance-ai-grid {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 417 | `  display: grid;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 418 | `  grid-template-columns: repeat(auto-fit, minmax(255px, 1fr));` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 419 | `  gap: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 420 | `  margin-top: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 421 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 422 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 423 | `.seance-ai-card {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 424 | `  position: relative;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 425 | `  background: #ffffff;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 426 | `  border: 1px solid rgba(226, 232, 240, 0.95);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 427 | `  border-radius: 18px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 428 | `  padding: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 429 | `  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.06);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 430 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 431 | `  flex-direction: column;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 432 | `  gap: 0.75rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 433 | `  transition: transform 0.18s ease, box-shadow 0.18s ease, border-color 0.18s ease;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 434 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 435 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 436 | `.seance-ai-card:hover {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 437 | `  transform: translateY(-3px);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 438 | `  border-color: rgba(37, 99, 235, 0.30);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 439 | `  box-shadow: 0 16px 30px rgba(15, 23, 42, 0.10);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 440 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 441 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 442 | `.seance-ai-card-top {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 443 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 444 | `  justify-content: space-between;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 445 | `  align-items: flex-start;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 446 | `  gap: 0.75rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 447 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 448 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 449 | `.seance-ai-card h3 {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 450 | `  margin: 0 0 0.5rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 451 | `  color: var(--sl-text);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 452 | `  font-size: 1rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 453 | `  font-weight: 800;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 454 | `  line-height: 1.2;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 455 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 456 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 457 | `.seance-ai-card .seance-param-tag + .seance-param-tag {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 458 | `  margin-left: 0.35rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 459 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 460 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 461 | `.seance-ai-score {` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 462 | `  white-space: nowrap;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 463 | `  background: #dcfce7;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 464 | `  color: #15803d;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 465 | `  border-radius: 999px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 466 | `  padding: 0.26rem 0.58rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 467 | `  font-size: 0.72rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 468 | `  font-weight: 800;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 469 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 470 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 471 | `.seance-ai-desc {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 472 | `  min-height: 42px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 473 | `  margin: 0;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 474 | `  color: var(--sl-text2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 475 | `  font-size: 0.86rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 476 | `  line-height: 1.55;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 477 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 478 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 479 | `.seance-ai-info {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 480 | `  display: flex;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 481 | `  flex-wrap: wrap;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 482 | `  gap: 0.45rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 483 | `  color: var(--sl-text3);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 484 | `  font-size: 0.78rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 485 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 486 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 487 | `.seance-ai-info span {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 488 | `  background: var(--sl-surface2);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 489 | `  border: 1px solid rgba(226, 232, 240, 0.9);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 490 | `  border-radius: 999px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 491 | `  padding: 0.28rem 0.55rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 492 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 493 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 494 | `.seance-ai-reason,` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 495 | `.seance-ai-safety {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 496 | `  color: var(--sl-text3);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 497 | `  font-size: 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 498 | `  line-height: 1.45;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 499 | `  background: #f8fafc;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 500 | `  border: 1px solid rgba(226, 232, 240, 0.9);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 501 | `  border-radius: 12px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 502 | `  padding: 0.7rem 0.8rem;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 503 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 504 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 505 | `.seance-ai-safety {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 506 | `  background: #fff7ed;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 507 | `  border-color: rgba(249, 115, 22, 0.16);` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 508 | `  color: #9a3412;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 509 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 510 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 511 | `.seance-ai-add-btn {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 512 | `  margin-top: auto;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 513 | `  width: 100%;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 514 | `  min-height: 42px;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 515 | `  justify-content: center;` | Propriété CSS : définit l’apparence visuelle comme couleur, taille, marge ou espacement. |
| 516 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 517 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 518 | `@media (max-width: 768px) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 519 | `  .seance-header-actions { width: 100%; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 520 | `  .seance-header-actions .sl-btn { flex: 1; justify-content: center; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 521 | `  .seance-ai-panel { padding: 1rem; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 522 | `  .seance-ai-header { display: block; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 523 | `  .seance-ai-actions .sl-btn { width: 100%; }` | Déclare une classe CSS qui stylise un élément de l’interface. |
| 524 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `seance-details.component.css` améliore l’ergonomie et garde un style cohérent avec le projet, surtout pour les cartes, boutons, badges et la partie IA.