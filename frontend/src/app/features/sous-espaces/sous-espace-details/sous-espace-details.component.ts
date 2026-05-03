import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FieldService } from '../../../core/services/field.service';
import { SousEspace, Equipement, Reservation } from '../../../core/models/endroit.model';

@Component({
  selector: 'app-sous-espace-details',
  templateUrl: './sous-espace-details.component.html',
  styleUrls: ['./sous-espace-details.component.css']
})
export class SousEspaceDetailsComponent implements OnInit {
  se!: SousEspace;
  id!: number;

  showEquipementForm = false;
  equipementForm: FormGroup;

  showReservationForm = false;
  reservationForm: FormGroup;

  errorMsg = '';
  successMsg = '';

  constructor(
    private route: ActivatedRoute,
    private fieldService: FieldService,
    private fb: FormBuilder
  ) {
    this.equipementForm = this.fb.group({
      nom: ['', Validators.required],
      quantite: [1, [Validators.required, Validators.min(1)]]
    });
    this.reservationForm = this.fb.group({
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.loadData();
  }

  loadData(): void {
    this.fieldService.getSousEspaceById(this.id).subscribe(se => {
      this.se = se;
      this.loadEquipements();
      this.loadReservations();
    });
  }

  loadEquipements(): void {
    this.fieldService.getEquipementsBySousEspace(this.id).subscribe(eq => this.se.equipements = eq);
  }

  loadReservations(): void {
    this.fieldService.getReservationsBySousEspace(this.id).subscribe(r => this.se.reservations = r);
  }

  clearMessages(): void { this.errorMsg = ''; this.successMsg = ''; }

  addEquipement(): void {
    if (this.equipementForm.valid) {
      this.fieldService.createEquipement(this.id, this.equipementForm.value).subscribe({
        next: () => { this.loadEquipements(); this.showEquipementForm = false; this.equipementForm.reset({ quantite: 1 }); },
        error: (err) => { this.errorMsg = err.error?.error || 'Erreur'; }
      });
    }
  }

  deleteEquipement(eqId: number): void {
    this.fieldService.deleteEquipement(eqId).subscribe(() => this.loadEquipements());
  }

  addReservation(): void {
    if (this.reservationForm.valid) {
      this.clearMessages();
      const { dateDebut, dateFin } = this.reservationForm.value;
      if (new Date(dateDebut) >= new Date(dateFin)) {
        this.errorMsg = 'La date de début doit être avant la date de fin';
        return;
      }
      this.fieldService.createReservation(this.id, this.reservationForm.value).subscribe({
        next: () => {
          this.loadReservations(); this.showReservationForm = false; this.reservationForm.reset();
          this.successMsg = 'Réservation créée avec succès';
          setTimeout(() => this.successMsg = '', 4000);
        },
        error: (err) => { this.errorMsg = err.error?.error || 'Erreur lors de la création'; }
      });
    }
  }

  confirmerReservation(rId: number): void {
    this.clearMessages();
    this.fieldService.confirmerReservation(rId).subscribe({
      next: () => { this.loadReservations(); this.successMsg = 'Réservation confirmée'; setTimeout(() => this.successMsg = '', 4000); },
      error: (err) => { this.errorMsg = err.error?.error || 'Erreur lors de la confirmation'; }
    });
  }

  annulerReservation(rId: number): void {
    const motif = prompt('Motif d\'annulation:') || 'Annulée par l\'administrateur';
    this.clearMessages();
    this.fieldService.annulerReservation(rId, motif).subscribe({
      next: () => { this.loadReservations(); this.successMsg = 'Réservation annulée'; setTimeout(() => this.successMsg = '', 4000); },
      error: (err) => { this.errorMsg = err.error?.error || err.error?.message || 'Erreur'; }
    });
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
}
