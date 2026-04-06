import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, throwError } from 'rxjs';

export type UserRole = 'JOUEUR' | 'TERRAIN_MANAGER' | 'ADMIN' | 'COACH' | 'SPORTIF';

export interface AuthUser {
  id: string | number;
  username: string;
  email: string;
  role: UserRole;
  token: string;
}

interface LoginResponse {
  id: number;
  email: string;
  nom: string;
  prenom: string;
  role: string;
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API = 'http://localhost:8080/api/auth';
  private readonly storageKey = 'streetleague-basic-auth';
  private readonly userKey = 'sl_user';

  private currentUserSubject = new BehaviorSubject<AuthUser | null>(this.loadUser());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private router: Router, private http: HttpClient) {}

  private loadUser(): AuthUser | null {
    try {
      const data = localStorage.getItem(this.userKey);
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
   * Real login — calls backend POST /api/auth/login
   * Maps backend roles (ADMIN, COACH, SPORTIF) to frontend UserRole
   */
  loginWithBackend(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API}/login`, { email, password }).pipe(
      tap((res) => {
        // Map backend role to frontend UserRole
        const roleMap: Record<string, UserRole> = {
          ADMIN: 'ADMIN',
          COACH: 'COACH',
          SPORTIF: 'JOUEUR'
        };
        const role: UserRole = roleMap[res.role] ?? 'JOUEUR';

        const user: AuthUser = {
          id: res.id,
          username: `${res.prenom} ${res.nom}`,
          email: res.email,
          role,
          token: res.token
        };

        localStorage.setItem(this.userKey, JSON.stringify(user));
        localStorage.setItem(this.storageKey, res.token);
        this.currentUserSubject.next(user);
      }),
      catchError((err) => throwError(() => err))
    );
  }

  /**
   * Fallback mock login (kept for JOUEUR / TERRAIN_MANAGER roles not yet in backend)
   */
  login(email: string, password: string, role: UserRole = 'JOUEUR'): boolean {
    const mockUser: AuthUser = {
      id: crypto.randomUUID(),
      username: email.split('@')[0],
      email,
      role,
      token: btoa(`${email}:${Date.now()}`)
    };
    localStorage.setItem(this.userKey, JSON.stringify(mockUser));
    localStorage.setItem(this.storageKey, btoa(`${email}:${password}`));
    this.currentUserSubject.next(mockUser);
    return true;
  }

  register(username: string, email: string, password: string, role: UserRole): boolean {
    const newUser: AuthUser = {
      id: crypto.randomUUID(),
      username,
      email,
      role,
      token: btoa(`${email}:${Date.now()}`)
    };
    localStorage.setItem(this.userKey, JSON.stringify(newUser));
    this.currentUserSubject.next(newUser);
    return true;
  }

  logout(): void {
    localStorage.removeItem(this.userKey);
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
    } else if (role === 'COACH' || role === 'SPORTIF') {
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
