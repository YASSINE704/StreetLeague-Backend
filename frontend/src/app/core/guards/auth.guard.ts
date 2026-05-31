import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService, UserRole } from '../../features/auth/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (!this.authService.isLoggedIn) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    const allowedRoles = route.data['roles'] as UserRole[] | undefined;
    if (allowedRoles && allowedRoles.length > 0) {
      const userRole = this.authService.userRole;
      if (!userRole || !allowedRoles.includes(userRole)) {
        // Redirect to their appropriate dashboard
        this.authService.redirectAfterLogin();
        return false;
      }
    }

    return true;
  }
}
