import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, UserRole } from '../../features/auth/auth.service';
import { NotificationService } from '../../core/services/notification.service';

interface NavLink {
  path: string;
  label: string;
  icon: string;
  section?: string;
}

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit {
  collapsed = false;
  showNotifs = false;
  username = '';
  role: UserRole | null = null;
  navLinks: NavLink[] = [];

  constructor(
    public authService: AuthService,
    public notifService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.username = user?.username || '';
      this.role = user?.role || null;
      this.buildNavLinks();
    });
    this.buildNavLinks();
  }

  private buildNavLinks(): void {
    const links: NavLink[] = [];

    if (this.role === 'ADMIN') {
      links.push(
        { path: '/dashboard', label: 'Dashboard', icon: '📊', section: 'ADMINISTRATION' },
        { path: '/endroits', label: 'Endroits', icon: '📍' },
        { path: '/sous-espaces', label: 'Sous-Espaces', icon: '🏗️' },
        { path: '/tournament', label: 'Tournois', icon: '🏆' }
      );
    }

    if (this.role === 'JOUEUR' || this.role === 'SPORTIF') {
      links.push(
        { path: '/client', label: 'Explorer', icon: '🏟️', section: 'CLIENT' },
        { path: '/client/reservations', label: 'Mes Réservations', icon: '📋' },
        { path: '/player-dashboard', label: 'Mon Profil', icon: '⚽' }
      );
    }

    if (this.role === 'COACH') {
      links.push(
        { path: '/coaching/programmes', label: 'Programmes', icon: '📋', section: 'COACHING' },
        { path: '/coaching/exercices', label: 'Exercices', icon: '💪' }
      );
    }

    if (this.role === 'TERRAIN_MANAGER') {
      links.push(
        { path: '/terrain-manager-dashboard', label: 'Mes Terrains', icon: '🏟️', section: 'TERRAINS' }
      );
    }

    this.navLinks = links;
  }

  get roleLabel(): string {
    const map: Record<string, string> = {
      JOUEUR: 'Joueur', TERRAIN_MANAGER: 'Terrain Manager',
      ADMIN: 'Administrateur', COACH: 'Coach', SPORTIF: 'Sportif'
    };
    return this.role ? (map[this.role] || this.role) : '';
  }

  get avatarInitial(): string {
    return this.username ? this.username.charAt(0).toUpperCase() : 'U';
  }

  get pageTitle(): string {
    const url = this.router.url;
    if (url.includes('/dashboard')) return 'Dashboard';
    if (url.includes('/endroits')) return 'Gestion des Endroits';
    if (url.includes('/sous-espaces')) return 'Sous-Espaces';
    if (url.includes('/client/reservations')) return 'Réservations Client';
    if (url.includes('/client')) return 'Vue Client';
    if (url.includes('/tournament')) return 'Gestion des Tournois';
    if (url.includes('/coaching')) return 'Module Coaching';
    if (url.includes('/player')) return 'Tableau de Bord Joueur';
    if (url.includes('/terrain')) return 'Gestion des Terrains';
    return 'StreetLeague';
  }

  toggleNotifs(): void {
    this.showNotifs = !this.showNotifs;
  }
}
