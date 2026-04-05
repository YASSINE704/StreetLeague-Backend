import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FieldService } from '../../../core/services/field.service';
import { NotificationService } from '../../../core/services/notification.service';
import { SousEspace, Equipement } from '../../../core/models/endroit.model';

@Component({
  selector: 'app-client-sous-espace',
  templateUrl: './client-sous-espace.component.html',
  styleUrls: ['./client-sous-espace.component.css']
})
export class ClientSousEspaceComponent implements OnInit {
  se!: SousEspace;
  id!: number;

  reservationForm: FormGroup;
  showForm = false;
  errorMsg = '';
  successMsg = '';

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

  reserver(): void {
    if (this.reservationForm.valid) {
      this.errorMsg = '';
      const { dateDebut, dateFin } = this.reservationForm.value;
      if (new Date(dateDebut) >= new Date(dateFin)) {
        this.errorMsg = 'La date de début doit être avant la date de fin';
        return;
      }
      this.fieldService.createReservation(this.id, this.reservationForm.value).subscribe({
        next: () => {
          this.loadReservations();
          this.showForm = false;
          this.reservationForm.reset();
          this.successMsg = 'Réservation envoyée avec succès !';
          this.notifService.addNotification('NOUVELLE_RESERVATION', 'Réservation créée pour ' + (this.se?.nom || ''));
          setTimeout(() => this.successMsg = '', 5000);
        },
        error: (err) => { this.errorMsg = err.error?.error || 'Erreur lors de la réservation'; }
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
}
