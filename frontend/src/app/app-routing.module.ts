import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'endroits', pathMatch: 'full' },
      {
        path: 'endroits',
        loadChildren: () => import('./features/fields/fields.module').then(m => m.FieldsModule)
      },
      {
        path: 'sous-espaces',
        loadChildren: () => import('./features/sous-espaces/sous-espaces.module').then(m => m.SousEspacesModule)
      },
      {
        path: 'client',
        loadChildren: () => import('./features/client/client.module').then(m => m.ClientModule)
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
