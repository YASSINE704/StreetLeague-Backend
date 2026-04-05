import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FieldService } from '../../../core/services/field.service';
import { Endroit, TypeEndroit, StatutEndroit } from '../../../core/models/endroit.model';

@Component({
  selector: 'app-field-list',
  templateUrl: './field-list.component.html',
  styleUrls: ['./field-list.component.css']
})
export class FieldListComponent implements OnInit {
  endroits: Endroit[] = [];
  filtered: Endroit[] = [];
  loading = true;
  searchTerm = '';
  typeFilter = '';
  statutFilter = '';
  villeFilter = '';
  villes: string[] = [];
  types = Object.values(TypeEndroit);
  statuts = Object.values(StatutEndroit);

  constructor(private fieldService: FieldService, private router: Router) {}

  ngOnInit(): void {
    this.loadEndroits();
  }

  loadEndroits(): void {
    this.loading = true;
    this.fieldService.getAllEndroits().subscribe({
      next: (data) => {
        this.endroits = data;
        this.filtered = data;
        this.villes = [...new Set(data.map(e => e.ville).filter(v => v))];
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  applyFilters(): void {
    const term = this.searchTerm.toLowerCase();
    this.filtered = this.endroits.filter(e => {
      const matchSearch = !term || e.nom.toLowerCase().includes(term) || e.adresse?.toLowerCase().includes(term) || e.ville?.toLowerCase().includes(term);
      const matchType = !this.typeFilter || e.type === this.typeFilter;
      const matchStatut = !this.statutFilter || e.statut === this.statutFilter;
      const matchVille = !this.villeFilter || e.ville === this.villeFilter;
      return matchSearch && matchType && matchStatut && matchVille;
    });
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.typeFilter = '';
    this.statutFilter = '';
    this.villeFilter = '';
    this.filtered = this.endroits;
  }

  deleteEndroit(id: number): void {
    if (confirm('Supprimer cet endroit ?')) {
      this.fieldService.deleteEndroit(id).subscribe(() => this.loadEndroits());
    }
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'DISPONIBLE': return 'badge-success';
      case 'INDISPONIBLE': return 'badge-danger';
      case 'MAINTENANCE': return 'badge-warning';
      default: return '';
    }
  }

  getStatutBadge(statut: string): string {
    switch (statut) {
      case 'DISPONIBLE': return 'sl-badge--green';
      case 'INDISPONIBLE': return 'sl-badge--red';
      case 'MAINTENANCE': return 'sl-badge--orange';
      default: return 'sl-badge--gray';
    }
  }

  getTypeLabel(type: string): string {
    switch (type) {
      case 'STADE': return '🏟️ Stade';
      case 'SALLE_SPORT': return '🏋️ Salle de Sport';
      case 'TERRAIN': return '⚽ Terrain';
      default: return type;
    }
  }
}
