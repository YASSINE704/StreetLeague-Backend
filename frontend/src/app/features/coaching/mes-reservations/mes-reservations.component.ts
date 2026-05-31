import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservationService } from '../../../core/services/reservation.service';
import { AuthService } from '../../auth/auth.service';
import { ReservationSeance } from '../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-mes-reservations-coaching',
  templateUrl: './mes-reservations.component.html',
  styleUrls: ['./mes-reservations.component.css']
})
export class MesReservationsCoachingComponent implements OnInit {
  reservations: ReservationSeance[] = [];
  loading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private reservationService: ReservationService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.errorMessage = '';

    this.reservationService.getMesReservations().subscribe({
      next: (data) => {
        this.reservations = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors du chargement des réservations';
        this.loading = false;
      }
    });
  }

  onAnnuler(id: number): void {
    if (confirm('Annuler cette réservation ?')) {
      this.reservationService.annuler(id, 'Annulée par le sportif').subscribe({
        next: () => {
          this.successMessage = 'Réservation annulée';
          setTimeout(() => this.successMessage = '', 3000);
          this.loadReservations();
        },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur'
      });
    }
  }

  onVoirSeance(seanceId: number | undefined): void {
    if (seanceId) {
      this.router.navigate(['/coaching/seances', seanceId]);
    }
  }

  goToSeances(): void {
    this.router.navigate(['/coaching/seances']);
  }

  getStatutClass(statut: string | undefined): string {
    switch (statut) {
      case 'RESERVEE': return 'sl-badge--blue';
      case 'CONFIRMEE': return 'sl-badge--green';
      case 'ANNULEE': return 'sl-badge--red';
      default: return '';
    }
  }

  getPaiementClass(statut: string | undefined): string {
    switch (statut) {
      case 'PAYE': return 'sl-badge--green';
      case 'EN_ATTENTE': return 'sl-badge--orange';
      case 'REMBOURSE': return 'sl-badge--violet';
      default: return 'sl-badge--orange';
    }
  }
}
