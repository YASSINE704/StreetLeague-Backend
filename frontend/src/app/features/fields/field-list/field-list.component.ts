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
  loading = true;
  typeFilter = '';
  types = Object.values(TypeEndroit);
  statuts = Object.values(StatutEndroit);

  constructor(private fieldService: FieldService, private router: Router) {}

  ngOnInit(): void {
    this.loadEndroits();
  }

  loadEndroits(): void {
    this.loading = true;
    this.fieldService.getAllEndroits().subscribe({
      next: (data) => { this.endroits = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
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

  getTypeLabel(type: string): string {
    switch (type) {
      case 'STADE': return '🏟️ Stade';
      case 'SALLE_SPORT': return '🏋️ Salle de Sport';
      case 'TERRAIN': return '⚽ Terrain';
      default: return type;
    }
  }
}
