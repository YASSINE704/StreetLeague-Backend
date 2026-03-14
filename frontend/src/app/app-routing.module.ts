import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'coaching',
    loadChildren: () => import('./features/coaching/coaching.module').then(m => m.CoachingModule)
  },
  { path: '', redirectTo: 'coaching', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
