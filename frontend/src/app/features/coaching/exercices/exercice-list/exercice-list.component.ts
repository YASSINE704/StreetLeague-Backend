import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExerciceService } from '../../../../core/services/exercice.service';
import { Exercice } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-exercice-list',
  templateUrl: './exercice-list.component.html',
  styleUrls: ['./exercice-list.component.css']
})
export class ExerciceListComponent implements OnInit {
  exercices: Exercice[] = [];
  errorMessage = '';

  constructor(private exerciceService: ExerciceService, private router: Router) {}

  ngOnInit(): void {
    this.loadExercices();
  }

  loadExercices(): void {
    this.errorMessage = '';
    this.exerciceService.getAll().subscribe({
      next: (data) => this.exercices = data,
      error: () => this.errorMessage = 'Erreur de connexion au serveur'
    });
  }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet exercice ?')) {
      this.exerciceService.delete(id).subscribe({
        next: () => this.loadExercices(),
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onEdit(id: number): void { this.router.navigate(['/coaching/exercices/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/exercices/create']); }
}
