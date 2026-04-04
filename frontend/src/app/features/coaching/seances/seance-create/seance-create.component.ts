import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { SeanceEntrainement, ProgrammeEntrainement, Intensite, StatutSeance } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-create',
  templateUrl: './seance-create.component.html',
  styleUrls: ['./seance-create.component.css']
})
export class SeanceCreateComponent implements OnInit {
  seance: SeanceEntrainement = {
    titreSeance: '',
    dateSeance: '',
    dureeMinutes: 60,
    intensite: Intensite.MOYENNE,
    statut: StatutSeance.PREVUE,
    programmeId: 0
  };
  programmes: ProgrammeEntrainement[] = [];
  intensites = Object.values(Intensite);
  statuts = Object.values(StatutSeance);
  errorMessage = '';
  private returnProgrammeId: number | null = null;

  constructor(
    private seanceService: SeanceService,
    private programmeService: ProgrammeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const programmeId = this.route.snapshot.paramMap.get('programmeId');
    if (programmeId) {
      this.seance.programmeId = Number(programmeId);
      this.returnProgrammeId = Number(programmeId);
    }
    this.programmeService.getAll().subscribe(data => this.programmes = data);
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.seanceService.create(this.seance).subscribe({
      next: () => this.navigateAfterSave(),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la création';
      }
    });
  }

  onCancel(): void {
    this.navigateAfterSave();
  }

  private navigateAfterSave(): void {
    if (this.returnProgrammeId) {
      this.router.navigate(['/coaching/programmes', this.returnProgrammeId]);
      return;
    }
    this.router.navigate(['/coaching/seances']);
  }
}
