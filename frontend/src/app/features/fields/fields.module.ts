import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { FieldsRoutingModule } from './fields-routing.module';
import { FieldListComponent } from './field-list/field-list.component';
import { FieldDetailsComponent } from './field-details/field-details.component';
import { CreateFieldComponent } from './create-field/create-field.component';
import { EditFieldComponent } from './edit-field/edit-field.component';

@NgModule({
  declarations: [
    FieldListComponent,
    FieldDetailsComponent,
    CreateFieldComponent,
    EditFieldComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FieldsRoutingModule
  ]
})
export class FieldsModule { }
