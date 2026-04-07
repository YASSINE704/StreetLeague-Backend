import { Component } from '@angular/core';
import { AuthService } from '../../features/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent {
  constructor(public authService: AuthService, private router: Router) {}

  get username(): string { return this.authService.user?.username || ''; }
  get role(): string { return this.authService.user?.role || ''; }

  get roleLabel(): string {
    const map: Record<string, string> = {
      JOUEUR: 'Joueur',
      TERRAIN_MANAGER: 'Terrain Manager',
      ADMIN: 'Administrateur'
    };
    return map[this.role] || this.role;
  }

  get dashboardLink(): string {
    if (this.role === 'JOUEUR') return '/player-dashboard';
    if (this.role === 'TERRAIN_MANAGER') return '/terrain-manager-dashboard';
    return '/tournament';
  }
}
