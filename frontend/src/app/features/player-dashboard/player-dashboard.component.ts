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

@Component({
  selector: 'app-player-dashboard',
  templateUrl: './player-dashboard.component.html',
  styleUrls: ['./player-dashboard.component.css']
})
export class PlayerDashboardComponent implements OnInit {
  profileForm!: FormGroup;
  activeTab: 'OVERVIEW' | 'PROFILE' | 'INVITATIONS' | 'STATS' = 'OVERVIEW';

  // Data from backend
  playerData: PlayerDTO | null = null;
  myTeam: TeamDTO | null = null;
  upcomingMatches: MatchDTO[] = [];
  matchHistory: MatchDTO[] = [];
  playerStats: PlayerStatsDTO[] = [];
  playerPrediction: PlayerPredictionDTO | null = null;

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
      error: () => {
        this.error = 'Impossible de charger ton profil joueur. Vérifie que ton compte a été créé avec le rôle Joueur.';
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
    if (this.profileForm.valid) {
      alert('Profil mis à jour avec succès !');
    }
  }

  dismissError(): void {
    this.error = null;
  }
}
