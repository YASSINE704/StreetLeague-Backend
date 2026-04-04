import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { SeanceService } from '../../../../core/services/seance.service';
import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-programme-details',
  templateUrl: './programme-details.component.html',
  styleUrls: ['./programme-details.component.css']
})
export class ProgrammeDetailsComponent implements OnInit {
  programme!: ProgrammeEntrainement;
  errorMessage = '';

  constructor(
    private programmeService: ProgrammeService,
    private seanceService: SeanceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProgramme();
  }

  loadProgramme(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programmeService.getById(id).subscribe({
      next: (data) => this.programme = data,
      error: () => this.errorMessage = 'Erreur lors du chargement du programme'
    });
  }

  onAddSeance(): void {
    this.router.navigate(['/coaching/seances/create', this.programme.idProgramme]);
  }

  onSeanceDetails(seanceId: number): void {
    this.router.navigate(['/coaching/seances', seanceId]);
  }

  onEditSeance(seanceId: number): void {
    this.router.navigate(['/coaching/seances/edit', seanceId], {
      queryParams: { programmeId: this.programme.idProgramme }
    });
  }

  onDeleteSeance(seanceId: number): void {
    if (!confirm('Supprimer cette séance du programme ?')) {
      return;
    }

    this.seanceService.delete(seanceId).subscribe({
      next: () => this.loadProgramme(),
      error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression de la séance'
    });
  }

  onBack(): void {
    this.router.navigate(['/coaching/programmes']);
  }

  exportPDF(): void {
    const doc = new jsPDF();
    const p = this.programme;

    // Helper to strip problematic characters
    const clean = (s: string): string => {
      return s
        .replace(/→/g, '->')
        .replace(/é/g, 'e').replace(/è/g, 'e').replace(/ê/g, 'e').replace(/ë/g, 'e')
        .replace(/à/g, 'a').replace(/â/g, 'a')
        .replace(/ù/g, 'u').replace(/û/g, 'u')
        .replace(/ô/g, 'o').replace(/î/g, 'i').replace(/ï/g, 'i')
        .replace(/ç/g, 'c')
        .replace(/É/g, 'E').replace(/È/g, 'E').replace(/Ê/g, 'E')
        .replace(/À/g, 'A').replace(/Ç/g, 'C');
    };

    // Header bar
    doc.setFillColor(37, 99, 235);
    doc.rect(0, 0, 210, 40, 'F');
    doc.setFontSize(22);
    doc.setTextColor(255, 255, 255);
    doc.text(clean(p.titre), 14, 18);
    doc.setFontSize(11);
    doc.text(`Statut: ${p.statut}  |  ${clean(p.dateDebut + ' -> ' + p.dateFin)}`, 14, 28);
    if (p.description) {
      doc.text(clean(p.description), 14, 35);
    }

    // Body
    let y = 52;
    doc.setTextColor(30, 41, 59);
    doc.setFontSize(16);
    doc.text('Seances du programme', 14, y);
    y += 8;

    if (p.seances && p.seances.length > 0) {
      const rows = p.seances.map(s => [
        clean(s.titreSeance),
        s.dateSeance || '',
        `${s.dureeMinutes || 0} min`,
        s.intensite || '',
        s.statut || '',
        `${s.exercices?.length || 0} ex.`
      ]);

      autoTable(doc, {
        startY: y,
        head: [['Titre', 'Date', 'Duree', 'Intensite', 'Statut', 'Exercices']],
        body: rows,
        styles: { fontSize: 9, cellPadding: 4, lineColor: [226, 232, 240], lineWidth: 0.5 },
        headStyles: { fillColor: [37, 99, 235], textColor: 255, fontStyle: 'bold', cellPadding: 5 },
        alternateRowStyles: { fillColor: [248, 250, 252] },
        margin: { left: 14, right: 14 }
      });

      // Footer
      const finalY = (doc as any).lastAutoTable?.finalY || y + 40;
      doc.setFontSize(8);
      doc.setTextColor(148, 163, 184);
      doc.text(`Genere le ${new Date().toLocaleDateString('fr-FR')} - StreetLeague Coaching`, 14, finalY + 12);
    } else {
      doc.setFontSize(10);
      doc.setTextColor(148, 163, 184);
      doc.text('Aucune seance dans ce programme', 14, y + 6);
    }

    doc.save(`programme-${p.titre.replace(/\s+/g, '-').toLowerCase()}.pdf`);
  }
}
