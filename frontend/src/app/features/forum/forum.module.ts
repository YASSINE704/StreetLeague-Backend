import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ForumRoutingModule } from './forum-routing.module';
import { PostListComponent } from './post-list/post-list.component';
import { PostCreateComponent } from './post-create/post-create.component';
import { PostDetailComponent } from './post-detail/post-detail.component';
import { PostEditComponent } from './post-edit/post-edit.component';

@NgModule({
  declarations: [
    PostListComponent,
    PostCreateComponent,
    PostDetailComponent,
    PostEditComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ForumRoutingModule
  ]
})
export class ForumModule { }
