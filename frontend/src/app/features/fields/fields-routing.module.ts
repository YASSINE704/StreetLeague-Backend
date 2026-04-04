import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FieldListComponent } from './field-list/field-list.component';
import { FieldDetailsComponent } from './field-details/field-details.component';
import { CreateFieldComponent } from './create-field/create-field.component';
import { EditFieldComponent } from './edit-field/edit-field.component';

const routes: Routes = [
  { path: '', component: FieldListComponent },
  { path: 'create', component: CreateFieldComponent },
  { path: ':id', component: FieldDetailsComponent },
  { path: ':id/edit', component: EditFieldComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FieldsRoutingModule { }
