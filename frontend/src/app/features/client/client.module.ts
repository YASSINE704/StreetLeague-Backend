import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClientRoutingModule } from './client-routing.module';
import { ClientMapComponent } from './client-map/client-map.component';
import { ClientEndroitComponent } from './client-endroit/client-endroit.component';
import { ClientReservationsComponent } from './client-reservations/client-reservations.component';
import { ClientSousEspaceComponent } from './client-sous-espace/client-sous-espace.component';

@NgModule({
  declarations: [ClientMapComponent, ClientEndroitComponent, ClientReservationsComponent, ClientSousEspaceComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, ClientRoutingModule]
})
export class ClientModule { }
