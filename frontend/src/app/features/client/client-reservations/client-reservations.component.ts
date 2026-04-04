import { Component, OnInit } from '@angular/core';
import { FieldService } from '../../../core/services/field.service';
import { Reservation } from '../../../core/models/endroit.model';

@Component({
  selector: 'app-client-reservations',
  templateUrl: './client-reservations.component.html',
  styleUrls: ['./client-reservations.component.css']
})
export class ClientReservationsComponent implements OnInit {
  reservations: Reservation[] = [];
  filtered: Reservation[] = [];
  loading = true;
  statutFilter = '';

  constructor(private fieldService: FieldService) {}

  ngOnInit(): void {
    this.fieldService.getAllReservations().subscribe({
      next: (data) => {
        this.reservations = data.sort((a, b) =>
          new Date(b.dateCreation || '').getTime() - new Date(a.dateCreation || '').getTime()
        );
        this.filtered = this.reservations;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  applyFilter(): void {
    this.filtered = this.statutFilter
      ? this.reservations.filter(r => r.statut === this.statutFilter)
      : this.reservations;
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'CONFIRMEE': return 'badge-success';
      case 'ANNULEE': return 'badge-danger';
      case 'EN_ATTENTE': return 'badge-warning';
      default: return '';
    }
  }

  annuler(id: number): void {
    const motif = prompt('Motif d\'annulation:');
    if (motif) {
      this.fieldService.annulerReservation(id, motif).subscribe(() => {
        const r = this.reservations.find(res => res.id === id);
        if (r) { r.statut = 'ANNULEE' as any; r.motifAnnulation = motif; }
      });
    }
  }
}
