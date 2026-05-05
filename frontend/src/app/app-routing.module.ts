import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  // Auth routes (no sidebar)
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  // Main app routes (with sidebar) — protected by AuthGuard
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent, data: { roles: ['ADMIN'] } },
      {
        path: 'endroits',
        loadChildren: () => import('./features/fields/fields.module').then(m => m.FieldsModule),
        data: { roles: ['ADMIN'] }
      },
      {
        path: 'sous-espaces',
        loadChildren: () => import('./features/sous-espaces/sous-espaces.module').then(m => m.SousEspacesModule),
        data: { roles: ['ADMIN'] }
      },
      {
        path: 'client',
        loadChildren: () => import('./features/client/client.module').then(m => m.ClientModule),
        data: { roles: ['JOUEUR', 'SPORTIF'] }
      },
      {
        path: 'tournament',
        loadChildren: () =>
          import('./features/tournament-dashboard/tournament-dashboard.module').then(m => m.TournamentDashboardModule),
        data: { roles: ['ADMIN'] }
      },
      {
        path: 'player-dashboard',
        loadChildren: () =>
          import('./features/player-dashboard/player-dashboard.module').then(m => m.PlayerDashboardModule),
        data: { roles: ['JOUEUR', 'SPORTIF'] }
      },
      {
        path: 'terrain-manager-dashboard',
        loadChildren: () =>
          import('./features/terrain-manager-dashboard/terrain-manager-dashboard.module').then(m => m.TerrainManagerDashboardModule),
        data: { roles: ['TERRAIN_MANAGER'] }
      },
      {
        path: 'coaching',
        loadChildren: () => import('./features/coaching/coaching.module').then(m => m.CoachingModule),
        data: { roles: ['COACH', 'SPORTIF', 'JOUEUR', 'ADMIN'] }
      },
      { path: '', redirectTo: 'auth/login', pathMatch: 'full' }
    ]
  },
  // Wildcard: redirect unknown routes to login
  { path: '**', redirectTo: 'auth/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
