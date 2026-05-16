import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../../features/auth/auth.service';

@Injectable()
export class JwtAuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const credentials = this.authService.getAuthorizationHeader();
    const isAuthRequest = req.url.includes('/api/auth/');

    let authReq = req;
    if (credentials && !isAuthRequest) {
      const user = this.authService.currentUser;
      const headers: { [key: string]: string } = { Authorization: credentials };
      // Envoyer l'ID utilisateur pour les vérifications de rôle côté backend
      if (user?.id) {
        headers['X-User-Id'] = String(user.id);
      }
      authReq = req.clone({ setHeaders: headers });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !isAuthRequest) {
          this.authService.logout();
          this.router.navigate(['/auth/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
