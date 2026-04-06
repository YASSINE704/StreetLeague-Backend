import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { TournamentDashboardRoutingModule } from './tournament-dashboard-routing.module';
import { TournamentDashboardComponent } from './tournament-dashboard.component';

@NgModule({
  declarations: [
    TournamentDashboardComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    TournamentDashboardRoutingModule
  ]
})
export class TournamentDashboardModule { }
