import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SousEspacesRoutingModule } from './sous-espaces-routing.module';
import { SousEspaceListComponent } from './sous-espace-list/sous-espace-list.component';
import { SousEspaceDetailsComponent } from './sous-espace-details/sous-espace-details.component';

@NgModule({
  declarations: [SousEspaceListComponent, SousEspaceDetailsComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, SousEspacesRoutingModule]
})
export class SousEspacesModule { }
