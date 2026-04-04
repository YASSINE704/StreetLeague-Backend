import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly storageKey = 'streetleague-basic-auth';

  login(email: string, password: string): void {
    const token = btoa(`${email}:${password}`);
    localStorage.setItem(this.storageKey, token);
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.storageKey);
  }

  getAuthorizationHeader(): string | null {
    const token = localStorage.getItem(this.storageKey);
    return token ? `Basic ${token}` : null;
  }
}
