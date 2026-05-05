import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { FieldService } from '../../../../core/services/field.service';
import { SeanceEntrainement, Intensite, StatutSeance } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-edit',
  templateUrl: './seance-edit.component.html',
  styleUrls: ['./seance-edit.component.css']
})
export class SeanceEditComponent implements OnInit {
  seance!: SeanceEntrainement;
  sousEspaces: any[] = [];
  intensites = Object.values(Intensite);
  statuts = Object.values(StatutSeance);
  errorMessage = '';
  private returnProgrammeId: number | null = null;

  constructor(
    private seanceService: SeanceService,
    private fieldService: FieldService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const programmeId = this.route.snapshot.queryParamMap.get('programmeId');
    this.returnProgrammeId = programmeId ? Number(programmeId) : null;

    this.fieldService.getAllSousEspaces().subscribe(data => this.sousEspaces = data);

    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.seanceService.getById(id).subscribe({
      next: (data) => {
        this.seance = data;
        if (!this.returnProgrammeId && data.programmeId) {
          this.returnProgrammeId = data.programmeId;
        }
      },
      error: () => this.errorMessage = 'Séance introuvable'
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.seanceService.update(this.seance.idSeance!, this.seance).subscribe({
      next: () => this.navigateAfterSave(),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour';
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
