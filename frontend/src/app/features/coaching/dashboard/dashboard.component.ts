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

  get todayStr(): string { return new Date().toISOString().split('T')[0]; }

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

  /** Séances du jour (calendrier) */
  get seancesAujourdhui(): SeanceEntrainement[] {
    const today = new Date().toISOString().split('T')[0];
    return this.seances.filter(s => s.dateSeance === today && s.statut === 'PREVUE')
      .sort((a, b) => (a.heureDebut || '').localeCompare(b.heureDebut || ''));
  }

  /** Séances cette semaine */
  get seancesCetteSemaine(): SeanceEntrainement[] {
    const today = new Date();
    const endOfWeek = new Date(today);
    endOfWeek.setDate(today.getDate() + 7);
    const todayStr = today.toISOString().split('T')[0];
    const endStr = endOfWeek.toISOString().split('T')[0];
    return this.seances.filter(s => s.dateSeance && s.dateSeance >= todayStr && s.dateSeance <= endStr && s.statut === 'PREVUE')
      .sort((a, b) => (a.dateSeance || '').localeCompare(b.dateSeance || ''));
  }

  /** Réservations en attente de confirmation */
  get reservationsEnAttente(): { seanceTitre: string; userNom: string; idReservation: number; seanceId: number }[] {
    const pending: any[] = [];
    for (const s of this.seances) {
      if (s.reservations) {
        for (const r of s.reservations) {
          if (r.statut === 'RESERVEE') {
            pending.push({
              seanceTitre: s.titreSeance,
              userNom: r.userNom || 'Sportif #' + r.userId,
              idReservation: r.idReservation,
              seanceId: s.idSeance
            });
          }
        }
      }
    }
    return pending;
  }

  /** Total sportifs inscrits (réservations actives) */
  get totalSportifsInscrits(): number {
    let count = 0;
    for (const s of this.seances) {
      if (s.reservations) {
        count += s.reservations.filter(r => r.statut !== 'ANNULEE').length;
      }
    }
    return count;
  }

  /** Nombre total de feedbacks reçus */
  get totalFeedbacks(): number {
    return this.seances.filter(s => s.suiviSeance).length;
  }

  /** Moyenne des notes (1-5 étoiles) */
  get moyenneNote(): string {
    const notes = this.seances
      .filter(s => s.suiviSeance && (s.suiviSeance as any).note)
      .map(s => (s.suiviSeance as any).note);
    if (notes.length === 0) return '—';
    return (notes.reduce((a: number, b: number) => a + b, 0) / notes.length).toFixed(1);
  }

  /** Verdict global basé sur les feedbacks */
  get verdictGlobal(): string {
    const note = parseFloat(this.moyenneNote);
    if (isNaN(note)) return 'AUCUN';
    if (note >= 4.5) return 'EXCELLENT';
    if (note >= 3.5) return 'BON';
    if (note >= 2.5) return 'MOYEN';
    return 'FAIBLE';
  }

  get verdictDescription(): string {
    switch (this.verdictGlobal) {
      case 'EXCELLENT': return 'Les sportifs adorent vos séances !';
      case 'BON': return 'Bonne satisfaction générale';
      case 'MOYEN': return 'Des améliorations possibles';
      case 'FAIBLE': return 'Attention — revoir le contenu des séances';
      default: return 'En attente de feedbacks';
    }
  }

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

  getDayLabel(dateStr: string): string {
    const days = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];
    const d = new Date(dateStr);
    return days[d.getDay()] + ' ' + d.getDate();
  }

  getParticipants(seance: SeanceEntrainement): number {
    if (!seance.reservations) return 0;
    return seance.reservations.filter(r => r.statut !== 'ANNULEE').length;
  }
}
