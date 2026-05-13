# Code hyper commenté — `frontend/src/app/core/services/reservation.service.ts`

## 1. Rôle du fichier

Service Angular : centralise les appels HTTP vers Spring Boot.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **TypeScript / Angular**.
- Utilise Angular, TypeScript, RxJS/HttpClient, et le binding avec le template HTML.

## 3. Comment il communique avec les autres fichiers

- Participe au workflow de réservation et aux contrôles de capacité/doublons/chevauchements.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `import { Injectable } from '@angular/core';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 2 | `import { HttpClient } from '@angular/common/http';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 3 | `import { Observable } from 'rxjs';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import { ReservationSeance } from '../../shared/models/programme-entrainement.model';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 6 | `@Injectable({ providedIn: 'root' })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `export class ReservationService {` | Déclare une classe exportée afin qu’elle soit utilisable par Angular ou d’autres fichiers. |
| 8 | `  private apiUrl = 'http://localhost:8080/api/reservations-seances';` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 9 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 10 | `  constructor(private http: HttpClient) {}` | Service Angular utilisé pour envoyer des requêtes HTTP vers le backend. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `  /** Réserver une place dans une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 13 | `  reserver(reservation: { seanceId: number; modePaiement: string }): Observable<ReservationSeance> {` | Ligne liée à la réservation d’une séance par un sportif. |
| 14 | `    return this.http.post<ReservationSeance>(this.apiUrl, reservation);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 15 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `  /** Annuler une réservation */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 18 | `  annuler(id: number, motif?: string): Observable<ReservationSeance> {` | Ligne liée à la réservation d’une séance par un sportif. |
| 19 | `    return this.http.put<ReservationSeance>(\`${this.apiUrl}/${id}/annuler\`, { motif });` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 20 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `  /** Confirmer une réservation (coach) */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 23 | `  confirmer(id: number): Observable<ReservationSeance> {` | Ligne liée à la réservation d’une séance par un sportif. |
| 24 | `    return this.http.put<ReservationSeance>(\`${this.apiUrl}/${id}/confirmer\`, {});` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 25 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 26 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 27 | `  /** Réservations d'une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 28 | `  getBySeance(seanceId: number): Observable<ReservationSeance[]> {` | Ligne liée à la réservation d’une séance par un sportif. |
| 29 | `    return this.http.get<ReservationSeance[]>(\`${this.apiUrl}/seance/${seanceId}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 30 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 31 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 32 | `  /** Réservations d'un sportif */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 33 | `  getByUser(userId: number): Observable<ReservationSeance[]> {` | Ligne liée à la réservation d’une séance par un sportif. |
| 34 | `    return this.http.get<ReservationSeance[]>(\`${this.apiUrl}/user/${userId}\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 35 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 36 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 37 | `  /** Places restantes pour une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 38 | `  getPlacesRestantes(seanceId: number): Observable<{ placesRestantes: number }> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 39 | `    return this.http.get<{ placesRestantes: number }>(\`${this.apiUrl}/seance/${seanceId}/places\`);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 40 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 41 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `reservation.service.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.