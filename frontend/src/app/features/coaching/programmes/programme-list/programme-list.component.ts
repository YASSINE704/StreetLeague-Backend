import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { AuthService } from '../../../auth/auth.service';
import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';
declare var jspdf: any;

@Component({
  selector: 'app-programme-list',
  templateUrl: './programme-list.component.html',
  styleUrls: ['./programme-list.component.css']
})
export class ProgrammeListComponent implements OnInit {
  programmes: ProgrammeEntrainement[] = [];
  filtered: ProgrammeEntrainement[] = [];
  errorMessage = '';
  successMessage = '';

  // Search & Filter
  searchTerm = '';
  filterStatut = '';

  // Sort
  sortField = 'titre';
  sortDir: 'asc' | 'desc' = 'asc';

  // Pagination
  page = 1;
  pageSize = 5;

  constructor(
    private programmeService: ProgrammeService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void { this.loadProgrammes(); }

  get isCoachOrAdmin(): boolean {
    const role = this.authService.userRole;
    return role === 'COACH' || role === 'ADMIN';
  }

  loadProgrammes(): void {
    this.errorMessage = '';
    this.programmeService.getAll().subscribe({
      next: (data) => { this.programmes = data; this.applyFilters(); },
      error: () => this.errorMessage = 'Erreur de connexion au serveur. Vérifiez que le backend est lancé.'
    });
  }

  applyFilters(): void {
    let result = [...this.programmes];
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p => p.titre.toLowerCase().includes(term) || p.description?.toLowerCase().includes(term));
    }
    if (this.filterStatut) {
      result = result.filter(p => p.statut === this.filterStatut);
    }
    result.sort((a, b) => {
      const valA = (a as any)[this.sortField] || '';
      const valB = (b as any)[this.sortField] || '';
      const cmp = valA > valB ? 1 : valA < valB ? -1 : 0;
      return this.sortDir === 'asc' ? cmp : -cmp;
    });
    this.filtered = result;
    this.page = 1;
  }

  get paged(): ProgrammeEntrainement[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  get totalPages(): number { return Math.ceil(this.filtered.length / this.pageSize) || 1; }

  onSort(field: string): void {
    if (this.sortField === field) { this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc'; }
    else { this.sortField = field; this.sortDir = 'asc'; }
    this.applyFilters();
  }

  sortIcon(field: string): string {
    if (this.sortField !== field) return '↕';
    return this.sortDir === 'asc' ? '↑' : '↓';
  }

  countByStatut(statut: string): number { return this.programmes.filter(p => p.statut === statut).length; }

  showToast(msg: string): void {
    this.successMessage = msg;
    setTimeout(() => this.successMessage = '', 3000);
  }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce programme ?')) {
      this.programmeService.delete(id).subscribe({
        next: () => { this.loadProgrammes(); this.showToast('Programme supprimé avec succès'); },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  /** Export PDF — télécharge la liste des programmes avec séances et exercices */
  exportPDF(): void {
    import('jspdf').then(jsPDFModule => {
      const doc = new jsPDFModule.jsPDF();
      let y = 20;

      // Title
      doc.setFontSize(18);
      doc.setFont('helvetica', 'bold');
      doc.text('StreetLeague - Programmes d\'Entraînement', 14, y);
      y += 10;
      doc.setFontSize(10);
      doc.setFont('helvetica', 'normal');
      doc.text('Généré le ' + new Date().toLocaleDateString('fr-FR') + ' à ' + new Date().toLocaleTimeString('fr-FR'), 14, y);
      y += 12;

      for (const prog of this.programmes) {
        // Check page overflow
        if (y > 260) { doc.addPage(); y = 20; }

        // Programme header
        doc.setFontSize(13);
        doc.setFont('helvetica', 'bold');
        doc.text(prog.titre + ' [' + (prog.statut || 'N/A') + ']', 14, y);
        y += 6;
        doc.setFontSize(9);
        doc.setFont('helvetica', 'normal');
        doc.text('Période : ' + (prog.dateDebut || '?') + ' → ' + (prog.dateFin || '?'), 14, y);
        y += 5;
        if (prog.description) {
          doc.text('Description : ' + prog.description.substring(0, 90), 14, y);
          y += 5;
        }

        // Séances
        if (prog.seances && prog.seances.length > 0) {
          doc.setFont('helvetica', 'bold');
          doc.text('  Séances (' + prog.seances.length + ') :', 14, y);
          y += 5;
          doc.setFont('helvetica', 'normal');

          for (const seance of prog.seances) {
            if (y > 270) { doc.addPage(); y = 20; }
            doc.text('    • ' + seance.titreSeance + ' | ' + (seance.dateSeance || '') + ' | ' + (seance.intensite || '') + ' | ' + (seance.statut || ''), 14, y);
            y += 4;

            // Exercices de la séance
            if (seance.exercices && seance.exercices.length > 0) {
              for (const ex of seance.exercices) {
                if (y > 275) { doc.addPage(); y = 20; }
                doc.text('        → ' + (ex.exerciceNom || 'Exercice') + ' (' + (ex.series || 0) + 'x' + (ex.repetitions || 0) + ')', 14, y);
                y += 4;
              }
            }
          }
        }
        y += 8;
      }

      doc.save('StreetLeague_Programmes.pdf');
      this.showToast('PDF téléchargé avec succès');
    });
  }

  onDetails(id: number): void { this.router.navigate(['/coaching/programmes', id]); }
  onEdit(id: number): void { this.router.navigate(['/coaching/programmes/edit', id]); }
  onCreate(): void { this.router.navigate(['/coaching/programmes/create']); }
}
