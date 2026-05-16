import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SousEspaceListComponent } from './sous-espace-list/sous-espace-list.component';
import { SousEspaceDetailsComponent } from './sous-espace-details/sous-espace-details.component';

const routes: Routes = [
  { path: '', component: SousEspaceListComponent },
  { path: ':id', component: SousEspaceDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SousEspacesRoutingModule { }
