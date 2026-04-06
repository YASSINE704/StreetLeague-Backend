import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

export type UserRole = 'JOUEUR' | 'TERRAIN_MANAGER' | 'ADMIN' | 'COACH';

export interface AuthUser {
  id: string;
  username: string;
  email: string;
  role: UserRole;
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<AuthUser | null>(this.loadUser());
  currentUser$ = this.currentUserSubject.asObservable();
  private readonly storageKey = 'streetleague-basic-auth';

  constructor(private router: Router) {}

  private loadUser(): AuthUser | null {
    try {
      const data = localStorage.getItem('sl_user');
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  }

  get currentUser(): AuthUser | null {
    return this.currentUserSubject.value;
  }

  get user(): AuthUser | null {
    return this.currentUser;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUser;
  }

  get userRole(): UserRole | null {
    return this.currentUser?.role ?? null;
  }

  /**
   * Mock login — replace with real HTTP call when backend is ready.
   */
  login(email: string, password: string, role: UserRole = 'JOUEUR'): boolean {
    // Simulated credential check
    const mockUser: AuthUser = {
      id: crypto.randomUUID(),
      username: email.split('@')[0],
      email,
      role,
      token: btoa(`${email}:${Date.now()}`)
    };
    localStorage.setItem('sl_user', JSON.stringify(mockUser));
    this.currentUserSubject.next(mockUser);
    
    // Basic auth logic from coaching module
    const token = btoa(`${email}:${password}`);
    localStorage.setItem(this.storageKey, token);

    return true;
  }

  /**
   * Mock register — replace with real HTTP call when backend is ready.
   */
  register(username: string, email: string, password: string, role: UserRole): boolean {
    const newUser: AuthUser = {
      id: crypto.randomUUID(),
      username,
      email,
      role,
      token: btoa(`${email}:${Date.now()}`)
    };
    localStorage.setItem('sl_user', JSON.stringify(newUser));
    this.currentUserSubject.next(newUser);
    return true;
  }

  logout(): void {
    localStorage.removeItem('sl_user');
    localStorage.removeItem(this.storageKey);
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  redirectAfterLogin(): void {
    const role = this.userRole;
    if (role === 'ADMIN') {
      this.router.navigate(['/tournament']);
    } else if (role === 'TERRAIN_MANAGER') {
      this.router.navigate(['/terrain-manager-dashboard']);
    } else if (role === 'JOUEUR') {
      this.router.navigate(['/player-dashboard']);
    } else if (role === 'COACH') {
      this.router.navigate(['/coaching/programmes']);
    } else {
      this.router.navigate(['/auth/login']);
    }
  }

  getAuthorizationHeader(): string | null {
    const token = localStorage.getItem(this.storageKey);
    return token ? `Basic ${token}` : null;
  }
}
