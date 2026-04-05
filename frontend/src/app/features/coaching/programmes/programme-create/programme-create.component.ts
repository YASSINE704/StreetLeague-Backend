import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { ProgrammeEntrainement, StatutProgramme } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-programme-create',
  templateUrl: './programme-create.component.html',
  styleUrls: ['./programme-create.component.css']
})
export class ProgrammeCreateComponent {
  programme: ProgrammeEntrainement = {
    titre: '',
    dateDebut: '',
    dateFin: '',
    statut: StatutProgramme.BROUILLON
  };
  statuts = Object.values(StatutProgramme);
  errorMessage = '';
  today = new Date().toISOString().split('T')[0];

  constructor(private programmeService: ProgrammeService, private router: Router) {}

  onSubmit(): void {
    this.errorMessage = '';
    if (this.programme.dateFin && this.programme.dateDebut && this.programme.dateFin < this.programme.dateDebut) {
      this.errorMessage = 'La date de fin doit être après la date de début';
      return;
    }
    this.programmeService.create(this.programme).subscribe({
      next: () => this.router.navigate(['/coaching/programmes']),
      error: (err) => {
        this.errorMessage = err.error?.message || err.error?.errors?.titre || 'Erreur lors de la création';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/coaching/programmes']);
  }
}
