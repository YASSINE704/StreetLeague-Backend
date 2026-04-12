import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';

const routes: Routes = [
  // Auth routes (no sidebar)
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  // Main app routes (with sidebar)
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'endroits',
        loadChildren: () => import('./features/fields/fields.module').then(m => m.FieldsModule)
      },
      {
        path: 'sous-espaces',
        loadChildren: () => import('./features/sous-espaces/sous-espaces.module').then(m => m.SousEspacesModule)
      },
      {
        path: 'client',
        loadChildren: () => import('./features/client/client.module').then(m => m.ClientModule)
      },
      {
        path: 'tournament',
        loadChildren: () =>
          import('./features/tournament-dashboard/tournament-dashboard.module').then(m => m.TournamentDashboardModule)
      },
      {
        path: 'player-dashboard',
        loadChildren: () =>
          import('./features/player-dashboard/player-dashboard.module').then(m => m.PlayerDashboardModule)
      },
      {
        path: 'terrain-manager-dashboard',
        loadChildren: () =>
          import('./features/terrain-manager-dashboard/terrain-manager-dashboard.module').then(m => m.TerrainManagerDashboardModule)
      },
      {
        path: 'coaching',
        loadChildren: () => import('./features/coaching/coaching.module').then(m => m.CoachingModule)
      }
    ]
  },
  // Default: go to login
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: '**', redirectTo: 'auth/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
