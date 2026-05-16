import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../core/services/post.service';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-edit.component.html',
  styleUrls: ['./post-edit.component.css']
})
export class PostEditComponent implements OnInit {
  title = '';
  content = '';
  tags = '';
  errorMessage = '';
  successMessage = '';
  isLoading = false;
  postId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.postId) {
      this.loadPost();
    }
  }

  loadPost(): void {
    this.postService.getById(this.postId).subscribe({
      next: (post) => {
        this.title = post.title;
        this.content = post.content;
        this.tags = (post.tags || []).join(', ');
      },
      error: () => {
        this.errorMessage = 'Post introuvable.';
        setTimeout(() => this.router.navigate(['/forum/posts']), 2000);
      }
    });
  }

  onSubmit(): void {
    if (!this.title.trim() || !this.content.trim()) {
      this.errorMessage = 'Le titre et le contenu sont requis.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const tagList = this.tags
      .split(',')
      .map(t => t.trim())
      .filter(t => t.length > 0);

    this.postService.update(this.postId, {
      title: this.title,
      content: this.content,
      tags: tagList.length > 0 ? tagList : undefined
    }).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Post mis à jour avec succès !';
        setTimeout(() => this.router.navigate(['/forum/posts', this.postId]), 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/forum/posts', this.postId]);
  }
}
