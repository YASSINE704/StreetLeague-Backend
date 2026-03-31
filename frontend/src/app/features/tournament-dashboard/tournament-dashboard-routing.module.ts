import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TournamentDashboardComponent } from './tournament-dashboard.component';

const routes: Routes = [
  { path: '', component: TournamentDashboardComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TournamentDashboardRoutingModule { }
