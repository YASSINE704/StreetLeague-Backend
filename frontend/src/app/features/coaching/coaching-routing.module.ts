import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProgrammeListComponent } from './programmes/programme-list/programme-list.component';
import { ProgrammeCreateComponent } from './programmes/programme-create/programme-create.component';
import { ProgrammeEditComponent } from './programmes/programme-edit/programme-edit.component';
import { ProgrammeDetailsComponent } from './programmes/programme-details/programme-details.component';
import { SeanceListComponent } from './seances/seance-list/seance-list.component';
import { SeanceCreateComponent } from './seances/seance-create/seance-create.component';
import { SeanceEditComponent } from './seances/seance-edit/seance-edit.component';
import { SeanceDetailsComponent } from './seances/seance-details/seance-details.component';
import { ExerciceListComponent } from './exercices/exercice-list/exercice-list.component';
import { ExerciceCreateComponent } from './exercices/exercice-create/exercice-create.component';
import { ExerciceEditComponent } from './exercices/exercice-edit/exercice-edit.component';
import { SuiviCreateComponent } from './suivis/suivi-create/suivi-create.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  // Dashboard
  { path: 'dashboard', component: DashboardComponent },
  // Programmes
  { path: 'programmes', component: ProgrammeListComponent },
  { path: 'programmes/create', component: ProgrammeCreateComponent },
  { path: 'programmes/edit/:id', component: ProgrammeEditComponent },
  { path: 'programmes/:id', component: ProgrammeDetailsComponent },
  // Séances
  { path: 'seances', component: SeanceListComponent },
  { path: 'seances/create', component: SeanceCreateComponent },
  { path: 'seances/create/:programmeId', component: SeanceCreateComponent },
  { path: 'seances/edit/:id', component: SeanceEditComponent },
  { path: 'seances/:id', component: SeanceDetailsComponent },
  // Exercices
  { path: 'exercices', component: ExerciceListComponent },
  { path: 'exercices/create', component: ExerciceCreateComponent },
  { path: 'exercices/edit/:id', component: ExerciceEditComponent },
  // Suivi
  { path: 'suivis/create/:seanceId', component: SuiviCreateComponent },
  // Default
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoachingRoutingModule {}
