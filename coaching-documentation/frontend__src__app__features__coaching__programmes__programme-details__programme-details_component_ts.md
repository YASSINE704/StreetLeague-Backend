# Code hyper commenté — `frontend/src/app/features/coaching/programmes/programme-details/programme-details.component.ts`

## 1. Rôle du fichier

Composant Angular TypeScript : logique de la page.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **TypeScript / Angular**.
- Utilise Angular, TypeScript, RxJS/HttpClient, et le binding avec le template HTML.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Appelé par Angular Router lorsque l’utilisateur ouvre la page correspondante.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `import { Component, OnInit } from '@angular/core';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 2 | `import { ActivatedRoute, Router } from '@angular/router';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 3 | `import { ProgrammeService } from '../../../../core/services/programme.service';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import { SeanceService } from '../../../../core/services/seance.service';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import jsPDF from 'jspdf';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import autoTable from 'jspdf-autotable';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 9 | `@Component({` | Déclare un composant Angular avec son selector, son HTML et son CSS. |
| 10 | `  selector: 'app-programme-details',` | Nom de balise Angular permettant d’utiliser ce composant dans un template. |
| 11 | `  templateUrl: './programme-details.component.html',` | Indique le fichier HTML associé au composant. |
| 12 | `  styleUrls: ['./programme-details.component.css']` | Indique le fichier CSS associé au composant. |
| 13 | `})` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `export class ProgrammeDetailsComponent implements OnInit {` | Indique que le composant exécute une logique au chargement via ngOnInit(). |
| 15 | `  programme!: ProgrammeEntrainement;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `  errorMessage = '';` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `  constructor(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `    private programmeService: ProgrammeService,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 20 | `    private seanceService: SeanceService,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `    private route: ActivatedRoute,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `    private router: Router` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `  ) {}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `  ngOnInit(): void {` | Méthode Angular appelée automatiquement au chargement du composant. |
| 26 | `    this.loadProgramme();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 28 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 29 | `  loadProgramme(): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 30 | `    const id = Number(this.route.snapshot.paramMap.get('id'));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `    this.programmeService.getById(id).subscribe({` | Souscrit à une requête HTTP asynchrone : Angular attend la réponse du backend. |
| 32 | `      next: (data) => this.programme = data,` | Bloc exécuté si l’appel HTTP réussit. |
| 33 | `      error: () => this.errorMessage = 'Erreur lors du chargement du programme'` | Bloc exécuté si l’appel HTTP échoue. |
| 34 | `    });` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 36 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 37 | `  onAddSeance(): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 38 | `    this.router.navigate(['/coaching/seances/create', this.programme.idProgramme]);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 39 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 40 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 41 | `  onSeanceDetails(seanceId: number): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 42 | `    this.router.navigate(['/coaching/seances', seanceId]);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 43 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 44 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 45 | `  onEditSeance(seanceId: number): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 46 | `    this.router.navigate(['/coaching/seances/edit', seanceId], {` | Ligne liée aux rôles et aux permissions utilisateur. |
| 47 | `      queryParams: { programmeId: this.programme.idProgramme }` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `    });` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 50 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 51 | `  onDeleteSeance(seanceId: number): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 52 | `    if (!confirm('Supprimer cette séance du programme ?')) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 53 | `      return;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 55 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 56 | `    this.seanceService.delete(seanceId).subscribe({` | Souscrit à une requête HTTP asynchrone : Angular attend la réponse du backend. |
| 57 | `      next: () => this.loadProgramme(),` | Bloc exécuté si l’appel HTTP réussit. |
| 58 | `      error: (err) => this.errorMessage = err.error?.message \|\| 'Erreur lors de la suppression de la séance'` | Bloc exécuté si l’appel HTTP échoue. |
| 59 | `    });` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 61 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 62 | `  onBack(): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 63 | `    this.router.navigate(['/coaching/programmes']);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 64 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 65 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 66 | `  exportPDF(): void {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 67 | `    const doc = new jsPDF();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 68 | `    const p = this.programme;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 70 | `    // Helper to strip problematic characters` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 71 | `    const clean = (s: string): string => {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 72 | `      return s` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 73 | `        .replace(/→/g, '->')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 74 | `        .replace(/é/g, 'e').replace(/è/g, 'e').replace(/ê/g, 'e').replace(/ë/g, 'e')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `        .replace(/à/g, 'a').replace(/â/g, 'a')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `        .replace(/ù/g, 'u').replace(/û/g, 'u')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 77 | `        .replace(/ô/g, 'o').replace(/î/g, 'i').replace(/ï/g, 'i')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 78 | `        .replace(/ç/g, 'c')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `        .replace(/É/g, 'E').replace(/È/g, 'E').replace(/Ê/g, 'E')` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 80 | `        .replace(/À/g, 'A').replace(/Ç/g, 'C');` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `    };` | Fin d’un bloc de code ou d’un élément HTML. |
| 82 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 83 | `    // Header bar` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 84 | `    doc.setFillColor(37, 99, 235);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 85 | `    doc.rect(0, 0, 210, 40, 'F');` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 86 | `    doc.setFontSize(22);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 87 | `    doc.setTextColor(255, 255, 255);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 88 | `    doc.text(clean(p.titre), 14, 18);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 89 | `    doc.setFontSize(11);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 90 | `    doc.text(\`Statut: ${p.statut}  \|  ${clean(p.dateDebut + ' -> ' + p.dateFin)}\`, 14, 28);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 91 | `    if (p.description) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 92 | `      doc.text(clean(p.description), 14, 35);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 93 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 94 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 95 | `    // Body` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 96 | `    let y = 52;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 97 | `    doc.setTextColor(30, 41, 59);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 98 | `    doc.setFontSize(16);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 99 | `    doc.text('Seances du programme', 14, y);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 100 | `    y += 8;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 101 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 102 | `    if (p.seances && p.seances.length > 0) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 103 | `      const rows = p.seances.map(s => [` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 104 | `        clean(s.titreSeance),` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 105 | `        s.dateSeance \|\| '',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 106 | `        \`${s.dureeMinutes \|\| 0} min\`,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 107 | `        s.intensite \|\| '',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 108 | `        s.statut \|\| '',` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 109 | `        \`${s.exercices?.length \|\| 0} ex.\`` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 110 | `      ]);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 111 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 112 | `      autoTable(doc, {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 113 | `        startY: y,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 114 | `        head: [['Titre', 'Date', 'Duree', 'Intensite', 'Statut', 'Exercices']],` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 115 | `        body: rows,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 116 | `        styles: { fontSize: 9, cellPadding: 4, lineColor: [226, 232, 240], lineWidth: 0.5 },` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 117 | `        headStyles: { fillColor: [37, 99, 235], textColor: 255, fontStyle: 'bold', cellPadding: 5 },` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 118 | `        alternateRowStyles: { fillColor: [248, 250, 252] },` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 119 | `        margin: { left: 14, right: 14 }` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 120 | `      });` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 121 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 122 | `      // Footer` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 123 | `      const finalY = (doc as any).lastAutoTable?.finalY \|\| y + 40;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 124 | `      doc.setFontSize(8);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 125 | `      doc.setTextColor(148, 163, 184);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 126 | `      doc.text(\`Genere le ${new Date().toLocaleDateString('fr-FR')} - StreetLeague Coaching\`, 14, finalY + 12);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 127 | `    } else {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 128 | `      doc.setFontSize(10);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 129 | `      doc.setTextColor(148, 163, 184);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 130 | `      doc.text('Aucune seance dans ce programme', 14, y + 6);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 131 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 132 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 133 | `    doc.save(\`programme-${p.titre.replace(/\s+/g, '-').toLowerCase()}.pdf\`);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 134 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 135 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `programme-details.component.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.