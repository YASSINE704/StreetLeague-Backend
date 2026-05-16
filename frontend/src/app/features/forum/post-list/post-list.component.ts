import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../../core/services/post.service';
import { PostDTO } from '../../../shared/models/forum.model';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {
  posts: PostDTO[] = [];
  filtered: PostDTO[] = [];
  errorMessage = '';
  successMessage = '';

  searchTerm = '';

  sortField = 'createdAt';
  sortDir: 'asc' | 'desc' = 'desc';

  page = 1;
  pageSize = 10;

  sortOptions = [
    { value: 'createdAt,desc', label: 'Plus récents' },
    { value: 'createdAt,asc', label: 'Plus anciens' },
    { value: 'title,asc', label: 'Titre (A-Z)' },
    { value: 'title,desc', label: 'Titre (Z-A)' },
  ];

  private readonly avatarColors = ['#2563eb', '#16a34a', '#dc2626', '#7c3aed', '#d97706', '#0891b2', '#db2777'];

  constructor(private postService: PostService, private router: Router) {}

  ngOnInit(): void { this.loadPosts(); }

  loadPosts(): void {
    this.errorMessage = '';
    this.postService.getAll().subscribe({
      next: (data) => { this.posts = data; this.applyFilters(); },
      error: () => this.errorMessage = 'Erreur de connexion au serveur. Vérifiez que le backend est lancé.'
    });
  }

  applyFilters(): void {
    let result = [...this.posts];
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p =>
        p.title.toLowerCase().includes(term) ||
        p.content.toLowerCase().includes(term)
      );
    }
    result.sort((a, b) => {
      const valA = (a as any)[this.sortField] || '';
      const valB = (b as any)[this.sortField] || '';
      const cmp = valA > valB ? 1 : valA < valB ? -1 : 0;
      return this.sortDir === 'asc' ? cmp : -cmp;
    });
    this.filtered = result;
    this.page = 1;
  }

  get paged(): PostDTO[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  get totalPages(): number { return Math.ceil(this.filtered.length / this.pageSize) || 1; }

  get countWithTags(): number { return this.posts.filter(p => p.tags?.length).length; }

  get countWithComments(): number { return this.posts.filter(p => (p.commentCount || 0) > 0).length; }

  onSortChange(value: string): void {
    const [field, dir] = value.split(',');
    this.sortField = field;
    this.sortDir = dir as 'asc' | 'desc';
    this.applyFilters();
  }

  onDetails(id: number): void { this.router.navigate(['/forum/posts', id]); }
  onEdit(id: number): void { this.router.navigate(['/forum/posts', id, 'edit']); }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce post ?')) {
      this.postService.delete(id).subscribe({
        next: () => { this.loadPosts(); this.showToast('Post supprimé avec succès'); },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onCreate(): void { this.router.navigate(['/forum/posts/create']); }

  showToast(msg: string): void {
    this.successMessage = msg;
    setTimeout(() => this.successMessage = '', 3000);
  }

  getUserName(post: PostDTO): string {
    if (!post.user) return 'Anonyme';
    return [post.user.prenom, post.user.nom].filter(Boolean).join(' ') || 'Anonyme';
  }

  getInitials(post: PostDTO): string {
    if (!post.user) return '?';
    const first = (post.user.prenom?.[0] || '').toUpperCase();
    const last = (post.user.nom?.[0] || '').toUpperCase();
    return first + last || '?';
  }

  getAvatarColor(post: PostDTO): string {
    const id = post.user?.idUser || 0;
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
