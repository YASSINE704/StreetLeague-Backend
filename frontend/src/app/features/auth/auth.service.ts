import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';

export type UserRole = 'JOUEUR' | 'TERRAIN_MANAGER' | 'ADMIN' | 'COACH' | 'SPORTIF';

export interface AuthUser {
  id: string | number;
  username: string;
  email: string;
  role: UserRole;
  token: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
  errors?: Record<string, string>;
}

export interface BackendUser {
  id: number;
  email: string;
  nom: string;
  prenom: string;
  role: UserRole;
  emailVerified: boolean;
}

export interface AuthPayload {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
  user: BackendUser;
}

export interface RegisterPayload {
  prenom: string;
  nom: string;
  email: string;
  password: string;
  role: UserRole | 'PLAYER';
  age?: number;
  niveau?: string;
  position?: string;
  profilePicture?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'streetleague-jwt';
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

  login(email: string, password: string): Observable<AuthPayload> {
    return this.http.post<ApiResponse<AuthPayload>>(`${this.API}/login`, { email, password }).pipe(
      map((response) => response.data),
      tap((payload) => this.storeSession(payload))
    );
  }

  register(payload: RegisterPayload): Observable<BackendUser> {
    return this.http.post<ApiResponse<BackendUser>>(`${this.API}/register`, payload).pipe(
      map((response) => response.data)
    );
  }

  verifyEmail(email: string, code: string): Observable<BackendUser> {
    return this.http.post<ApiResponse<BackendUser>>(`${this.API}/verify-email`, { email, code }).pipe(
      map((response) => response.data)
    );
  }

  resendVerification(email: string): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.API}/resend-verification`, { email });
  }

  forgotPassword(email: string): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.API}/forgot-password`, { email });
  }

  resetPassword(email: string, code: string, newPassword: string): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.API}/reset-password`, { email, code, newPassword });
  }

  logout(): void {
    localStorage.removeItem(this.userKey);
    localStorage.removeItem(this.tokenKey);
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  redirectAfterLogin(): void {
    const role = this.userRole;
    if (role === 'ADMIN') {
      this.router.navigate(['/tournament']);
    } else if (role === 'TERRAIN_MANAGER') {
      this.router.navigate(['/terrain-manager-dashboard']);
    } else if (role === 'COACH' || role === 'SPORTIF') {
      this.router.navigate(['/coaching']);
    } else if (role === 'JOUEUR') {
      this.router.navigate(['/player-dashboard']);
    } else {
      this.router.navigate(['/auth/login']);
    }
  }

  getAuthorizationHeader(): string | null {
    const token = localStorage.getItem(this.tokenKey);
    return token ? `Bearer ${token}` : null;
  }

  getErrorMessage(error: any, fallback = 'Something went wrong. Please try again.'): string {
    if (error?.error?.message) {
      return error.error.message;
    }
    if (error?.error?.errors) {
      return Object.values(error.error.errors).join(' ');
    }
    return fallback;
  }

  private storeSession(payload: AuthPayload): void {
    const user: AuthUser = {
      id: payload.user.id,
      username: `${payload.user.prenom} ${payload.user.nom}`.trim(),
      email: payload.user.email,
      role: payload.user.role,
      token: payload.token
    };

    localStorage.setItem(this.userKey, JSON.stringify(user));
    localStorage.setItem(this.tokenKey, payload.token);
    this.currentUserSubject.next(user);
  }
}
