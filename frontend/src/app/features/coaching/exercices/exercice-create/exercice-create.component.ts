import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ExerciceService } from '../../../../core/services/exercice.service';
import { Exercice, TypeExercice } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-exercice-create',
  templateUrl: './exercice-create.component.html',
  styleUrls: ['./exercice-create.component.css']
})
export class ExerciceCreateComponent {
  exercice: Exercice = { nom: '', type: TypeExercice.FORCE };
  types = Object.values(TypeExercice);
  errorMessage = '';

  constructor(private exerciceService: ExerciceService, private router: Router) {}

  onSubmit(): void {
    this.errorMessage = '';
    this.exerciceService.create(this.exercice).subscribe({
      next: () => this.router.navigate(['/coaching/exercices']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la création';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/coaching/exercices']);
  }
}
