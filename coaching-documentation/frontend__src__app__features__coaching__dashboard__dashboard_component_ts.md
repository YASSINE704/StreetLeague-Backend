# Code hyper commenté — `frontend/src/app/features/coaching/dashboard/dashboard.component.ts`

## 1. Rôle du fichier

Composant Angular TypeScript : logique de la page.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **TypeScript / Angular**.
- Utilise Angular, TypeScript, RxJS/HttpClient, et le binding avec le template HTML.

## 3. Comment il communique avec les autres fichiers

- Appelé par Angular Router lorsque l’utilisateur ouvre la page correspondante.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `import { Component, OnInit } from '@angular/core';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 2 | `import { ProgrammeService } from '../../../core/services/programme.service';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 3 | `import { SeanceService } from '../../../core/services/seance.service';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import { ExerciceService } from '../../../core/services/exercice.service';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import { ProgrammeEntrainement, SeanceEntrainement, Exercice } from '../../../shared/models/programme-entrainement.model';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 7 | `@Component({` | Déclare un composant Angular avec son selector, son HTML et son CSS. |
| 8 | `  selector: 'app-dashboard',` | Nom de balise Angular permettant d’utiliser ce composant dans un template. |
| 9 | `  templateUrl: './dashboard.component.html',` | Indique le fichier HTML associé au composant. |
| 10 | `  styleUrls: ['./dashboard.component.css']` | Indique le fichier CSS associé au composant. |
| 11 | `})` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 12 | `export class DashboardComponent implements OnInit {` | Indique que le composant exécute une logique au chargement via ngOnInit(). |
| 13 | `  programmes: ProgrammeEntrainement[] = [];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `  seances: SeanceEntrainement[] = [];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `  exercices: Exercice[] = [];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `  constructor(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 18 | `    private programmeService: ProgrammeService,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `    private seanceService: SeanceService,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `    private exerciceService: ExerciceService` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `  ) {}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 23 | `  ngOnInit(): void {` | Méthode Angular appelée automatiquement au chargement du composant. |
| 24 | `    this.programmeService.getAll().subscribe({ next: d => this.programmes = d });` | Souscrit à une requête HTTP asynchrone : Angular attend la réponse du backend. |
| 25 | `    this.seanceService.getAll().subscribe({ next: d => this.seances = d });` | Souscrit à une requête HTTP asynchrone : Angular attend la réponse du backend. |
| 26 | `    this.exerciceService.getAll().subscribe({ next: d => this.exercices = d });` | Souscrit à une requête HTTP asynchrone : Angular attend la réponse du backend. |
| 27 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 28 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 29 | `  get totalProgrammes(): number { return this.programmes.length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 30 | `  get programmesActifs(): number { return this.programmes.filter(p => p.statut === 'ACTIF').length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 31 | `  get totalSeances(): number { return this.seances.length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 32 | `  get seancesRealisees(): number { return this.seances.filter(s => s.statut === 'REALISEE').length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 33 | `  get seancesPrevues(): number { return this.seances.filter(s => s.statut === 'PREVUE').length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 34 | `  get totalExercices(): number { return this.exercices.length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 35 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 36 | `  get tauxCompletion(): number {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 37 | `    if (this.totalSeances === 0) return 0;` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 38 | `    return Math.round((this.seancesRealisees / this.totalSeances) * 100);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 39 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 40 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 41 | `  get moyenneRessenti(): string {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 42 | `    const suivis = this.seances.filter(s => s.suiviSeance).map(s => s.suiviSeance!.ressenti!);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `    if (suivis.length === 0) return '—';` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 44 | `    return (suivis.reduce((a, b) => a + b, 0) / suivis.length).toFixed(1);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 45 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 46 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 47 | `  get moyenneFatigue(): string {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 48 | `    const suivis = this.seances.filter(s => s.suiviSeance).map(s => s.suiviSeance!.fatigue!);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `    if (suivis.length === 0) return '—';` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 50 | `    return (suivis.reduce((a, b) => a + b, 0) / suivis.length).toFixed(1);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 51 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 52 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 53 | `  get recentSeances(): SeanceEntrainement[] {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 54 | `    return [...this.seances].sort((a, b) => (b.dateSeance \|\| '').localeCompare(a.dateSeance \|\| '')).slice(0, 5);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 55 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `  get recentProgrammes(): ProgrammeEntrainement[] {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 58 | `    return [...this.programmes].slice(0, 3);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 59 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 60 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 61 | `  get seancesAnnulees(): number { return this.seances.filter(s => s.statut === 'ANNULEE').length; }` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 62 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 63 | `  get exercicesByType(): { type: string; count: number; icon: string; color: string }[] {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 64 | `    const types = ['FORCE', 'CARDIO', 'MOBILITE', 'TECHNIQUE'];` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `    const icons: any = { FORCE: '🏋️', CARDIO: '🏃', MOBILITE: '🧘', TECHNIQUE: '⚡' };` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 66 | `    const colors: any = { FORCE: '#dc2626', CARDIO: '#16a34a', MOBILITE: '#7c3aed', TECHNIQUE: '#2563eb' };` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `    return types.map(t => ({` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 68 | `      type: t,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `      count: this.exercices.filter(e => e.type === t).length,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 70 | `      icon: icons[t],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `      color: colors[t]` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `    })).filter(t => t.count > 0);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 73 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 74 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `dashboard.component.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.