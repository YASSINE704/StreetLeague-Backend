import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostListComponent } from './post-list/post-list.component';
import { PostCreateComponent } from './post-create/post-create.component';
import { PostDetailComponent } from './post-detail/post-detail.component';
import { PostEditComponent } from './post-edit/post-edit.component';

const routes: Routes = [
  { path: 'posts', component: PostListComponent },
  { path: 'posts/create', component: PostCreateComponent },
  { path: 'posts/:id', component: PostDetailComponent },
  { path: 'posts/:id/edit', component: PostEditComponent },
  { path: '', redirectTo: 'posts', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForumRoutingModule { }
