import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { PlayerService } from '../../core/services/player.service';
import { PlayerPredictionService } from '../../core/services/player-prediction.service';
import { TeamService } from '../../core/services/team.service';
import { MatchService } from '../../core/services/match.service';
import { PlayerDTO, TeamDTO, MatchDTO, PlayerStatsDTO, PlayerPredictionDTO } from '../../shared/models/sports.model';

export interface ActivityFeedItem {
  id: string;
  type: 'GOAL' | 'MATCH' | 'AWARD' | 'LEVEL';
  content: string;
  time: string;
  icon: string;
}

export interface AdvancedPlayerMetric {
  code: string;
  label: string;
  value: number;
  tone: string;
}

@Component({
  selector: 'app-player-dashboard',
  templateUrl: './player-dashboard.component.html',
  styleUrls: ['./player-dashboard.component.css']
})
export class PlayerDashboardComponent implements OnInit {
  profileForm!: FormGroup;
  customPredictionForm!: FormGroup;
  activeTab: 'OVERVIEW' | 'ADVANCED' | 'PROFILE' | 'INVITATIONS' | 'STATS' = 'OVERVIEW';

  // Data from backend
  playerData: PlayerDTO | null = null;
  myTeam: TeamDTO | null = null;
  upcomingMatches: MatchDTO[] = [];
  matchHistory: MatchDTO[] = [];
  playerStats: PlayerStatsDTO[] = [];
  playerPrediction: PlayerPredictionDTO | null = null;
  customPredictionResult: any = null;
  predictionLoading = false;
  savingSimulation = false;
  savedSimulationId: number | null = null;

  loading = false;
  error: string | null = null;

  // Activity feed built from real match history
  get activityFeed(): ActivityFeedItem[] {
    return this.matchHistory.slice(0, 5).map(m => ({
      id: String(m.id),
      type: 'MATCH' as const,
      content: m.result
        ? `${m.homeTeamName} ${m.homeTeamScore} - ${m.awayTeamScore} ${m.awayTeamName}`
        : `${m.homeTeamName} vs ${m.awayTeamName}`,
      time: m.matchDate ? new Date(m.matchDate).toLocaleDateString('fr-FR') : '',
      icon: m.result === 'Home Win' || m.result === 'Away Win' ? '🏆' : '⚽'
    }));
  }

  // Stats computed from playerData + playerStats
  get stats() {
    const totalGoals = this.playerStats.reduce((s, p) => s + p.goals, 0);
    const totalAssists = this.playerStats.reduce((s, p) => s + p.assists, 0);
    const avgRating = this.playerStats.length
      ? this.playerStats.reduce((s, p) => s + p.performanceRating, 0) / this.playerStats.length
      : 0;
    const wins = this.matchHistory.filter(m =>
      (m.result === 'Home Win' && this.myTeam && m.homeTeamId === this.myTeam.id) ||
      (m.result === 'Away Win' && this.myTeam && m.awayTeamId === this.myTeam.id)
    ).length;
    const winRate = this.matchHistory.length
      ? Math.round((wins / this.matchHistory.length) * 100)
      : 0;

    return {
      matchesPlayed: this.playerData?.matchesPlayed ?? this.playerStats.length,
      goals: this.playerData?.totalGoals ?? totalGoals,
      assists: this.playerData?.totalAssists ?? totalAssists,
      rating: (this.playerData?.averageRating ?? avgRating).toFixed(1),
      winRate
    };
  }

  // Attribute bars derived from player stats
  get attributes() {
    const goals = this.stats.goals;
    const assists = this.stats.assists;
    const rating = parseFloat(String(this.stats.rating));
    return [
      { label: 'Buts', value: Math.min(goals * 5, 100), color: '#ef4444' },
      { label: 'Assists', value: Math.min(assists * 6, 100), color: '#10b981' },
      { label: 'Rating', value: Math.round(rating * 10), color: '#3b82f6' },
      { label: 'Matchs', value: Math.min(this.stats.matchesPlayed * 2, 100), color: '#f59e0b' }
    ];
  }

  get positionCode(): string {
    const map: Record<string, string> = {
      GOALKEEPER: 'GK',
      DEFENDER: 'DEF',
      MIDFIELDER: 'CM',
      FORWARD: 'ST',
      UTILITY: 'UTL'
    };
    return map[this.playerData?.position || ''] || 'ST';
  }

  get advancedOverall(): number {
    const predictionRating = this.customPredictionResult?.predictedPerformanceRating
      ?? this.playerPrediction?.predictedPerformanceRating;
    const averageRating = parseFloat(String(this.stats.rating || '0')) * 10;
    const levelBase = this.getLevelBaseRating();
    return this.clampRating(Math.round(Math.max(predictionRating || 0, averageRating || 0, levelBase)));
  }

  get advancedMetrics(): AdvancedPlayerMetric[] {
    const perMatch = this.getPerMatchAverages();
    const levelBase = this.getLevelBaseRating();
    const averageRating = parseFloat(String(this.stats.rating || '0')) * 10 || levelBase;

    const pace = this.clampRating((perMatch.averageSpeed * 2.5) + (perMatch.distanceCovered * 3.2) + this.levelBonus());
    const shooting = this.clampRating((perMatch.goals * 24) + (perMatch.predictedGoals * 10) + (averageRating * 0.45));
    const passing = this.clampRating((perMatch.assists * 22) + (perMatch.passesCompleted * 0.28) + (averageRating * 0.35));
    const dribbling = this.clampRating((averageRating * 0.72) + (this.stats.winRate * 0.12) + this.levelBonus());
    const defending = this.clampRating(((perMatch.tackles + perMatch.interceptions) * 7.5) + (averageRating * 0.35));
    const physical = this.clampRating((perMatch.distanceCovered * 5) + (perMatch.averageSpeed * 0.9) + (perMatch.minutesPlayed * 0.18));

    return [
      { code: 'PAC', label: 'Vitesse', value: pace, tone: '#38bdf8' },
      { code: 'SHO', label: 'Finition', value: shooting, tone: '#f97316' },
      { code: 'PAS', label: 'Passes', value: passing, tone: '#22c55e' },
      { code: 'DRI', label: 'Dribble', value: dribbling, tone: '#a855f7' },
      { code: 'DEF', label: 'Défense', value: defending, tone: '#2563eb' },
      { code: 'PHY', label: 'Physique', value: physical, tone: '#ef4444' }
    ];
  }

  get growthMetrics(): AdvancedPlayerMetric[] {
    return [
      { code: 'PHY', label: 'Physical', value: this.metricValue('PHY'), tone: '#22c55e' },
      { code: 'DEF', label: 'Defending', value: this.metricValue('DEF'), tone: '#16a34a' },
      { code: 'DRI', label: 'Dribbling', value: this.metricValue('DRI'), tone: '#22c55e' },
      { code: 'PAS', label: 'Passing', value: this.metricValue('PAS'), tone: '#14b8a6' },
      { code: 'SHO', label: 'Shooting', value: this.metricValue('SHO'), tone: '#f59e0b' },
      { code: 'PAC', label: 'Pace', value: this.metricValue('PAC'), tone: '#38bdf8' }
    ];
  }

  get advancedSummary() {
    const perMatch = this.getPerMatchAverages();
    const scenario = this.getActiveScenarioStats();
    return {
      matches: this.stats.matchesPlayed,
      goals: scenario.goals,
      assists: scenario.assists,
      averageRating: scenario.averageRating,
      minutes: Math.round(perMatch.minutesPlayed),
      distance: perMatch.distanceCovered.toFixed(1),
      topMetric: [...this.advancedMetrics].sort((a, b) => b.value - a.value)[0]
    };
  }

  get levelStars(): number[] {
    const level = this.playerData?.niveau;
    const count = level === 'ADVANCED' ? 5 : level === 'INTERMEDIATE' ? 4 : 3;
    return Array.from({ length: count }, (_, i) => i);
  }

  // Upcoming matches as "invitations"
  get invitations(): MatchDTO[] {
    return this.upcomingMatches;
  }

  constructor(
    private fb: FormBuilder,
    public authService: AuthService,
    private playerService: PlayerService,
    public predictionService: PlayerPredictionService,
    private teamService: TeamService,
    private matchService: MatchService
  ) {}

  ngOnInit(): void {
    const user = this.authService.user;
    this.profileForm = this.fb.group({
      name: [user?.username || '', Validators.required],
      username: [user?.username || '', Validators.required],
      email: [user?.email || '', [Validators.required, Validators.email]],
      position: ['MIDFIELDER', Validators.required],
      age: [''],
      niveau: ['BEGINNER'],
      preferredFoot: ['Right', Validators.required],
      bio: ['Passionné par le street football. Toujours prêt pour un nouveau défi !'],
      availability: ['Soirs & Week-ends']
    });

    // Custom Prediction Form
    this.customPredictionForm = this.fb.group({
      goals: [5, [Validators.required, Validators.min(0), Validators.max(20)]],
      assists: [3, [Validators.required, Validators.min(0), Validators.max(20)]],
      tackles: [8, [Validators.required, Validators.min(0), Validators.max(30)]],
      interceptions: [2, [Validators.required, Validators.min(0), Validators.max(20)]],
      passesCompleted: [120, [Validators.required, Validators.min(0), Validators.max(300)]],
      passAccuracy: [85, [Validators.required, Validators.min(0), Validators.max(100)]],
      distanceCoveredKm: [11, [Validators.required, Validators.min(0), Validators.max(20)]],
      averageSpeedKmh: [26, [Validators.required, Validators.min(0), Validators.max(40)]],
      ballPossessionPercent: [45, [Validators.required, Validators.min(0), Validators.max(100)]],
      foulsCommitted: [3, [Validators.required, Validators.min(0), Validators.max(20)]],
      shotsOnTarget: [4, [Validators.required, Validators.min(0), Validators.max(15)]]
    });

    this.loadData();
  }

  loadData(): void {
    this.loading = true;

    this.playerService.getMe().subscribe({
      next: (match) => {
        this.playerData = match;

          this.profileForm.patchValue({
            name: match.nom,
            email: match.email || this.authService.user?.email,
            position: match.position,
            age: match.age,
            niveau: match.niveau
          });

          // Load player's team
          if (match.equipeId) {
            this.teamService.getById(match.equipeId).subscribe({
              next: (team) => { this.myTeam = team; },
              error: () => {}
            });
          }

          // Load player stats
          this.playerService.getStatistics(match.id).subscribe({
            next: (stats) => { this.playerStats = stats; },
              error: () => { this.playerStats = []; }
            });

          // Load player prediction
          this.predictionService.getPrediction(match.id).subscribe({
            next: (prediction) => { this.playerPrediction = prediction; },
              error: () => { this.playerPrediction = null; }
            });

        this.loading = false;
      },
      error: (err) => {
        // Ne pas afficher d'erreur bloquante — le profil joueur est optionnel pour le rôle SPORTIF
        const role = this.authService.userRole;
        if (role === 'SPORTIF') {
          // Le SPORTIF n'a pas forcément un profil joueur (table joueur) — c'est normal
          this.playerData = null;
          this.error = null;
        } else {
          this.error = 'Profil joueur non trouvé. Si tu viens de créer ton compte, ton profil sera disponible sous peu.';
        }
        this.loading = false;
      }
    });

    // Load upcoming matches
    this.matchService.getScheduled().subscribe({
      next: (matches) => { this.upcomingMatches = matches; },
      error: () => { this.upcomingMatches = []; }
    });

    // Load match history for activity feed
    this.matchService.getHistory().subscribe({
      next: (matches) => { this.matchHistory = matches; },
      error: () => { this.matchHistory = []; }
    });
  }

  saveProfile(): void {
    if (!this.profileForm.valid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    if (!this.playerData?.id) {
      this.error = 'ID joueur non trouvé.';
      return;
    }

    const formValue = this.profileForm.value;
    const payload = {
      nom: formValue.name,
      age: Number(formValue.age || this.playerData.age),
      niveau: formValue.niveau,
      position: formValue.position,
      profilePicture: this.playerData.profilePicture,
      equipe: this.playerData.equipeId ? { id: this.playerData.equipeId } : undefined
    };

    this.playerService.update(this.playerData.id, payload).subscribe({
      next: (updatedPlayer) => {
        this.playerData = updatedPlayer;
        this.error = null;
        alert('Profil mis à jour avec succès !');
      },
      error: (err) => {
        this.error = `Erreur lors de la sauvegarde du profil: ${err.message || 'mise à jour impossible'}`;
      }
    });
  }

  /**
   * Submit custom player stats for AI prediction
   */
  submitCustomPrediction(): void {
    if (!this.customPredictionForm.valid) {
      this.error = 'Veuillez remplir tous les champs correctement.';
      return;
    }

    if (!this.playerData?.id) {
      this.error = 'ID joueur non trouvé.';
      return;
    }

    this.predictionLoading = true;
    this.customPredictionResult = null;
    this.savedSimulationId = null;
    this.error = null;

    const predictionRequest = {
      playerId: this.playerData.id,
      ...this.customPredictionForm.value
    };

    this.predictionService.predictWithCustomStats(predictionRequest).subscribe({
      next: (response) => {
        this.customPredictionResult = response;
        this.predictionLoading = false;
      },
      error: (err) => {
        this.error = `Erreur lors de la prédiction: ${err.message || 'Impossible de contacter le service IA'}`;
        this.predictionLoading = false;
      }
    });
  }

  saveCustomPredictionAsStats(): void {
    if (!this.playerData?.id) {
      this.error = 'ID joueur non trouvé.';
      return;
    }

    if (!this.customPredictionResult) {
      this.error = 'Lance une prédiction IA avant de sauvegarder.';
      return;
    }

    this.savingSimulation = true;
    this.error = null;

    const payload = {
      playerId: this.playerData.id,
      ...this.customPredictionForm.value,
      predictedPerformanceRating: this.customPredictionResult.predictedPerformanceRating
    };

    this.playerService.saveSimulationStats(payload).subscribe({
      next: (savedStats) => {
        this.savedSimulationId = savedStats.id;
        this.savingSimulation = false;
        this.reloadPlayerStats();
        alert('Performance IA sauvegardée dans les statistiques joueur.');
      },
      error: (err) => {
        this.error = `Erreur lors de la sauvegarde des stats: ${err.message || 'sauvegarde impossible'}`;
        this.savingSimulation = false;
      }
    });
  }

  dismissError(): void {
    this.error = null;
  }

  private metricValue(code: string): number {
    return this.advancedMetrics.find(metric => metric.code === code)?.value ?? this.getLevelBaseRating();
  }

  private reloadPlayerStats(): void {
    if (!this.playerData?.id) {
      return;
    }

    this.playerService.getStatistics(this.playerData.id).subscribe({
      next: (stats) => { this.playerStats = stats; },
      error: () => { this.playerStats = []; }
    });

    this.playerService.getMe().subscribe({
      next: (player) => { this.playerData = player; },
      error: () => {}
    });
  }

  private getPerMatchAverages() {
    const count = this.playerStats.length || 1;
    const totals = this.playerStats.reduce((acc, stat) => ({
      goals: acc.goals + stat.goals,
      assists: acc.assists + stat.assists,
      tackles: acc.tackles + stat.tackles,
      interceptions: acc.interceptions + stat.interceptions,
      passesCompleted: acc.passesCompleted + stat.passesCompleted,
      distanceCovered: acc.distanceCovered + stat.distanceCovered,
      averageSpeed: acc.averageSpeed + stat.averageSpeed,
      minutesPlayed: acc.minutesPlayed + stat.minutesPlayed
    }), {
      goals: 0,
      assists: 0,
      tackles: 0,
      interceptions: 0,
      passesCompleted: 0,
      distanceCovered: 0,
      averageSpeed: 0,
      minutesPlayed: 0
    });

    const scenario = this.getActiveScenarioStats();
    const fallback = {
      goals: scenario.goals,
      assists: scenario.assists,
      tackles: this.customPredictionForm?.value?.tackles || 4,
      interceptions: this.customPredictionForm?.value?.interceptions || 2,
      passesCompleted: this.customPredictionForm?.value?.passesCompleted || 45,
      distanceCovered: this.customPredictionForm?.value?.distanceCoveredKm || 9,
      averageSpeed: this.customPredictionForm?.value?.averageSpeedKmh || 23,
      minutesPlayed: 75,
      predictedGoals: this.playerPrediction?.predictedGoals || 0
    };

    if (!this.playerStats.length) {
      return fallback;
    }

    return {
      goals: totals.goals / count,
      assists: totals.assists / count,
      tackles: totals.tackles / count,
      interceptions: totals.interceptions / count,
      passesCompleted: totals.passesCompleted / count,
      distanceCovered: totals.distanceCovered / count,
      averageSpeed: totals.averageSpeed / count,
      minutesPlayed: totals.minutesPlayed / count,
      predictedGoals: this.playerPrediction?.predictedGoals || 0
    };
  }

  private getActiveScenarioStats() {
    const hasCustomPrediction = !!this.customPredictionResult;
    const hasMatchHistory = this.playerStats.length > 0;
    const formValue = this.customPredictionForm?.value || {};

    if (hasCustomPrediction || !hasMatchHistory) {
      return {
        goals: Number(formValue.goals ?? 0),
        assists: Number(formValue.assists ?? 0),
        averageRating: hasCustomPrediction
          ? (this.customPredictionResult.predictedPerformanceRating / 10).toFixed(1)
          : this.stats.rating
      };
    }

    return {
      goals: this.stats.goals,
      assists: this.stats.assists,
      averageRating: this.stats.rating
    };
  }

  private getLevelBaseRating(): number {
    const level = this.playerData?.niveau;
    if (level === 'ADVANCED') return 82;
    if (level === 'INTERMEDIATE') return 72;
    return 62;
  }

  private levelBonus(): number {
    const level = this.playerData?.niveau;
    if (level === 'ADVANCED') return 14;
    if (level === 'INTERMEDIATE') return 8;
    return 3;
  }

  private clampRating(value: number): number {
    return Math.max(1, Math.min(99, Math.round(value || 0)));
  }
}
