import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { SeanceEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-list',
  templateUrl: './seance-list.component.html',
  styleUrls: ['./seance-list.component.css']
})
export class SeanceListComponent implements OnInit {
  seances: SeanceEntrainement[] = [];
  errorMessage = '';

  constructor(private seanceService: SeanceService, private router: Router) {}

  ngOnInit(): void {
    this.loadSeances();
  }

  loadSeances(): void {
    this.errorMessage = '';
    this.seanceService.getAll().subscribe({
      next: (data) => this.seances = data,
      error: () => this.errorMessage = 'Erreur de connexion au serveur'
    });
  }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette séance ?')) {
      this.seanceService.delete(id).subscribe({
        next: () => this.loadSeances(),
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onDetails(id: number): void { this.router.navigate(['/coaching/seances', id]); }
  onEdit(id: number): void { this.router.navigate(['/coaching/seances/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/seances/create']); }
}
