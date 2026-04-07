import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService, UserRole } from './features/auth/auth.service';

interface NavLink {
  path: string;
  label: string;
  icon: string;
  section?: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'PI_StreetLeague';
  sidebarCollapsed = false;
  isAuthRoute = false;
  userRole: UserRole | null = null;
  username = '';
  
  navLinks: NavLink[] = [];

  constructor(private router: Router, public authService: AuthService) {
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event) => {
        this.updateRouteState(event.urlAfterRedirects);
      });
  }

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.userRole = user.role;
        this.username = user.username;
        this.generateNavLinks(user.role);
      } else {
        this.userRole = null;
        this.username = '';
        this.navLinks = [];
      }
    });
    this.updateRouteState(this.router.url);
  }

  private updateRouteState(url: string) {
    this.isAuthRoute = url.includes('/auth/login') || url.includes('/auth/register') || url === '/' || url === '/auth';
  }

  private generateNavLinks(role: UserRole) {
    const links: NavLink[] = [];
    
    if (role === 'ADMIN') {
      links.push(
        { path: '/tournament', label: 'Tournament Dashboard', icon: '📊', section: 'ADMINISTRATION' }
      );
    } else if (role === 'COACH') {
      links.push(
        { path: '/coaching/dashboard', label: 'Dashboard', icon: '📊', section: 'COACHING' },
        { path: '/coaching/programmes', label: 'Programmes', icon: '📋' },
        { path: '/coaching/exercices', label: 'Exercices', icon: '💪' }
      );
    } else if (role === 'JOUEUR') {
      links.push(
        { path: '/player-dashboard', label: 'Mon Profil', icon: '⚽', section: 'JOUEUR' },
        { path: '/coaching/programmes', label: 'Mes Programmes', icon: '📋' }
      );
    } else if (role === 'TERRAIN_MANAGER') {
      links.push(
        { path: '/terrain-manager-dashboard', label: 'Mes Terrains', icon: '🏟️', section: 'GESTION' }
      );
    }
    
    this.navLinks = links;
  }

  getRoleLabel(): string {
    const labels: Record<UserRole, string> = {
      ADMIN: 'Administrateur',
      COACH: 'Coach',
      JOUEUR: 'Joueur',
      SPORTIF: 'Sportif',
      TERRAIN_MANAGER: 'Terrain Manager'
    };
    return labels[this.userRole!] || '';
  }

  getHeaderTitle(): string {
    if (this.router.url.includes('/tournament')) return 'Gestion des Tournois';
    if (this.router.url.includes('/coaching')) return 'Module Coaching';
    if (this.router.url.includes('/player')) return 'Tableau de Bord Joueur';
    if (this.router.url.includes('/terrain')) return 'Gestion des Terrains';
    return 'StreetLeague Dashboard';
  }
}
