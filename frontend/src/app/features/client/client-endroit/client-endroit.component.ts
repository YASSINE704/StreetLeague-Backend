import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FieldService } from '../../../core/services/field.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Endroit, SousEspace } from '../../../core/models/endroit.model';

@Component({
  selector: 'app-client-endroit',
  templateUrl: './client-endroit.component.html',
  styleUrls: ['./client-endroit.component.css']
})
export class ClientEndroitComponent implements OnInit {
  endroit!: Endroit;
  sousEspaces: SousEspace[] = [];
  id!: number;

  reservationForm: FormGroup;
  showFormForSe: number | null = null;
  errorMsg: { [seId: number]: string } = {};
  successMsg: { [seId: number]: string } = {};

  constructor(
    private route: ActivatedRoute,
    private fieldService: FieldService,
    private notifService: NotificationService,
    private fb: FormBuilder
  ) {
    this.reservationForm = this.fb.group({
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.fieldService.getEndroitById(this.id).subscribe(e => this.endroit = e);
    this.fieldService.getSousEspacesByEndroit(this.id).subscribe(s => {
      this.sousEspaces = s;
      s.forEach(se => this.loadReservations(se));
    });
  }

  loadReservations(se: SousEspace): void {
    this.fieldService.getReservationsBySousEspace(se.id!).subscribe(r => se.reservations = r);
  }

  openForm(seId: number): void {
    this.showFormForSe = this.showFormForSe === seId ? null : seId;
    this.reservationForm.reset();
    delete this.errorMsg[seId];
  }

  reserver(seId: number): void {
    if (this.reservationForm.valid) {
      delete this.errorMsg[seId];
      const { dateDebut, dateFin } = this.reservationForm.value;
      if (new Date(dateDebut) >= new Date(dateFin)) {
        this.errorMsg[seId] = 'La date de début doit être avant la date de fin';
        return;
      }
      this.fieldService.createReservation(seId, this.reservationForm.value).subscribe({
        next: () => {
          const se = this.sousEspaces.find(s => s.id === seId);
          if (se) this.loadReservations(se);
          this.showFormForSe = null;
          this.reservationForm.reset();
          this.successMsg[seId] = 'Réservation envoyée avec succès !';
          this.notifService.addNotification('NOUVELLE_RESERVATION', 'Réservation créée pour ' + (this.endroit?.nom || ''));
          setTimeout(() => delete this.successMsg[seId], 5000);
        },
        error: (err) => {
          this.errorMsg[seId] = err.error?.error || 'Erreur lors de la réservation';
        }
      });
    }
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'DISPONIBLE': case 'CONFIRMEE': return 'badge-success';
      case 'INDISPONIBLE': case 'ANNULEE': return 'badge-danger';
      case 'MAINTENANCE': case 'EN_ATTENTE': return 'badge-warning';
      default: return '';
    }
  }

  getStatutBadge(statut: string): string {
    switch (statut) {
      case 'DISPONIBLE': case 'CONFIRMEE': return 'sl-badge--green';
      case 'INDISPONIBLE': case 'ANNULEE': return 'sl-badge--red';
      case 'MAINTENANCE': case 'EN_ATTENTE': return 'sl-badge--orange';
      default: return 'sl-badge--gray';
    }
  }

  get reservationsActives(): number {
    let count = 0;
    this.sousEspaces.forEach(se => {
      if (se.reservations) {
        count += se.reservations.filter(r => r.statut === 'CONFIRMEE' || r.statut === 'EN_ATTENTE').length;
      }
    });
    return count;
  }

  get capaciteRestante(): number {
    return this.endroit ? (this.endroit.capacite - this.reservationsActives) : 0;
  }

  get isComplet(): boolean {
    return this.endroit && this.capaciteRestante <= 0;
  }
}
