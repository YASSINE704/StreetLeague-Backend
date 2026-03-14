import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-programme-list',
  templateUrl: './programme-list.component.html',
  styleUrls: ['./programme-list.component.css']
})
export class ProgrammeListComponent implements OnInit {
  programmes: ProgrammeEntrainement[] = [];
  errorMessage = '';

  constructor(private programmeService: ProgrammeService, private router: Router) {}

  ngOnInit(): void { this.loadProgrammes(); }

  loadProgrammes(): void {
    this.errorMessage = '';
    this.programmeService.getAll().subscribe({
      next: (data) => this.programmes = data,
      error: () => this.errorMessage = 'Erreur de connexion au serveur. Vérifiez que le backend est lancé.'
    });
  }

  countByStatut(statut: string): number {
    return this.programmes.filter(p => p.statut === statut).length;
  }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce programme ?')) {
      this.programmeService.delete(id).subscribe({
        next: () => this.loadProgrammes(),
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onDetails(id: number): void { this.router.navigate(['/coaching/programmes', id]); }
  onEdit(id: number): void { this.router.navigate(['/coaching/programmes/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/programmes/create']); }
}
