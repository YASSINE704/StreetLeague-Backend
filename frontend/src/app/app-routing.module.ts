import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'tournament',
    loadChildren: () =>
      import('./features/tournament-dashboard/tournament-dashboard.module').then(
        m => m.TournamentDashboardModule
      )
  },
  { path: '', redirectTo: 'tournament', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
