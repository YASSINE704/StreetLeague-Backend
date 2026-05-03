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
export class BasicAuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const credentials = this.authService.getAuthorizationHeader();
    const user = this.authService.currentUser;

    /* Ajoute le header Authorization + X-User-Id pour le module coaching */
    let headers: { [key: string]: string } = {};
    if (credentials) {
      headers['Authorization'] = credentials;
    }
    if (user?.id) {
      headers['X-User-Id'] = String(user.id);
    }

    const authReq = Object.keys(headers).length > 0
      ? req.clone({ setHeaders: headers })
      : req;

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.authService.logout();
          this.router.navigate(['/auth/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
