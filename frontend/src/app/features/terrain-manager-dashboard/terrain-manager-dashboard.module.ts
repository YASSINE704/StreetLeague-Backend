import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { TerrainManagerDashboardComponent } from './terrain-manager-dashboard.component';

const routes: Routes = [
  { path: '', component: TerrainManagerDashboardComponent }
];

@NgModule({
  declarations: [TerrainManagerDashboardComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class TerrainManagerDashboardModule { }
