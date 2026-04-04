import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { SeanceService } from '../../../../core/services/seance.service';
import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-programme-details',
  templateUrl: './programme-details.component.html',
  styleUrls: ['./programme-details.component.css']
})
export class ProgrammeDetailsComponent implements OnInit {
  programme!: ProgrammeEntrainement;
  errorMessage = '';

  constructor(
    private programmeService: ProgrammeService,
    private seanceService: SeanceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProgramme();
  }

  loadProgramme(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programmeService.getById(id).subscribe({
      next: (data) => this.programme = data,
      error: () => this.errorMessage = 'Erreur lors du chargement du programme'
    });
  }

  onAddSeance(): void {
    this.router.navigate(['/coaching/seances/create', this.programme.idProgramme]);
  }

  onSeanceDetails(seanceId: number): void {
    this.router.navigate(['/coaching/seances', seanceId]);
  }

  onEditSeance(seanceId: number): void {
    this.router.navigate(['/coaching/seances/edit', seanceId], {
      queryParams: { programmeId: this.programme.idProgramme }
    });
  }

  onDeleteSeance(seanceId: number): void {
    if (!confirm('Supprimer cette séance du programme ?')) {
      return;
    }

    this.seanceService.delete(seanceId).subscribe({
      next: () => this.loadProgramme(),
      error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression de la séance'
    });
  }

  onBack(): void {
    this.router.navigate(['/coaching/programmes']);
  }
}
