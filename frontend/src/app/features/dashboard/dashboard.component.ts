import { Component, OnInit } from '@angular/core';
import { FieldService } from '../../core/services/field.service';
import { Endroit, SousEspace, Reservation } from '../../core/models/endroit.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  endroits: Endroit[] = [];
  sousEspaces: SousEspace[] = [];
  reservations: Reservation[] = [];
  loading = true;

  constructor(private fieldService: FieldService) {}

  ngOnInit(): void {
    let loaded = 0;
    const check = () => { loaded++; if (loaded === 3) this.loading = false; };
    this.fieldService.getAllEndroits().subscribe({ next: d => { this.endroits = d; check(); }, error: check });
    this.fieldService.getAllSousEspaces().subscribe({ next: d => { this.sousEspaces = d; check(); }, error: check });
    this.fieldService.getAllReservations().subscribe({ next: d => { this.reservations = d; check(); }, error: check });
  }

  get totalEndroits(): number { return this.endroits.length; }
  get totalSousEspaces(): number { return this.sousEspaces.length; }
  get totalReservations(): number { return this.reservations.length; }

  get resEnAttente(): number { return this.reservations.filter(r => r.statut === 'EN_ATTENTE').length; }
  get resConfirmees(): number { return this.reservations.filter(r => r.statut === 'CONFIRMEE').length; }
  get resAnnulees(): number { return this.reservations.filter(r => r.statut === 'ANNULEE').length; }

  get endroitsDisponibles(): number { return this.endroits.filter(e => e.statut === 'DISPONIBLE').length; }
  get endroitsMaintenance(): number { return this.endroits.filter(e => e.statut === 'MAINTENANCE').length; }
  get endroitsIndisponibles(): number { return this.endroits.filter(e => e.statut === 'INDISPONIBLE').length; }

  get tauxOccupation(): number {
    if (this.totalReservations === 0) return 0;
    return Math.round((this.resConfirmees / this.totalReservations) * 100);
  }

  get endroitsParType(): { type: string; count: number }[] {
    const map: { [key: string]: number } = {};
    this.endroits.forEach(e => map[e.type] = (map[e.type] || 0) + 1);
    return Object.entries(map).map(([type, count]) => ({ type, count }));
  }

  get topEndroits(): { nom: string; count: number }[] {
    const map: { [key: string]: number } = {};
    this.reservations.forEach(r => {
      const nom = r.endroitNom || 'Inconnu';
      map[nom] = (map[nom] || 0) + 1;
    });
    return Object.entries(map)
      .map(([nom, count]) => ({ nom, count }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 5);
  }

  get recentReservations(): Reservation[] {
    return [...this.reservations]
      .sort((a, b) => new Date(b.dateCreation || '').getTime() - new Date(a.dateCreation || '').getTime())
      .slice(0, 5);
  }

  getBarWidth(count: number, max: number): number {
    return max > 0 ? (count / max) * 100 : 0;
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'CONFIRMEE': return 'badge-success';
      case 'ANNULEE': return 'badge-danger';
      case 'EN_ATTENTE': return 'badge-warning';
      default: return '';
    }
  }

  getStatutBadge(statut: string): string {
    switch (statut) {
      case 'CONFIRMEE': case 'DISPONIBLE': return 'sl-badge--green';
      case 'ANNULEE': case 'INDISPONIBLE': return 'sl-badge--red';
      case 'EN_ATTENTE': case 'MAINTENANCE': return 'sl-badge--orange';
      default: return 'sl-badge--gray';
    }
  }

  getTypeLabel(type: string): string {
    switch (type) {
      case 'STADE': return '🏟️ Stade';
      case 'SALLE_SPORT': return '🏋️ Salle';
      case 'TERRAIN': return '⚽ Terrain';
      default: return type;
    }
  }
}
