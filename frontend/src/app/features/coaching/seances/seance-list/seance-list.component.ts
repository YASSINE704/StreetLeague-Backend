import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { ReservationService } from '../../../../core/services/reservation.service';
import { AuthService } from '../../../auth/auth.service';
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

  reservingId: number | null = null;
  selectedPaymentMode = 'SUR_PLACE';
  showReservationModal = false;
  showCardForm = false;
  reservationTarget: SeanceEntrainement | null = null;

  // Card form fields
  cardNumber = '';
  cardExpiry = '';
  cardCvv = '';
  cardName = '';
  paymentError = '';

  constructor(
    private seanceService: SeanceService,
    private reservationService: ReservationService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void { this.loadSeances(); }

  get isCoachOrAdmin(): boolean {
    const role = this.authService.userRole;
    return role === 'COACH' || role === 'ADMIN';
  }

  get isSportifOrJoueur(): boolean {
    const role = this.authService.userRole;
    return role === 'SPORTIF' || role === 'JOUEUR';
  }

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

  /** Open reservation modal with payment choice */
  openReservationModal(seance: SeanceEntrainement): void {
    this.reservationTarget = seance;
    this.selectedPaymentMode = 'SUR_PLACE';
    this.showReservationModal = true;
    this.showCardForm = false;
    this.paymentError = '';
    this.errorMessage = '';
    this.cardNumber = '';
    this.cardExpiry = '';
    this.cardCvv = '';
    this.cardName = '';
  }

  /** Proceed: if SUR_PLACE → reserve directly, if EN_LIGNE → show card form */
  proceedToPayment(): void {
    this.paymentError = '';
    if (this.selectedPaymentMode === 'EN_LIGNE') {
      this.showCardForm = true;
    } else {
      this.confirmReservation();
    }
  }

  /** Validate card and confirm online payment */
  confirmOnlinePayment(): void {
    this.paymentError = '';

    // Validate card fields
    const cleanCard = this.cardNumber.replace(/\s/g, '');
    if (cleanCard.length < 16) {
      this.paymentError = 'Numéro de carte invalide (16 chiffres requis)';
      return;
    }
    if (!this.cardExpiry || this.cardExpiry.length < 5) {
      this.paymentError = 'Date d\'expiration invalide (format MM/AA)';
      return;
    }
    if (!this.cardCvv || this.cardCvv.length < 3) {
      this.paymentError = 'CVV invalide (3 chiffres requis)';
      return;
    }
    if (!this.cardName || this.cardName.trim().length < 3) {
      this.paymentError = 'Nom sur la carte requis';
      return;
    }

    // Simulate payment processing then reserve
    this.confirmReservation();
  }

  /** Confirm reservation after payment mode selected */
  confirmReservation(): void {
    if (!this.reservationTarget?.idSeance) return;
    this.errorMessage = '';
    this.paymentError = '';
    this.reservingId = this.reservationTarget.idSeance;

    this.reservationService.reserver({
      seanceId: this.reservationTarget.idSeance,
      modePaiement: this.selectedPaymentMode
    }).subscribe({
      next: () => {
        this.reservingId = null;
        this.showReservationModal = false;
        this.showCardForm = false;
        this.reservationTarget = null;
        this.showToast(this.selectedPaymentMode === 'EN_LIGNE'
          ? 'Paiement accepté ! Réservation confirmée ✓'
          : 'Réservation effectuée ! Paiement sur place le jour J.');
        this.loadSeances();
      },
      error: (err) => {
        this.reservingId = null;
        this.paymentError = err.error?.message || 'Erreur lors de la réservation';
      }
    });
  }

  cancelReservation(): void {
    this.showReservationModal = false;
    this.showCardForm = false;
    this.reservationTarget = null;
    this.paymentError = '';
  }

  /** Format card number with spaces every 4 digits */
  formatCardNumber(): void {
    let raw = this.cardNumber.replace(/\s/g, '').replace(/\D/g, '');
    if (raw.length > 16) raw = raw.substring(0, 16);
    this.cardNumber = raw.replace(/(.{4})/g, '$1 ').trim();
  }

  canReserve(seance: SeanceEntrainement): boolean {
    return seance.statut === 'PREVUE' && (seance.placesRestantes == null || seance.placesRestantes > 0);
  }

  /** Navigate to feedback form for a completed session */
  onFeedback(seanceId: number): void {
    this.router.navigate(['/coaching/suivis/create', seanceId]);
  }

  onDetails(id: number): void { this.router.navigate(['/coaching/seances', id]); }
  onEdit(id: number): void { this.router.navigate(['/coaching/seances/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/seances/create']); }
}
