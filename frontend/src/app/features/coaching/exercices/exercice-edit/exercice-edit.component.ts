import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExerciceService } from '../../../../core/services/exercice.service';
import { Exercice, TypeExercice } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-exercice-edit',
  templateUrl: './exercice-edit.component.html',
  styleUrls: ['./exercice-edit.component.css']
})
export class ExerciceEditComponent implements OnInit {
  exercice!: Exercice;
  types = Object.values(TypeExercice);
  errorMessage = '';

  constructor(
    private exerciceService: ExerciceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.exerciceService.getById(id).subscribe({
      next: (data) => this.exercice = data,
      error: () => this.errorMessage = 'Exercice introuvable'
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.exerciceService.update(this.exercice.idExercice!, this.exercice).subscribe({
      next: () => this.router.navigate(['/coaching/exercices']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/coaching/exercices']);
  }
}
