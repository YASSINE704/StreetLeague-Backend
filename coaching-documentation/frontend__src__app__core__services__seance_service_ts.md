# Code hyper commenté — `frontend/src/app/core/services/seance.service.ts`

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
| 4 | `import { SeanceEntrainement } from '../../shared/models/programme-entrainement.model';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `@Injectable({ providedIn: 'root' })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `export class SeanceService {` | Déclare une classe exportée afin qu’elle soit utilisable par Angular ou d’autres fichiers. |
| 8 | `  private apiUrl = 'http://localhost:8080/api/seances';` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `  constructor(private http: HttpClient) {}` | Service Angular utilisé pour envoyer des requêtes HTTP vers le backend. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `  getAll(): Observable<SeanceEntrainement[]> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 13 | `    return this.http.get<SeanceEntrainement[]>(this.apiUrl);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 14 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 15 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 16 | `  getById(id: number): Observable<SeanceEntrainement> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 17 | `    return this.http.get<SeanceEntrainement>(\`${this.apiUrl}/${id}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 18 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `  getByProgramme(programmeId: number): Observable<SeanceEntrainement[]> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 21 | `    return this.http.get<SeanceEntrainement[]>(\`${this.apiUrl}/programme/${programmeId}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 22 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `  create(seance: SeanceEntrainement): Observable<SeanceEntrainement> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 25 | `    return this.http.post<SeanceEntrainement>(this.apiUrl, seance);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 26 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 27 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 28 | `  update(id: number, seance: SeanceEntrainement): Observable<SeanceEntrainement> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 29 | `    return this.http.put<SeanceEntrainement>(\`${this.apiUrl}/${id}\`, seance);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 30 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 31 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 32 | `  delete(id: number): Observable<void> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 33 | `    return this.http.delete<void>(\`${this.apiUrl}/${id}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 34 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 35 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `seance.service.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.