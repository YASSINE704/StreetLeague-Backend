import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-post-create',
  templateUrl: './post-create.component.html',
  styleUrls: ['./post-create.component.css']
})
export class PostCreateComponent {
  title = '';
  content = '';
  tags = '';
  errorMessage = '';
  successMessage = '';
  isLoading = false;

  constructor(
    private postService: PostService,
    private router: Router,
    private authService: AuthService
  ) {}

  onSubmit(): void {
    if (!this.title.trim() || !this.content.trim()) {
      this.errorMessage = 'Le titre et le contenu sont requis.';
      return;
    }

    const currentUser = this.authService.currentUser;
    if (!currentUser) {
      this.errorMessage = 'Vous devez être connecté pour créer un post.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const tagList = this.tags
      .split(',')
      .map(t => t.trim())
      .filter(t => t.length > 0);

    const userId = Number(currentUser.id);

    this.postService.create({
      title: this.title,
      content: this.content,
      user: { idUser: userId },
      tags: tagList.length > 0 ? tagList : undefined
    }).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Post créé avec succès !';
        setTimeout(() => this.router.navigate(['/forum/posts']), 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors de la création du post.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/forum/posts']);
  }
}
