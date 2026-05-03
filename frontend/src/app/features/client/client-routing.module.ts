import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientMapComponent } from './client-map/client-map.component';
import { ClientEndroitComponent } from './client-endroit/client-endroit.component';
import { ClientReservationsComponent } from './client-reservations/client-reservations.component';
import { ClientSousEspaceComponent } from './client-sous-espace/client-sous-espace.component';

const routes: Routes = [
  { path: '', component: ClientMapComponent },
  { path: 'reservations', component: ClientReservationsComponent },
  { path: 'sous-espace/:id', component: ClientSousEspaceComponent },
  { path: ':id', component: ClientEndroitComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
