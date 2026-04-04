import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { SeanceEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-list',
  templateUrl: './seance-list.component.html',
  styleUrls: ['./seance-list.component.css']
})
export class SeanceListComponent implements OnInit {
  seances: SeanceEntrainement[] = [];
  filtered: SeanceEntrainement[] = [];
  errorMessage = '';
  successMessage = '';

  searchTerm = '';
  filterStatut = '';
  filterIntensite = '';
  sortField = 'dateSeance';
  sortDir: 'asc' | 'desc' = 'desc';
  page = 1;
  pageSize = 8;

  constructor(private seanceService: SeanceService, private router: Router) {}

  ngOnInit(): void { this.loadSeances(); }

  loadSeances(): void {
    this.errorMessage = '';
    this.seanceService.getAll().subscribe({
      next: (data) => { this.seances = data; this.applyFilters(); },
      error: () => this.errorMessage = 'Erreur de connexion au serveur'
    });
  }

  applyFilters(): void {
    let result = [...this.seances];
    if (this.searchTerm) {
      const t = this.searchTerm.toLowerCase();
      result = result.filter(s => s.titreSeance.toLowerCase().includes(t) || s.programmeTitre?.toLowerCase().includes(t));
    }
    if (this.filterStatut) result = result.filter(s => s.statut === this.filterStatut);
    if (this.filterIntensite) result = result.filter(s => s.intensite === this.filterIntensite);
    result.sort((a, b) => {
      const vA = (a as any)[this.sortField] || '';
      const vB = (b as any)[this.sortField] || '';
      const c = vA > vB ? 1 : vA < vB ? -1 : 0;
      return this.sortDir === 'asc' ? c : -c;
    });
    this.filtered = result;
    this.page = 1;
  }

  get paged(): SeanceEntrainement[] {
    const s = (this.page - 1) * this.pageSize;
    return this.filtered.slice(s, s + this.pageSize);
  }
  get totalPages(): number { return Math.ceil(this.filtered.length / this.pageSize) || 1; }

  onSort(field: string): void {
    if (this.sortField === field) this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    else { this.sortField = field; this.sortDir = 'asc'; }
    this.applyFilters();
  }
  sortIcon(f: string): string { if (this.sortField !== f) return '↕'; return this.sortDir === 'asc' ? '↑' : '↓'; }

  showToast(msg: string): void { this.successMessage = msg; setTimeout(() => this.successMessage = '', 3000); }

  onDelete(id: number): void {
    if (confirm('Supprimer cette séance ?')) {
      this.seanceService.delete(id).subscribe({
        next: () => { this.loadSeances(); this.showToast('Séance supprimée'); },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur'
      });
    }
  }

  onDetails(id: number): void { this.router.navigate(['/coaching/seances', id]); }
  onEdit(id: number): void { this.router.navigate(['/coaching/seances/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/seances/create']); }
}
