import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CoachingRoutingModule } from './coaching-routing.module';

// Programmes
import { ProgrammeListComponent } from './programmes/programme-list/programme-list.component';
import { ProgrammeCreateComponent } from './programmes/programme-create/programme-create.component';
import { ProgrammeEditComponent } from './programmes/programme-edit/programme-edit.component';
import { ProgrammeDetailsComponent } from './programmes/programme-details/programme-details.component';

// Séances
import { SeanceListComponent } from './seances/seance-list/seance-list.component';
import { SeanceCreateComponent } from './seances/seance-create/seance-create.component';
import { SeanceEditComponent } from './seances/seance-edit/seance-edit.component';
import { SeanceDetailsComponent } from './seances/seance-details/seance-details.component';

// Exercices
import { ExerciceListComponent } from './exercices/exercice-list/exercice-list.component';
import { ExerciceCreateComponent } from './exercices/exercice-create/exercice-create.component';
import { ExerciceEditComponent } from './exercices/exercice-edit/exercice-edit.component';

// Suivis
import { SuiviCreateComponent } from './suivis/suivi-create/suivi-create.component';

@NgModule({
  declarations: [
    ProgrammeListComponent,
    ProgrammeCreateComponent,
    ProgrammeEditComponent,
    ProgrammeDetailsComponent,
    SeanceListComponent,
    SeanceCreateComponent,
    SeanceEditComponent,
    SeanceDetailsComponent,
    ExerciceListComponent,
    ExerciceCreateComponent,
    ExerciceEditComponent,
    SuiviCreateComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    CoachingRoutingModule
  ]
})
export class CoachingModule {}
