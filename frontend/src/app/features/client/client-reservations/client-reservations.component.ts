import { Component, OnInit } from '@angular/core';
import { FieldService } from '../../../core/services/field.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Reservation } from '../../../core/models/endroit.model';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

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

  constructor(private fieldService: FieldService, private notifService: NotificationService) {}

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

  getStatutBadge(statut: string): string {
    switch (statut) {
      case 'CONFIRMEE': return 'sl-badge--green';
      case 'ANNULEE': return 'sl-badge--red';
      case 'EN_ATTENTE': return 'sl-badge--orange';
      default: return 'sl-badge--gray';
    }
  }

  annuler(id: number): void {
    const motif = prompt('Motif d\'annulation:');
    if (motif) {
      this.fieldService.annulerReservation(id, motif).subscribe(() => {
        const r = this.reservations.find(res => res.id === id);
        if (r) {
          r.statut = 'ANNULEE' as any;
          r.motifAnnulation = motif;
          this.notifService.addNotification('RESERVATION_ANNULEE', 'Réservation pour ' + (r.sousEspaceNom || '') + ' annulée');
        }
      });
    }
  }

  exportPDF(): void {
    const doc = new jsPDF();
    doc.setFontSize(18);
    doc.text('StreetLeague - Mes Réservations', 14, 22);
    doc.setFontSize(10);
    doc.text('Généré le ' + new Date().toLocaleDateString('fr-FR'), 14, 30);

    const rows = this.filtered.map(r => [
      r.endroitNom || '-',
      r.sousEspaceNom || '-',
      new Date(r.dateDebut).toLocaleString('fr-FR'),
      new Date(r.dateFin).toLocaleString('fr-FR'),
      r.statut || '-',
      r.motifAnnulation || '-'
    ]);

    autoTable(doc, {
      head: [['Endroit', 'Sous-Espace', 'Début', 'Fin', 'Statut', 'Motif annulation']],
      body: rows,
      startY: 36,
      styles: { fontSize: 8 },
      headStyles: { fillColor: [26, 26, 46] }
    });

    doc.save('mes-reservations.pdf');
  }
}
