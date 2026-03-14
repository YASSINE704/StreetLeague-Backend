import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExerciceService } from '../../../../core/services/exercice.service';
import { SeanceService } from '../../../../core/services/seance.service';
import { Exercice, SeanceExercice, SeanceEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-exercice-list',
  templateUrl: './exercice-list.component.html',
  styleUrls: ['./exercice-list.component.css']
})
export class ExerciceListComponent implements OnInit {
  exercices: Exercice[] = [];
  seances: SeanceEntrainement[] = [];
  errorMessage = '';
  expandedId: number | null = null;
  linkedSeances: SeanceExercice[] = [];
  loadingLinks = false;

  // Assign to séance
  showAssignModal = false;
  assignExerciceId: number | null = null;
  assignExerciceNom = '';
  assignForm = { seanceId: 0, series: 3, repetitions: 10, charge: 0, tempsSecondes: 0, ordre: 1 };

  constructor(
    private exerciceService: ExerciceService,
    private seanceService: SeanceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadExercices();
    this.seanceService.getAll().subscribe({ next: (d) => this.seances = d });
  }

  loadExercices(): void {
    this.errorMessage = '';
    this.exerciceService.getAll().subscribe({
      next: (data) => this.exercices = data,
      error: () => this.errorMessage = 'Erreur de connexion au serveur'
    });
  }

  toggleExpand(id: number): void {
    if (this.expandedId === id) { this.expandedId = null; return; }
    this.expandedId = id;
    this.loadingLinks = true;
    this.exerciceService.getByExercice(id).subscribe({
      next: (data) => { this.linkedSeances = data; this.loadingLinks = false; },
      error: () => { this.linkedSeances = []; this.loadingLinks = false; }
    });
  }

  openAssignModal(ex: Exercice): void {
    this.assignExerciceId = ex.idExercice!;
    this.assignExerciceNom = ex.nom;
    this.assignForm = { seanceId: 0, series: 3, repetitions: 10, charge: 0, tempsSecondes: 0, ordre: 1 };
    this.showAssignModal = true;
  }

  closeAssignModal(): void { this.showAssignModal = false; }

  onAssign(): void {
    if (this.assignForm.seanceId === 0) { this.errorMessage = 'Sélectionnez une séance'; return; }
    this.exerciceService.addToSeance({
      seanceId: this.assignForm.seanceId,
      exerciceId: this.assignExerciceId!,
      series: this.assignForm.series,
      repetitions: this.assignForm.repetitions,
      charge: this.assignForm.charge,
      tempsSecondes: this.assignForm.tempsSecondes,
      ordre: this.assignForm.ordre
    }).subscribe({
      next: () => {
        this.showAssignModal = false;
        if (this.expandedId === this.assignExerciceId) this.toggleExpand(this.assignExerciceId!);
      },
      error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de l\'affectation'
    });
  }

  onUnlink(seanceExerciceId: number): void {
    if (confirm('Retirer cet exercice de la séance ?')) {
      this.exerciceService.removeFromSeance(seanceExerciceId).subscribe({
        next: () => { if (this.expandedId) this.toggleExpand(this.expandedId); },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur'
      });
    }
  }

  onDelete(id: number): void {
    if (confirm('Supprimer cet exercice du catalogue ?')) {
      this.exerciceService.delete(id).subscribe({
        next: () => this.loadExercices(),
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onEdit(id: number): void { this.router.navigate(['/coaching/exercices/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/exercices/create']); }

  getTypeIcon(type: string | undefined): string {
    switch (type) {
      case 'FORCE': return '🏋️';
      case 'CARDIO': return '🏃';
      case 'MOBILITE': return '🧘';
      case 'TECHNIQUE': return '⚡';
      default: return '💪';
    }
  }
}
