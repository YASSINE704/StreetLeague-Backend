import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TournamentDashboardRoutingModule } from './tournament-dashboard-routing.module';
import { TournamentDashboardComponent } from './tournament-dashboard.component';

@NgModule({
  declarations: [
    TournamentDashboardComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    TournamentDashboardRoutingModule
  ]
})
export class TournamentDashboardModule { }
