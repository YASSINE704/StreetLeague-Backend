import { Component, OnInit } from '@angular/core';
import { ProgrammeService } from '../../../core/services/programme.service';
import { SeanceService } from '../../../core/services/seance.service';
import { ExerciceService } from '../../../core/services/exercice.service';
import { ProgrammeEntrainement, SeanceEntrainement, Exercice } from '../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  programmes: ProgrammeEntrainement[] = [];
  seances: SeanceEntrainement[] = [];
  exercices: Exercice[] = [];

  constructor(
    private programmeService: ProgrammeService,
    private seanceService: SeanceService,
    private exerciceService: ExerciceService
  ) {}

  ngOnInit(): void {
    this.programmeService.getAll().subscribe({ next: d => this.programmes = d });
    this.seanceService.getAll().subscribe({ next: d => this.seances = d });
    this.exerciceService.getAll().subscribe({ next: d => this.exercices = d });
  }

  get totalProgrammes(): number { return this.programmes.length; }
  get programmesActifs(): number { return this.programmes.filter(p => p.statut === 'ACTIF').length; }
  get totalSeances(): number { return this.seances.length; }
  get seancesRealisees(): number { return this.seances.filter(s => s.statut === 'REALISEE').length; }
  get seancesPrevues(): number { return this.seances.filter(s => s.statut === 'PREVUE').length; }
  get totalExercices(): number { return this.exercices.length; }

  get tauxCompletion(): number {
    if (this.totalSeances === 0) return 0;
    return Math.round((this.seancesRealisees / this.totalSeances) * 100);
  }

  get moyenneRessenti(): string {
    const suivis = this.seances.filter(s => s.suiviSeance).map(s => s.suiviSeance!.ressenti!);
    if (suivis.length === 0) return '—';
    return (suivis.reduce((a, b) => a + b, 0) / suivis.length).toFixed(1);
  }

  get moyenneFatigue(): string {
    const suivis = this.seances.filter(s => s.suiviSeance).map(s => s.suiviSeance!.fatigue!);
    if (suivis.length === 0) return '—';
    return (suivis.reduce((a, b) => a + b, 0) / suivis.length).toFixed(1);
  }

  get recentSeances(): SeanceEntrainement[] {
    return [...this.seances].sort((a, b) => (b.dateSeance || '').localeCompare(a.dateSeance || '')).slice(0, 5);
  }

  get recentProgrammes(): ProgrammeEntrainement[] {
    return [...this.programmes].slice(0, 3);
  }

  get seancesAnnulees(): number { return this.seances.filter(s => s.statut === 'ANNULEE').length; }

  get exercicesByType(): { type: string; count: number; icon: string; color: string }[] {
    const types = ['FORCE', 'CARDIO', 'MOBILITE', 'TECHNIQUE'];
    const icons: any = { FORCE: '🏋️', CARDIO: '🏃', MOBILITE: '🧘', TECHNIQUE: '⚡' };
    const colors: any = { FORCE: '#dc2626', CARDIO: '#16a34a', MOBILITE: '#7c3aed', TECHNIQUE: '#2563eb' };
    return types.map(t => ({
      type: t,
      count: this.exercices.filter(e => e.type === t).length,
      icon: icons[t],
      color: colors[t]
    })).filter(t => t.count > 0);
  }
}
