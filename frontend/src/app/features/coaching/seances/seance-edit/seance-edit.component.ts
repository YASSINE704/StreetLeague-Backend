import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { SeanceEntrainement, Intensite, StatutSeance } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-edit',
  templateUrl: './seance-edit.component.html',
  styleUrls: ['./seance-edit.component.css']
})
export class SeanceEditComponent implements OnInit {
  seance!: SeanceEntrainement;
  intensites = Object.values(Intensite);
  statuts = Object.values(StatutSeance);
  errorMessage = '';

  constructor(
    private seanceService: SeanceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.seanceService.getById(id).subscribe({
      next: (data) => this.seance = data,
      error: () => this.errorMessage = 'Séance introuvable'
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.seanceService.update(this.seance.idSeance!, this.seance).subscribe({
      next: () => this.router.navigate(['/coaching/seances']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/coaching/seances']);
  }
}
