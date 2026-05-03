import { Component, OnInit } from '@angular/core';
import { FieldService } from '../../core/services/field.service';
import { ForecastService, ForecastResponse } from '../../core/services/forecast.service';
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
  forecast: ForecastResponse | null = null;
  forecastLoading = true;

  constructor(private fieldService: FieldService, private forecastService: ForecastService) {}

  ngOnInit(): void {
    let loaded = 0;
    const check = () => { loaded++; if (loaded === 3) this.loading = false; };
    this.fieldService.getAllEndroits().subscribe({ next: d => { this.endroits = d; check(); }, error: check });
    this.fieldService.getAllSousEspaces().subscribe({ next: d => { this.sousEspaces = d; check(); }, error: check });
    this.fieldService.getAllReservations().subscribe({ next: d => { this.reservations = d; check(); }, error: check });

    this.forecastService.getForecast(7).subscribe({
      next: data => { this.forecast = data; this.forecastLoading = false; },
      error: () => { this.forecastLoading = false; }
    });
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

  getForecastBarWidth(value: number): number {
    if (!this.forecast?.forecast?.length) return 0;
    const max = Math.max(...this.forecast.forecast.map(f => f.predicted), 1);
    return (value / max) * 100;
  }

  getDayLabel(dateStr: string): string {
    const days = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];
    const d = new Date(dateStr);
    return days[d.getDay()] + ' ' + d.getDate();
  }

  getForecastColor(value: number): string {
    if (!this.forecast?.forecast?.length) return '#2563eb';
    const max = Math.max(...this.forecast.forecast.map(f => f.predicted), 1);
    const ratio = value / max;
    if (ratio > 0.7) return '#ef4444';
    if (ratio > 0.4) return '#f59e0b';
    return '#22c55e';
  }
}
