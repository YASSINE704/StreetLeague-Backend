import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FieldService } from '../../../core/services/field.service';
import {
  Endroit, SousEspace, Equipement, Reservation,
  TypeSousEspace, StatutEndroit
} from '../../../core/models/endroit.model';

@Component({
  selector: 'app-field-details',
  templateUrl: './field-details.component.html',
  styleUrls: ['./field-details.component.css']
})
export class FieldDetailsComponent implements OnInit {
  endroit!: Endroit;
  sousEspaces: SousEspace[] = [];
  id!: number;

  showSousEspaceForm = false;
  sousEspaceForm: FormGroup;
  typesSousEspace = Object.values(TypeSousEspace);
  statutsEndroit = Object.values(StatutEndroit);

  showEquipementForm: number | null = null;
  equipementForm: FormGroup;

  showReservationForm: number | null = null;
  reservationForm: FormGroup;

  reservationError: { [seId: number]: string } = {};
  reservationSuccess: { [seId: number]: string } = {};
  minDateTime: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fieldService: FieldService,
    private fb: FormBuilder
  ) {
    this.sousEspaceForm = this.fb.group({
      nom: ['', Validators.required],
      type: ['TERRAIN', Validators.required],
      capacite: [0, [Validators.required, Validators.min(1)]],
      statut: ['DISPONIBLE', Validators.required]
    });
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
    this.updateMinDateTime();
    this.loadData();
  }

  updateMinDateTime(): void {
    const now = new Date();
    this.minDateTime = now.toISOString().slice(0, 16);
  }

  loadData(): void {
    this.fieldService.getEndroitById(this.id).subscribe(e => this.endroit = e);
    this.fieldService.getSousEspacesByEndroit(this.id).subscribe(s => this.sousEspaces = s);
  }

  sousEspaceError = '';

  // Sous-Espaces
  addSousEspace(): void {
    if (this.sousEspaceForm.valid) {
      this.sousEspaceError = '';
      this.fieldService.createSousEspace(this.id, this.sousEspaceForm.value).subscribe({
        next: () => {
          this.loadData();
          this.showSousEspaceForm = false;
          this.sousEspaceForm.reset({ type: 'TERRAIN', statut: 'DISPONIBLE', capacite: 0 });
        },
        error: (err) => {
          this.sousEspaceError = err.error?.error || 'Erreur lors de la création du sous-espace';
        }
      });
    }
  }

  deleteSousEspace(seId: number): void {
    if (confirm('Supprimer ce sous-espace ?')) {
      this.fieldService.deleteSousEspace(seId).subscribe(() => this.loadData());
    }
  }

  // Equipements
  loadEquipements(se: SousEspace): void {
    this.fieldService.getEquipementsBySousEspace(se.id!).subscribe(eq => se.equipements = eq);
  }

  addEquipement(seId: number): void {
    if (this.equipementForm.valid) {
      this.fieldService.createEquipement(seId, this.equipementForm.value).subscribe(() => {
        const se = this.sousEspaces.find(s => s.id === seId);
        if (se) this.loadEquipements(se);
        this.showEquipementForm = null;
        this.equipementForm.reset({ quantite: 1 });
      });
    }
  }

  deleteEquipement(eqId: number, se: SousEspace): void {
    this.fieldService.deleteEquipement(eqId).subscribe(() => this.loadEquipements(se));
  }

  // Reservations
  loadReservations(se: SousEspace): void {
    this.fieldService.getReservationsBySousEspace(se.id!).subscribe(r => se.reservations = r);
  }

  clearMessages(seId: number): void {
    delete this.reservationError[seId];
    delete this.reservationSuccess[seId];
  }

  addReservation(seId: number): void {
    if (this.reservationForm.valid) {
      this.clearMessages(seId);
      const { dateDebut, dateFin } = this.reservationForm.value;

      // Frontend validation
      if (new Date(dateDebut) >= new Date(dateFin)) {
        this.reservationError[seId] = 'La date de début doit être avant la date de fin';
        return;
      }

      this.fieldService.createReservation(seId, this.reservationForm.value).subscribe({
        next: () => {
          const se = this.sousEspaces.find(s => s.id === seId);
          if (se) this.loadReservations(se);
          this.showReservationForm = null;
          this.reservationForm.reset();
          this.reservationSuccess[seId] = 'Réservation créée avec succès';
          setTimeout(() => delete this.reservationSuccess[seId], 4000);
        },
        error: (err) => {
          this.reservationError[seId] = err.error?.error || 'Erreur lors de la création de la réservation';
        }
      });
    }
  }

  confirmerReservation(rId: number, se: SousEspace): void {
    this.clearMessages(se.id!);
    this.fieldService.confirmerReservation(rId).subscribe({
      next: () => {
        this.loadReservations(se);
        this.reservationSuccess[se.id!] = 'Réservation confirmée';
        setTimeout(() => delete this.reservationSuccess[se.id!], 4000);
      },
      error: (err) => {
        this.reservationError[se.id!] = err.error?.error || 'Erreur lors de la confirmation';
      }
    });
  }

  annulerReservation(rId: number, se: SousEspace): void {
    const motif = prompt('Motif d\'annulation:');
    if (motif) {
      this.clearMessages(se.id!);
      this.fieldService.annulerReservation(rId, motif).subscribe({
        next: () => {
          this.loadReservations(se);
          this.reservationSuccess[se.id!] = 'Réservation annulée';
          setTimeout(() => delete this.reservationSuccess[se.id!], 4000);
        },
        error: (err) => {
          this.reservationError[se.id!] = err.error?.error || 'Erreur lors de l\'annulation';
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
}
