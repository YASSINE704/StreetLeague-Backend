# Code hyper commenté — `frontend/src/app/core/services/exercice.service.ts`

## 1. Rôle du fichier

Service Angular : centralise les appels HTTP vers Spring Boot.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **TypeScript / Angular**.
- Utilise Angular, TypeScript, RxJS/HttpClient, et le binding avec le template HTML.

## 3. Comment il communique avec les autres fichiers

- S’intègre dans l’architecture générale du module coaching.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `import { Injectable } from '@angular/core';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 2 | `import { HttpClient } from '@angular/common/http';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 3 | `import { Observable } from 'rxjs';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import { Exercice, SeanceExercice, TypeExercice } from '../../shared/models/programme-entrainement.model';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `@Injectable({ providedIn: 'root' })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `export class ExerciceService {` | Déclare une classe exportée afin qu’elle soit utilisable par Angular ou d’autres fichiers. |
| 8 | `  private apiUrl = 'http://localhost:8080/api/exercices';` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 9 | `  private seanceExerciceUrl = 'http://localhost:8080/api/seance-exercices';` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 10 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 11 | `  constructor(private http: HttpClient) {}` | Service Angular utilisé pour envoyer des requêtes HTTP vers le backend. |
| 12 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 13 | `  // --- Exercice CRUD ---` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 14 | `  getAll(): Observable<Exercice[]> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 15 | `    return this.http.get<Exercice[]>(this.apiUrl);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 16 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `  getById(id: number): Observable<Exercice> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 19 | `    return this.http.get<Exercice>(\`${this.apiUrl}/${id}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 20 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `  getByType(type: TypeExercice): Observable<Exercice[]> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 23 | `    return this.http.get<Exercice[]>(\`${this.apiUrl}/type/${type}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 24 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 25 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 26 | `  create(exercice: Exercice): Observable<Exercice> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 27 | `    return this.http.post<Exercice>(this.apiUrl, exercice);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 28 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 29 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 30 | `  update(id: number, exercice: Exercice): Observable<Exercice> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 31 | `    return this.http.put<Exercice>(\`${this.apiUrl}/${id}\`, exercice);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 32 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 33 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 34 | `  delete(id: number): Observable<void> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 35 | `    return this.http.delete<void>(\`${this.apiUrl}/${id}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 36 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `  // --- SeanceExercice ---` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 39 | `  getBySeance(seanceId: number): Observable<SeanceExercice[]> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 40 | `    return this.http.get<SeanceExercice[]>(\`${this.seanceExerciceUrl}/seance/${seanceId}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 41 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 42 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 43 | `  getByExercice(exerciceId: number): Observable<SeanceExercice[]> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 44 | `    return this.http.get<SeanceExercice[]>(\`${this.seanceExerciceUrl}/exercice/${exerciceId}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 45 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 46 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 47 | `  addToSeance(se: SeanceExercice): Observable<SeanceExercice> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 48 | `    return this.http.post<SeanceExercice>(this.seanceExerciceUrl, se);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 49 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 50 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 51 | `  updateSeanceExercice(id: number, se: SeanceExercice): Observable<SeanceExercice> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 52 | `    return this.http.put<SeanceExercice>(\`${this.seanceExerciceUrl}/${id}\`, se);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 53 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 54 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 55 | `  removeFromSeance(id: number): Observable<void> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 56 | `    return this.http.delete<void>(\`${this.seanceExerciceUrl}/${id}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 57 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 58 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `exercice.service.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.