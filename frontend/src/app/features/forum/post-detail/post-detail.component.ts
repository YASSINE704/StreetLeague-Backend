import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../core/services/post.service';
import { CommentService } from '../../../core/services/comment.service';
import { ForumAiService } from '../../../core/services/forum-ai.service';
import { PostDTO, CommentDTO } from '../../../shared/models/forum.model';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {
  post: PostDTO | null = null;
  comments: CommentDTO[] = [];
  errorMessage = '';
  successMessage = '';
  isLoadingPost = false;

  newComment = '';
  commentError = '';

  aiReply = '';
  isLoadingAi = false;
  isSendingComment = false;

  private readonly avatarColors = ['#2563eb', '#16a34a', '#dc2626', '#7c3aed', '#d97706', '#0891b2', '#db2777'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private commentService: CommentService,
    private forumAiService: ForumAiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadPost(id);
      this.loadComments(id);
    }
  }

  loadPost(id: number): void {
    this.isLoadingPost = true;
    this.postService.getById(id).subscribe({
      next: (data) => { this.post = data; this.isLoadingPost = false; },
      error: () => {
        this.errorMessage = 'Post introuvable.';
        this.isLoadingPost = false;
        setTimeout(() => this.router.navigate(['/forum/posts']), 2000);
      }
    });
  }

  loadComments(postId: number): void {
    this.commentService.getByPost(postId).subscribe({
      next: (data) => this.comments = data,
      error: () => this.commentError = 'Erreur lors du chargement des commentaires.'
    });
  }

  addComment(): void {
    if (!this.newComment.trim()) return;

    const currentUser = this.authService.currentUser;
    if (!currentUser) {
      this.commentError = 'Vous devez être connecté pour commenter.';
      return;
    }

    this.isSendingComment = true;
    this.commentError = '';

    const userId = Number(currentUser.id);

    this.commentService.create({
      content: this.newComment,
      postId: this.post!.id!,
      user: { idUser: userId }
    }).subscribe({
      next: (comment) => {
        this.comments.push(comment);
        this.newComment = '';
        this.isSendingComment = false;
      },
      error: (err) => {
        this.isSendingComment = false;
        this.commentError = err.error?.message || 'Erreur lors de l\'ajout du commentaire.';
      }
    });
  }

  deleteComment(id: number): void {
    if (confirm('Supprimer ce commentaire ?')) {
      this.commentService.delete(id).subscribe({
        next: () => {
          this.comments = this.comments.filter(c => c.id !== id);
          this.successMessage = 'Commentaire supprimé.';
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (err) => this.commentError = err.error?.message || 'Erreur lors de la suppression.'
      });
    }
  }

  generateAiReply(): void {
    if (!this.post?.id) return;
    this.isLoadingAi = true;
    this.aiReply = '';
    this.forumAiService.getSmartReply(this.post.id).subscribe({
      next: (reply) => {
        this.aiReply = reply;
        this.isLoadingAi = false;
      },
      error: () => {
        this.aiReply = 'Erreur : impossible de contacter l\'assistant IA. Vérifiez qu\'Ollama est lancé.';
        this.isLoadingAi = false;
      }
    });
  }

  useAiReply(): void {
    this.newComment = this.aiReply;
    this.aiReply = '';
  }

  onEdit(): void {
    this.router.navigate(['/forum/posts', this.post!.id, 'edit']);
  }

  onBack(): void {
    this.router.navigate(['/forum/posts']);
  }

  getUserName(user: any): string {
    if (!user) return 'Anonyme';
    return [user.prenom, user.nom].filter(Boolean).join(' ') || 'Anonyme';
  }

  getInitials(user: any): string {
    if (!user) return '?';
    const first = (user.prenom?.[0] || '').toUpperCase();
    const last = (user.nom?.[0] || '').toUpperCase();
    return first + last || '?';
  }

  getAvatarColor(user: any): string {
    const id = user?.idUser || 0;
    return this.avatarColors[id % this.avatarColors.length];
  }

  getTimeAgo(dateString: string): string {
    if (!dateString) return '';
    const now = new Date();
    const date = new Date(dateString);
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    if (diffMins < 1) return 'À l\'instant';
    if (diffMins < 60) return `Il y a ${diffMins} min`;
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return `Il y a ${diffHours}h`;
    const diffDays = Math.floor(diffHours / 24);
    if (diffDays < 30) return `Il y a ${diffDays}j`;
    const diffMonths = Math.floor(diffDays / 30);
    return `Il y a ${diffMonths}mois`;
  }
}
