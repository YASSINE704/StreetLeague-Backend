# Code hyper commenté — `frontend/src/app/core/services/ai-recommendation.service.ts`

## 1. Rôle du fichier

Service Angular : centralise les appels HTTP vers Spring Boot.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **TypeScript / Angular**.
- Utilise Angular, TypeScript, RxJS/HttpClient, et le binding avec le template HTML.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `import { Injectable } from '@angular/core';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 2 | `import { HttpClient } from '@angular/common/http';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 3 | `import { Observable } from 'rxjs';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import { TypeExercice } from '../../shared/models/programme-entrainement.model';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `export interface AIRecommendationRequest {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 7 | `  objectifProgramme: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `  typeSeance: TypeExercice \| string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 9 | `  intensite: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 10 | `  nbParticipants: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 11 | `  dureeSeanceMinutes: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 12 | `  niveauJoueurs?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 13 | `  lieuType?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 14 | `  enPleinAir?: boolean;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `export interface AIExerciseRecommendation {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 18 | `  nom: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `  type: TypeExercice \| string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `  description?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `  difficulte?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `  dureeMinutes?: number;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 23 | `  equipement?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `  objectif?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 25 | `  niveauRecommande?: string;` | Ligne liée à la recommandation IA d’exercices. |
| 26 | `  consigneSecurite?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `  scoreRelevance?: number;` | Ligne liée au calcul ou à l’affichage du score de pertinence IA. |
| 28 | `  raison?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `export interface AIRecommendationResponse {` | Déclare la forme d’un objet TypeScript échangé entre frontend et backend. |
| 32 | `  status: 'ok' \| 'fallback' \| string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `  message?: string;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `  nbRecommandations: number;` | Ligne liée à la recommandation IA d’exercices. |
| 35 | `  recommandations: AIExerciseRecommendation[];` | Ligne liée à la recommandation IA d’exercices. |
| 36 | `}` | Fin d’un bloc de code ou d’un élément HTML. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `@Injectable({ providedIn: 'root' })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `export class AiRecommendationService {` | Déclare une classe exportée afin qu’elle soit utilisable par Angular ou d’autres fichiers. |
| 40 | `  private apiUrl = 'http://localhost:8080/api/coaching/ai';` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 41 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 42 | `  constructor(private http: HttpClient) {}` | Service Angular utilisé pour envoyer des requêtes HTTP vers le backend. |
| 43 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 44 | `  health(): Observable<{ available: boolean; message: string }> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 45 | `    return this.http.get<{ available: boolean; message: string }>(\`${this.apiUrl}/health\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 46 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 47 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 48 | `  recommend(context: AIRecommendationRequest): Observable<AIRecommendationResponse> {` | Ligne liée à la recommandation IA d’exercices. |
| 49 | `    return this.http.post<AIRecommendationResponse>(\`${this.apiUrl}/recommend\`, context);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 50 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 51 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ai-recommendation.service.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.