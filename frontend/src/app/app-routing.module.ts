import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'tournament',
    loadChildren: () =>
      import('./features/tournament-dashboard/tournament-dashboard.module').then(
        m => m.TournamentDashboardModule
      )
  },
  {
    path: 'player-dashboard',
    loadChildren: () =>
      import('./features/player-dashboard/player-dashboard.module').then(
        m => m.PlayerDashboardModule
      )
  },
  {
    path: 'terrain-manager-dashboard',
    loadChildren: () =>
      import('./features/terrain-manager-dashboard/terrain-manager-dashboard.module').then(
        m => m.TerrainManagerDashboardModule
      )
  },
  {
    path: 'coaching',
    loadChildren: () => import('./features/coaching/coaching.module').then(m => m.CoachingModule)
  },
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
