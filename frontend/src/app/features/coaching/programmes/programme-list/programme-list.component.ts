import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-programme-list',
  templateUrl: './programme-list.component.html',
  styleUrls: ['./programme-list.component.css']
})
export class ProgrammeListComponent implements OnInit {
  programmes: ProgrammeEntrainement[] = [];
  filtered: ProgrammeEntrainement[] = [];
  errorMessage = '';
  successMessage = '';

  // Search & Filter
  searchTerm = '';
  filterStatut = '';

  // Sort
  sortField = 'titre';
  sortDir: 'asc' | 'desc' = 'asc';

  // Pagination
  page = 1;
  pageSize = 5;

  constructor(private programmeService: ProgrammeService, private router: Router) {}

  ngOnInit(): void { this.loadProgrammes(); }

  loadProgrammes(): void {
    this.errorMessage = '';
    this.programmeService.getAll().subscribe({
      next: (data) => { this.programmes = data; this.applyFilters(); },
      error: () => this.errorMessage = 'Erreur de connexion au serveur. Vérifiez que le backend est lancé.'
    });
  }

  applyFilters(): void {
    let result = [...this.programmes];
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p => p.titre.toLowerCase().includes(term) || p.description?.toLowerCase().includes(term));
    }
    if (this.filterStatut) {
      result = result.filter(p => p.statut === this.filterStatut);
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

  get paged(): ProgrammeEntrainement[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  get totalPages(): number { return Math.ceil(this.filtered.length / this.pageSize) || 1; }

  onSort(field: string): void {
    if (this.sortField === field) { this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc'; }
    else { this.sortField = field; this.sortDir = 'asc'; }
    this.applyFilters();
  }

  sortIcon(field: string): string {
    if (this.sortField !== field) return '↕';
    return this.sortDir === 'asc' ? '↑' : '↓';
  }

  countByStatut(statut: string): number { return this.programmes.filter(p => p.statut === statut).length; }

  showToast(msg: string): void {
    this.successMessage = msg;
    setTimeout(() => this.successMessage = '', 3000);
  }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce programme ?')) {
      this.programmeService.delete(id).subscribe({
        next: () => { this.loadProgrammes(); this.showToast('Programme supprimé avec succès'); },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onDetails(id: number): void { this.router.navigate(['/coaching/programmes', id]); }
  onEdit(id: number): void { this.router.navigate(['/coaching/programmes/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/programmes/create']); }
}
