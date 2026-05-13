# Code hyper commenté — `frontend/src/app/core/interceptors/basic-auth.interceptor.ts`

## 1. Rôle du fichier

Interceptor Angular : ajoute automatiquement des informations aux requêtes HTTP.

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
| 2 | `import {` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 3 | `  HttpEvent,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 4 | `  HttpHandler,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 5 | `  HttpInterceptor,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 6 | `  HttpRequest,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 7 | `  HttpErrorResponse` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 8 | `} from '@angular/common/http';` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 9 | `import { Observable, throwError } from 'rxjs';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import { catchError } from 'rxjs/operators';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import { Router } from '@angular/router';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import { AuthService } from '../../features/auth/auth.service';` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 14 | `@Injectable()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 15 | `export class BasicAuthInterceptor implements HttpInterceptor {` | Déclare une classe exportée afin qu’elle soit utilisable par Angular ou d’autres fichiers. |
| 16 | `  constructor(private authService: AuthService, private router: Router) {}` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {` | Bloc exécuté si l’appel HTTP réussit. |
| 19 | `    const credentials = this.authService.getAuthorizationHeader();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `    const user = this.authService.currentUser;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `    /* Ajoute le header Authorization + X-User-Id pour le module coaching */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 23 | `    let headers: { [key: string]: string } = {};` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 24 | `    if (credentials) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 25 | `      headers['Authorization'] = credentials;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 27 | `    if (user?.id) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 28 | `      headers['X-User-Id'] = String(user.id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 30 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 31 | `    const authReq = Object.keys(headers).length > 0` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 32 | `      ? req.clone({ setHeaders: headers })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `      : req;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 35 | `    return next.handle(authReq).pipe(` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 36 | `      catchError((error: HttpErrorResponse) => {` | Bloc exécuté si l’appel HTTP échoue. |
| 37 | `        if (error.status === 401) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 38 | `          this.authService.logout();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `          this.router.navigate(['/auth/login']);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 41 | `        return throwError(() => error);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 42 | `      })` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `    );` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 44 | `  }` | Fin d’un bloc de code ou d’un élément HTML. |
| 45 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `basic-auth.interceptor.ts` contient la logique Angular. Il récupère les données via les services, gère les actions utilisateur et met à jour l’interface sans toucher directement à la base.