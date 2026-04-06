import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { ExerciceService } from '../../../../core/services/exercice.service';
import { SeanceEntrainement, Exercice, SeanceExercice } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-details',
  templateUrl: './seance-details.component.html',
  styleUrls: ['./seance-details.component.css']
})
export class SeanceDetailsComponent implements OnInit {
  seance!: SeanceEntrainement;
  catalogueExercices: Exercice[] = [];
  showAddForm = false;
  errorMessage = '';

  newExercice: SeanceExercice = {
    seanceId: 0,
    exerciceId: 0,
    series: 3,
    repetitions: 10,
    charge: 0,
    tempsSecondes: 0,
    ordre: 1
  };

  constructor(
    private seanceService: SeanceService,
    private exerciceService: ExerciceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSeance();
  }

  loadSeance(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.seanceService.getById(id).subscribe({
      next: (data) => {
        this.seance = data;
        this.newExercice.seanceId = data.idSeance!;
        this.newExercice.ordre = (data.exercices?.length || 0) + 1;
      },
      error: () => this.errorMessage = 'Erreur chargement séance'
    });
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    if (this.showAddForm && this.catalogueExercices.length === 0) {
      this.exerciceService.getAll().subscribe({
        next: (data) => this.catalogueExercices = data
      });
    }
  }

  onAddExercice(): void {
    this.errorMessage = '';
    if (this.newExercice.exerciceId === 0) {
      this.errorMessage = 'Veuillez sélectionner un exercice';
      return;
    }
    this.exerciceService.addToSeance(this.newExercice).subscribe({
      next: () => {
        this.showAddForm = false;
        this.newExercice = {
          seanceId: this.seance.idSeance!,
          exerciceId: 0,
          series: 3,
          repetitions: 10,
          charge: 0,
          tempsSecondes: 0,
          ordre: (this.seance.exercices?.length || 0) + 2
        };
        this.loadSeance();
      },
      error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de l\'ajout'
    });
  }

  onRemoveExercice(id: number): void {
    if (confirm('Retirer cet exercice de la séance ?')) {
      this.exerciceService.removeFromSeance(id).subscribe({
        next: () => this.loadSeance(),
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onAddSuivi(): void {
    this.router.navigate(['/coaching/suivis/create', this.seance.idSeance]);
  }

  onBack(): void {
    if (this.seance?.programmeId) {
      this.router.navigate(['/coaching/programmes', this.seance.programmeId]);
      return;
    }
    this.router.navigate(['/coaching/seances']);
  }

  getTotalSeries(): number {
    if (!this.seance?.exercices) return 0;
    return this.seance.exercices.reduce((sum, ex) => sum + (ex.series || 0), 0);
  }
}
