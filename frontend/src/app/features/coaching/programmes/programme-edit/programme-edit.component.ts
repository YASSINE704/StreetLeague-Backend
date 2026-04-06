import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { ProgrammeEntrainement, StatutProgramme } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-programme-edit',
  templateUrl: './programme-edit.component.html',
  styleUrls: ['./programme-edit.component.css']
})
export class ProgrammeEditComponent implements OnInit {
  programme!: ProgrammeEntrainement;
  statuts = Object.values(StatutProgramme);
  errorMessage = '';

  constructor(
    private programmeService: ProgrammeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programmeService.getById(id).subscribe({
      next: (data) => this.programme = data,
      error: () => this.errorMessage = 'Programme introuvable'
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (this.programme.dateFin && this.programme.dateDebut && this.programme.dateFin < this.programme.dateDebut) {
      this.errorMessage = 'La date de fin doit être après la date de début';
      return;
    }
    this.programmeService.update(this.programme.idProgramme!, this.programme).subscribe({
      next: () => this.router.navigate(['/coaching/programmes']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/coaching/programmes']);
  }
}
