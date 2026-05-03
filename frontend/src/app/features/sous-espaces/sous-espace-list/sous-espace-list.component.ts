import { Component, OnInit } from '@angular/core';
import { FieldService } from '../../../core/services/field.service';
import { SousEspace, TypeSousEspace } from '../../../core/models/endroit.model';

@Component({
  selector: 'app-sous-espace-list',
  templateUrl: './sous-espace-list.component.html',
  styleUrls: ['./sous-espace-list.component.css']
})
export class SousEspaceListComponent implements OnInit {
  sousEspaces: SousEspace[] = [];
  filtered: SousEspace[] = [];
  loading = true;
  searchTerm = '';
  typeFilter = '';
  types = Object.values(TypeSousEspace);

  constructor(private fieldService: FieldService) {}

  ngOnInit(): void {
    this.loadSousEspaces();
  }

  loadSousEspaces(): void {
    this.loading = true;
    this.fieldService.getAllSousEspaces().subscribe({
      next: (data) => {
        this.sousEspaces = data;
        this.filtered = data;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  applyFilters(): void {
    this.filtered = this.sousEspaces.filter(se => {
      const matchSearch = !this.searchTerm ||
        se.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        (se.endroitNom && se.endroitNom.toLowerCase().includes(this.searchTerm.toLowerCase()));
      const matchType = !this.typeFilter || se.type === this.typeFilter;
      return matchSearch && matchType;
    });
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

  getTypeIcon(type: string): string {
    switch (type) {
      case 'TERRAIN': return '⚽';
      case 'SALLE': return '🏋️';
      case 'COURT': return '🎾';
      case 'ZONE': return '📍';
      default: return '🏟️';
    }
  }
}
