import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { PlayerDashboardComponent } from './player-dashboard.component';

const routes: Routes = [
  { path: '', component: PlayerDashboardComponent }
];

@NgModule({
  declarations: [PlayerDashboardComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forChild(routes)
  ]
})
export class PlayerDashboardModule { }
