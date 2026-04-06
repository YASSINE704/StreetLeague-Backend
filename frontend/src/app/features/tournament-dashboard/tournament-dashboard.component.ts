import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { TeamService } from '../../core/services/team.service';
import { MatchService } from '../../core/services/match.service';
import { TerrainService } from '../../core/services/terrain.service';
import { PlayerService } from '../../core/services/player.service';
import { TeamDTO, MatchDTO, TerrainDTO } from '../../shared/models/sports.model';

export type TabType = 'overview' | 'bracket' | 'teams' | 'standings' | 'stadium-mgmt' | 'player-mgmt' | 'match-mgmt';

export type SkillLevel = 'Débutant' | 'Intermédiaire' | 'Pro';

export interface Player {
  id: number;
  name: string;
  number: number;
  age?: number;
  skillLevel?: SkillLevel;
  position: string;
  posGroup: 'GK' | 'DEF' | 'MID' | 'FWD';
  goals: number;
  assists: number;
  appearances: number;
  rating: number; // 1-10
  teamId?: number; // Optional reference to team
}

export interface Team {
  id: number;
  name: string;
  logo: string;
  color: string;
  sportType?: string;
  players: Player[];
  // These will be calculated dynamically, but kept optional for compatibility
  wins?: number;
  draws?: number;
  losses?: number;
  goalsFor?: number;
  goalsAgainst?: number;
}

export interface MatchEvent {
  playerId: number;
  playerName: string;
  type: 'goal' | 'assist' | 'yellow' | 'red';
  minute?: number;
  teamId: number;
}

export interface MatchStats {
  possession1: number;
  possession2: number;
  shots1: number;
  shots2: number;
  fouls1: number;
  fouls2: number;
}

export interface BracketSlot {
  id: string; round: 'QF' | 'SF' | 'F';
  team1: string | null; team2: string | null;
  seed1: number | null; seed2: number | null;
  score1: number | null; score2: number | null;
  winner: string | null; date: string; time: string;
  venue: string;
  durationMinutes?: number;
  status: 'tbd' | 'upcoming' | 'live' | 'completed';
  nextMatchId: string | null; nextSlot: 1 | 2 | null;
  matchId?: number; // Link to Scheduled Match
  events?: MatchEvent[];
  stats?: MatchStats;
}

export interface TournamentBracket {
  id: string;
  name: string;
  color: string;
  category: string;
  status: 'Active' | 'Completed' | 'Draft';
  dateCreated: string;
  data: { [key: string]: BracketSlot };
  champion: string | null;
}

export interface Stadium {
  id: number;
  name: string;
  capacity: number;
  surface: string;
  district: string;
  lighting: string;
  yearBuilt: number;
  description: string;
  icon: string;
  supportedSports: string[];
}

export interface Match {
  id: number;
  team1: string;
  team2: string;
  date: string;
  time: string;
  round: string;
  venue: string;
  durationMinutes?: number;
  status: 'tbd' | 'upcoming' | 'live' | 'completed';
  score1?: number;
  score2?: number;
  matchDay?: number;
  competition?: string;
  bracketSlotId?: string; // Link to BracketSlot.id
  bracketId?: string; // Link to TournamentBracket.id
}

export interface ActivityItem {
  icon: string; msg: string; sub: string; time: string;
  type: 'complete' | 'advance' | 'info' | 'simulate' | 'reset';
  color: string;
}

@Component({
  selector: 'app-tournament-dashboard',
  templateUrl: './tournament-dashboard.component.html',
  styleUrls: ['./tournament-dashboard.component.css']
})
export class TournamentDashboardComponent implements OnInit {
  get tournamentName(): string { return this.activeBracket?.name || 'Tournament Hub'; }

  get currentRound(): string {
    if (!this.activeBracket) return 'Quarter-Finals';
    if (this.activeBracket.champion) return 'Championnat Terminé';
    const slots = Object.values(this.activeBracket.data);
    const f = slots.find(s => s.round === 'F');
    if (f?.status === 'completed') return 'Championnat Terminé';
    if (f?.status === 'live' || f?.status === 'upcoming') return 'Finale';
    const sf = slots.filter(s => s.round === 'SF');
    if (sf.some(s => s.status === 'live' || s.status === 'upcoming' || s.status === 'completed')) {
        if (sf.every(s => s.status === 'completed')) return 'Finale';
        return 'Semi-Finals';
    }
    return 'Quarter-Finals';
  }

  get totalRounds(): number { return 3; }
  
  get currentRoundNumber(): number {
    if (!this.activeBracket) return 1;
    const slots = Object.values(this.activeBracket.data);
    const completed = slots.filter(s => s.status === 'completed').length;
    if (completed >= 6) return 3; // Final reached
    if (completed >= 4) return 2; // SF reached
    return 1;
  }

  get totalTeamsCount(): number {
    const qf = Object.values(this.activeBracket.data).filter(s => s.round === 'QF');
    const teams = new Set<string>();
    qf.forEach(s => { if(s.team1) teams.add(s.team1); if(s.team2) teams.add(s.team2); });
    return teams.size || 8;
  }
  activeTab: TabType = 'overview';
  selectedMatchId: string | null = null;
  highlightTeam: string | null = null;
  teamSearch = '';
  teamSortBy: 'winrate' | 'wins' | 'goals' | 'name' = 'winrate';
  showResetConfirm = false;

  selectedRosterTeam: Team | null = null;
  rosterFilter: 'ALL' | 'GK' | 'DEF' | 'MID' | 'FWD' = 'ALL';

  // ── Bracket Management State ───────────────────────────
  activeBracketId = 'mens';
  bracketSearch = '';
  bracketStatusFilter: 'ALL' | 'Active' | 'Completed' | 'Draft' = 'ALL';
  showCreateTournamentModal = false;

  newBracketForm = {
    name: '',
    category: 'Pro League',
    color: '#2563eb',
    teamCount: 8
  };

  brackets: TournamentBracket[] = [
    {
      id: 'mens', name: 'Men\'s Pro League', color: 'linear-gradient(135deg, #2563eb, #3b82f6)', category: 'Pro League', status: 'Draft', dateCreated: '2025-03-01', champion: null,
      data: this.createEmptyBracketData('mens')
    },
    {
      id: 'womens', name: 'Women\'s Elite', color: 'linear-gradient(135deg, #ec4899, #f472b6)', category: 'Elite', status: 'Draft', dateCreated: '2025-03-05', champion: null,
      data: this.createEmptyBracketData('womens')
    },
    {
      id: 'u21', name: 'Under-21 Cup', color: 'linear-gradient(135deg, #10b981, #34d399)', category: 'Youth', status: 'Draft', dateCreated: '2025-03-10', champion: null,
      data: this.createEmptyBracketData('u21')
    }
  ];

  private createEmptyBracketData(id: string): { [key: string]: BracketSlot } {
    return {
      'qf1': { id:'qf1', round:'QF', team1:null, team2:null, seed1:1, seed2:8, score1:null, score2:null, winner:null, date:'TBD', time:'18:00', venue:'TBD', status:'tbd', nextMatchId:'sf1', nextSlot:1, durationMinutes: 90 },
      'qf2': { id:'qf2', round:'QF', team1:null, team2:null, seed1:4, seed2:5, score1:null, score2:null, winner:null, date:'TBD', time:'20:00', venue:'TBD', status:'tbd', nextMatchId:'sf1', nextSlot:2, durationMinutes: 90 },
      'qf3': { id:'qf3', round:'QF', team1:null, team2:null, seed1:3, seed2:6, score1:null, score2:null, winner:null, date:'TBD', time:'15:00', venue:'TBD', status:'tbd', nextMatchId:'sf2', nextSlot:1, durationMinutes: 90 },
      'qf4': { id:'qf4', round:'QF', team1:null, team2:null, seed1:2, seed2:7, score1:null, score2:null, winner:null, date:'TBD', time:'17:30', venue:'TBD', status:'tbd', nextMatchId:'sf2', nextSlot:2, durationMinutes: 90 },
      'sf1': { id:'sf1', round:'SF', team1:null, team2:null, seed1:null, seed2:null, score1:null, score2:null, winner:null, date:'TBD', time:'18:00', venue:'TBD', status:'tbd', nextMatchId:'f1', nextSlot:1, durationMinutes: 90 },
      'sf2': { id:'sf2', round:'SF', team1:null, team2:null, seed1:null, seed2:null, score1:null, score2:null, winner:null, date:'TBD', time:'20:00', venue:'TBD', status:'tbd', nextMatchId:'f1', nextSlot:2, durationMinutes: 90 },
      'f1':  { id:'f1',  round:'F',  team1:null, team2:null, seed1:null, seed2:null, score1:null, score2:null, winner:null, date:'TBD', time:'19:00', venue:'TBD', status:'tbd', nextMatchId:null, nextSlot:null, durationMinutes: 90 },
    };
  }

  startTournament(): void {
    if (!this.activeBracket) return;
    if (this.activeBracket.status !== 'Draft') {
      alert('Ce tournoi a déjà commencé !');
      return;
    }

    // SEEDING ALGORITHM
    const sortedTeams = [...this.teams].sort((a, b) => {
      const pA = (a.wins || 0) * 3 + (a.draws || 0);
      const pB = (b.wins || 0) * 3 + (b.draws || 0);
      return pB - pA;
    });

    if (sortedTeams.length < 8) {
      alert('Il faut au moins 8 équipes pour démarrer le tournoi !');
      return;
    }

    const b = this.activeBracket;
    const s = sortedTeams;

    // Seeding: 1v8, 4v5, 3v6, 2v7
    b.data['qf1'].team1 = s[0].name; b.data['qf1'].team2 = s[7].name;
    b.data['qf2'].team1 = s[3].name; b.data['qf2'].team2 = s[4].name;
    b.data['qf3'].team1 = s[2].name; b.data['qf3'].team2 = s[5].name;
    b.data['qf4'].team1 = s[1].name; b.data['qf4'].team2 = s[6].name;

    // Set venue/date for QF
    Object.values(b.data).forEach(slot => {
      if (slot.round === 'QF') {
        slot.status = 'upcoming';
        slot.date = new Date().toLocaleDateString('fr-FR', { month: 'short', day: 'numeric' });
        slot.venue = this.stadiumsList[Math.floor(Math.random() * this.stadiumsList.length)].name;
      }
    });

    b.status = 'Active';
    this.setTab('bracket');
    
    this.activityFeed.unshift({
      icon: '🏆',
      msg: `Tournament ${b.name} started!`,
      sub: `Top seeds matched by performance: ${s[0].name} (1) vs ${s[7].name} (8)...`,
      time: 'Just now',
      type: 'info',
      color: b.color
    });
  }

  get filteredBrackets(): TournamentBracket[] {
    const q = this.bracketSearch.toLowerCase();
    return this.brackets.filter(b => {
      const matchSearch = b.name.toLowerCase().includes(q) || b.category.toLowerCase().includes(q);
      const matchFilter = this.bracketStatusFilter === 'ALL' || b.status === this.bracketStatusFilter;
      return matchSearch && matchFilter;
    }).sort((a,b) => b.dateCreated.localeCompare(a.dateCreated));
  }

  get activeBracket(): TournamentBracket {
    return this.brackets.find(b => b.id === this.activeBracketId) || this.brackets[0];
  }

  get bracketData(): { [key: string]: BracketSlot } {
    return this.activeBracket.data;
  }

  get champion(): string | null {
    return this.activeBracket.champion;
  }

  set champion(name: string | null) {
    this.activeBracket.champion = name;
  }

  // ── Player Management State ───────────────────────────
  allPlayers: Player[] = [];
  playerSearch = '';
  showPlayerModal = false;
  editingPlayer: Partial<Player> | null = null;
  playerSkillFilter: 'ALL' | SkillLevel = 'ALL';
  playerSortBy: 'name' | 'goals' | 'assists' | 'rating' = 'name';

  // ── Team Management State ─────────────────────────────
  showTeamModal = false;
  editingTeam: Partial<Team> | null = null;
  teamSportTypes = ['Football', 'Basketball', 'Handball', 'Volleyball', 'Tennis'];

  // ── Match Reporting State ──────────────────────────
  showResultModal = false;
  reportingMatchId: string | null = null;
  resultForm = {
    score1: 0,
    score2: 0,
    durationMinutes: 90,
    winner: '' as string,
    events: [] as MatchEvent[],
    stats: {
      possession1: 50, possession2: 50,
      shots1: 0, shots2: 0,
      fouls1: 0, fouls2: 0
    }
  };

  // ── Stadium Management State ──────────────────────────
  stadiumSearch = '';
  selectedStadiumDetails: Stadium | null = null;
  showStadiumDetailsModal = false;
  showStadiumModal = false;
  editingStadium: Partial<Stadium> | null = null;

  // ── Match Management State ──────────────────────────
  showMatchModal = false;
  editingMatch: Partial<Match> | null = null;
  matchSearch = '';
  private _manualMatches: Match[] = [];
  get matchesList(): Match[] {
    const bracketMatches = this.getBracketMatches();
    return [...this._manualMatches, ...bracketMatches];
  }
  set matchesList(val: Match[]) {
    // Filter out matches that originate from brackets to avoid duplication
    this._manualMatches = val.filter(m => !m.bracketSlotId);
  }

  private getBracketMatches(): Match[] {
    if (!this.activeBracket) return [];
    const matches: Match[] = [];
    Object.values(this.activeBracket.data).forEach(slot => {
      if (slot.team1 && slot.team2) {
        matches.push({
          id: parseInt(slot.id.replace(/\D/g, '') || '0') + 10000, 
          team1: slot.team1,
          team2: slot.team2,
          score1: slot.score1 ?? undefined,
          score2: slot.score2 ?? undefined,
          status: slot.status as any,
          date: slot.date,
          time: slot.time,
          round: slot.round,
          venue: slot.venue,
          matchDay: 1,
          competition: this.activeBracket.name,
          bracketSlotId: slot.id
        });
      }
    });
    return matches;
  }

  teams: Team[] = [];

  constructor(
    public authService: AuthService,
    private teamService: TeamService,
    private matchService: MatchService,
    private terrainService: TerrainService,
    private playerService: PlayerService
  ) {
    this.initMatches();
  }

  ngOnInit(): void {
    this.loadTeams();
    this.loadPlayers();
    this.loadMatches();
    this.loadTerrains();
  }

  // ── Backend Data Loading ──────────────────────────────────

  private loadTeams(): void {
    this.teamService.getAll().subscribe({
      next: (dtos) => {
        const gradients = [
          'linear-gradient(135deg,#f97316,#ef4444)',
          'linear-gradient(135deg,#0ea5e9,#2563eb)',
          'linear-gradient(135deg,#10b981,#059669)',
          'linear-gradient(135deg,#6366f1,#4338ca)',
          'linear-gradient(135deg,#f43f5e,#e11d48)',
          'linear-gradient(135deg,#8b5cf6,#7c3aed)',
          'linear-gradient(135deg,#64748b,#475569)',
          'linear-gradient(135deg,#ec4899,#be185d)',
        ];
        this.teams = dtos.map((dto, i) => ({
          id: dto.id,
          name: dto.nom,
          logo: dto.nom.charAt(0).toUpperCase(),
          color: gradients[i % gradients.length],
          sportType: dto.typeSport,
          players: (dto.joueurs || []).map(p => ({
            id: p.id,
            name: p.nom,
            number: p.id,
            age: p.age,
            position: p.position,
            posGroup: this.mapPosGroup(p.position),
            goals: p.totalGoals,
            assists: p.totalAssists,
            appearances: p.matchesPlayed,
            rating: p.averageRating || 5.0,
            teamId: dto.id,
            skillLevel: (p.averageRating >= 8.5 ? 'Pro' : p.averageRating >= 7.5 ? 'Intermédiaire' : 'Débutant') as SkillLevel
          })),
          wins: dto.statistics?.wins ?? 0,
          draws: dto.statistics?.draws ?? 0,
          losses: dto.statistics?.losses ?? 0,
          goalsFor: dto.statistics?.goalsFor ?? 0,
          goalsAgainst: dto.statistics?.goalsAgainst ?? 0
        }));
        this.initAllPlayers();
      },
      error: () => {}
    });
  }

  private loadPlayers(): void {
    this.playerService.getAll().subscribe({
      next: (dtos) => {
        this.allPlayers = dtos.map(p => ({
          id: p.id,
          name: p.nom,
          number: p.id,
          age: p.age,
          position: p.position,
          posGroup: this.mapPosGroup(p.position),
          goals: p.totalGoals,
          assists: p.totalAssists,
          appearances: p.matchesPlayed,
          rating: p.averageRating || 5.0,
          teamId: p.equipeId ?? undefined,
          skillLevel: (p.averageRating >= 8.5 ? 'Pro' : p.averageRating >= 7.5 ? 'Intermédiaire' : 'Débutant') as SkillLevel
        }));
      },
      error: () => {}
    });
  }

  private mapPosGroupToPosition(posGroup: string): string {
    switch (posGroup) {
      case 'GK': return 'GOALKEEPER';
      case 'DEF': return 'DEFENDER';
      case 'MID': return 'MIDFIELDER';
      case 'FWD': return 'FORWARD';
      default: return 'MIDFIELDER';
    }
  }

  private mapSkillLevel(level: string): string {
    switch (level) {
      case 'Pro': return 'ADVANCED';
      case 'Intermédiaire': return 'INTERMEDIATE';
      default: return 'BEGINNER';
    }
  }

  private mapPosGroup(position: string): 'GK' | 'DEF' | 'MID' | 'FWD' {
    switch (position) {
      case 'GOALKEEPER': return 'GK';
      case 'DEFENDER': return 'DEF';
      case 'MIDFIELDER': return 'MID';
      case 'FORWARD': return 'FWD';
      default: return 'MID';
    }
  }

  private loadMatches(): void {
    // Load ALL matches into _manualMatches for the match management tab
    this.matchService.getAll().subscribe({
      next: (dtos) => {
        this._manualMatches = dtos.map(m => ({
          id: m.id,
          team1: m.homeTeamName,
          team2: m.awayTeamName,
          date: m.matchDate ? new Date(m.matchDate).toLocaleDateString('fr-FR', { month: 'short', day: 'numeric' }) : 'TBD',
          time: m.matchDate ? new Date(m.matchDate).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' }) : '18:00',
          round: 'Match',
          venue: m.terrainName || 'TBD',
          status: this.mapMatchStatus(m.status),
          score1: m.homeTeamScore ?? undefined,
          score2: m.awayTeamScore ?? undefined,
          durationMinutes: 90
        }));
      },
      error: () => {}
    });

    // Also populate overview upcoming/recent lists
    this.matchService.getScheduled().subscribe({
      next: (matches) => {
        this.upcomingMatches = matches.map(m => ({
          id: m.id,
          team1: m.homeTeamName,
          team2: m.awayTeamName,
          time: m.matchDate ? new Date(m.matchDate).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' }) : '18:00',
          date: m.matchDate ? new Date(m.matchDate).toLocaleDateString('fr-FR', { month: 'short', day: 'numeric' }) : 'TBD',
          round: 'Match',
          venue: m.terrainName || 'TBD'
        }));
      },
      error: () => {}
    });

    this.matchService.getHistory().subscribe({
      next: (matches) => {
        this.recentResults = matches.slice(0, 6).map(m => ({
          id: m.id,
          team1: m.homeTeamName,
          team2: m.awayTeamName,
          score1: m.homeTeamScore ?? 0,
          score2: m.awayTeamScore ?? 0,
          date: m.matchDate ? new Date(m.matchDate).toLocaleDateString('fr-FR', { month: 'short', day: 'numeric' }) : '',
          round: 'Match'
        }));
      },
      error: () => {}
    });
  }

  private mapMatchStatus(status: string): 'tbd' | 'upcoming' | 'live' | 'completed' {
    switch (status) {
      case 'SCHEDULED': return 'upcoming';
      case 'IN_PROGRESS': return 'live';
      case 'COMPLETED': return 'completed';
      case 'CANCELLED': return 'tbd';
      default: return 'upcoming';
    }
  }

  private loadTerrains(): void {
    this.terrainService.getAll().subscribe({
      next: (dtos) => {
        this.stadiumsList = dtos.map((dto, i) => ({
          id: dto.id,
          name: dto.nom,
          capacity: 500,
          surface: dto.typeSport,
          district: dto.location,
          lighting: 'Standard',
          yearBuilt: 2020,
          description: `${dto.nom} — ${dto.address}`,
          icon: ['🏟️', '⚽', '🌿', '🏃', '🥅', '🏆'][i % 6],
          supportedSports: [dto.typeSport]
        }));
      },
      error: () => {}
    });
  }

  private initMatches(): void {
    // Collect all matches from all brackets
    const bracketMatches: Match[] = [];
    this.brackets.forEach((b, bIdx) => {
      Object.keys(b.data).forEach((key, index) => {
        const slot = b.data[key];
        slot.matchId = 200 + (bIdx * 100) + index;
        bracketMatches.push({
          id: slot.matchId,
          team1: slot.team1 || 'TBD',
          team2: slot.team2 || 'TBD',
          date: slot.date,
          time: slot.time,
          round: slot.round,
          venue: slot.venue,
          status: slot.status === 'completed' ? 'completed' : 'upcoming',
          score1: slot.score1 !== null ? slot.score1 : undefined,
          score2: slot.score2 !== null ? slot.score2 : undefined,
          bracketSlotId: key,
          bracketId: b.id
        } as Match);
      });
    });
    // Note: matchesList getter will handle merging with brackets
  }

  private initAllPlayers(): void {
    // Collect all players from teams and assign teamId
    this.allPlayers = [];
    this.teams.forEach(t => {
      t.players.forEach(p => {
        const enrichedPlayer = {
          ...p,
          teamId: t.id,
          age: Math.floor(Math.random() * 15) + 18, // Mock age
          skillLevel: (p.rating >= 8.5 ? 'Pro' : p.rating >= 7.5 ? 'Intermédiaire' : 'Débutant') as SkillLevel
        };
        // Update the original player in the team too
        Object.assign(p, enrichedPlayer);
        this.allPlayers.push(enrichedPlayer as Player);
      });
    });
  }

  // ── Player CRUD Methods ───────────────────────────────
  
  openAddPlayer(): void {
    this.editingPlayer = {
      name: '',
      age: 20,
      skillLevel: 'Débutant',
      position: 'MID',
      posGroup: 'MID',
      number: 10,
      goals: 0,
      assists: 0,
      appearances: 0,
      rating: 5.0
    };
    this.showPlayerModal = true;
  }

  editPlayer(p: Player): void {
    this.editingPlayer = { ...p };
    this.showPlayerModal = true;
  }

  savePlayer(): void {
    if (!this.editingPlayer || !this.editingPlayer.name) return;

    const payload: any = {
      nom: this.editingPlayer.name,
      age: this.editingPlayer.age || 20,
      position: this.mapPosGroupToPosition(this.editingPlayer.posGroup || 'MID'),
      niveau: this.mapSkillLevel(this.editingPlayer.skillLevel || 'Débutant')
    };
    if (this.editingPlayer.teamId) {
      payload.equipe = { id: this.editingPlayer.teamId };
    }

    if (this.editingPlayer.id) {
      // UPDATE
      this.playerService.update(this.editingPlayer.id, payload).subscribe({
        next: (dto) => {
          const idx = this.allPlayers.findIndex(p => p.id === this.editingPlayer!.id);
          if (idx !== -1) {
            const updated: Player = {
              ...this.allPlayers[idx],
              name: dto.nom,
              age: dto.age,
              position: dto.position,
              posGroup: this.mapPosGroup(dto.position),
              teamId: dto.equipeId ?? undefined
            };
            this.allPlayers[idx] = updated;
            // sync team roster
            this.teams.forEach(t => {
              const pi = t.players.findIndex(p => p.id === updated.id);
              if (pi !== -1) t.players[pi] = updated;
            });
          }
          this.closePlayerModal();
        },
        error: (err) => alert('Erreur mise à jour joueur: ' + (err?.error?.message || err.message))
      });
    } else {
      // CREATE
      this.playerService.create(payload).subscribe({
        next: (dto) => {
          const newPlayer: Player = {
            id: dto.id,
            name: dto.nom,
            number: Math.floor(Math.random() * 99) + 1,
            age: dto.age,
            position: dto.position,
            posGroup: this.mapPosGroup(dto.position),
            goals: 0, assists: 0, appearances: 0,
            rating: 5.0,
            teamId: dto.equipeId ?? undefined,
            skillLevel: this.mapSkillLevel(this.editingPlayer!.skillLevel || 'Débutant') as any
          };
          this.allPlayers = [...this.allPlayers, newPlayer];
          if (newPlayer.teamId) {
            const team = this.teams.find(t => t.id === newPlayer.teamId);
            if (team) team.players = [...team.players, newPlayer];
            // also call backend to assign player to team
            this.teamService.addPlayer(newPlayer.teamId, newPlayer.id).subscribe({ error: () => {} });
          }
          this.closePlayerModal();
        },
        error: (err) => alert('Erreur création joueur: ' + (err?.error?.message || err.message))
      });
    }
  }

  deletePlayer(id: number): void {
    const p = this.allPlayers.find(pl => pl.id === id);
    if (!p) return;
    if (!confirm(`Voulez-vous vraiment supprimer ${p.name}?`)) return;

    this.playerService.delete(id).subscribe({
      next: () => {
        this.allPlayers = this.allPlayers.filter(pl => pl.id !== id);
        if (p.teamId) {
          const team = this.teams.find(t => t.id === p.teamId);
          if (team) team.players = team.players.filter(pl => pl.id !== id);
        }
      },
      error: (err) => alert('Erreur suppression joueur: ' + (err?.error?.message || err.message))
    });
  }

  closePlayerModal(): void {
    this.showPlayerModal = false;
    this.editingPlayer = null;
  }

  // ── Team CRUD Methods ────────────────────────────────
  
  openAddTeam(): void {
    const gradients = [
      'linear-gradient(135deg,#f97316,#ef4444)',
      'linear-gradient(135deg,#0ea5e9,#2563eb)',
      'linear-gradient(135deg,#8b5cf6,#6366f1)',
      'linear-gradient(135deg,#f59e0b,#d97706)',
      'linear-gradient(135deg,#10b981,#059669)',
      'linear-gradient(135deg,#ec4899,#db2777)'
    ];
    this.editingTeam = {
      name: '',
      logo: 'T',
      color: gradients[Math.floor(Math.random() * gradients.length)],
      sportType: 'Football',
      wins: 0, draws: 0, losses: 0,
      goalsFor: 0, goalsAgainst: 0,
      players: []
    };
    this.showTeamModal = true;
  }

  editTeam(t: Team): void {
    this.editingTeam = { ...t };
    this.showTeamModal = true;
  }

  saveTeam(): void {
    if (!this.editingTeam || !this.editingTeam.name) return;

    const payload = {
      nom: this.editingTeam.name,
      typeSport: (this.editingTeam.sportType?.toUpperCase() || 'FOOTBALL') as any
    };

    if (this.editingTeam.id) {
      // UPDATE
      this.teamService.update(this.editingTeam.id, payload).subscribe({
        next: (dto) => {
          const idx = this.teams.findIndex(t => t.id === this.editingTeam!.id);
          if (idx !== -1) {
            const oldName = this.teams[idx].name;
            const newName = dto.nom;
            this.teams[idx] = { ...this.teams[idx], name: newName };
            // cascade name change to bracket/matches
            if (oldName !== newName) {
              this._manualMatches.forEach(m => {
                if (m.team1 === oldName) m.team1 = newName;
                if (m.team2 === oldName) m.team2 = newName;
              });
              Object.values(this.bracketData).forEach(slot => {
                if (slot.team1 === oldName) slot.team1 = newName;
                if (slot.team2 === oldName) slot.team2 = newName;
                if (slot.winner === oldName) slot.winner = newName;
                if (this.champion === oldName) this.champion = newName;
              });
            }
          }
          this.closeTeamModal();
        },
        error: (err) => alert('Erreur mise à jour équipe: ' + (err?.error?.message || err.message))
      });
    } else {
      // CREATE
      this.teamService.create(payload).subscribe({
        next: (dto) => {
          const gradients = [
            'linear-gradient(135deg,#f97316,#ef4444)',
            'linear-gradient(135deg,#0ea5e9,#2563eb)',
            'linear-gradient(135deg,#8b5cf6,#6366f1)',
            'linear-gradient(135deg,#f59e0b,#d97706)',
            'linear-gradient(135deg,#10b981,#059669)',
            'linear-gradient(135deg,#ec4899,#db2777)'
          ];
          const newTeam: Team = {
            id: dto.id,
            name: dto.nom,
            logo: dto.nom.charAt(0).toUpperCase(),
            color: gradients[this.teams.length % gradients.length],
            sportType: dto.typeSport,
            players: [],
            wins: 0, draws: 0, losses: 0, goalsFor: 0, goalsAgainst: 0
          };
          this.teams = [...this.teams, newTeam];
          this.closeTeamModal();
        },
        error: (err) => alert('Erreur création équipe: ' + (err?.error?.message || err.message))
      });
    }
  }

  deleteTeam(id: number): void {
    const t = this.teams.find(team => team.id === id);
    if (!t) return;
    if (!confirm(`Voulez-vous vraiment supprimer l'équipe ${t.name}?`)) return;

    this.teamService.delete(id).subscribe({
      next: () => {
        const oldName = t.name;
        this.allPlayers.forEach(p => { if (p.teamId === id) p.teamId = undefined; });
        this.teams = this.teams.filter(team => team.id !== id);
        this._manualMatches.forEach(m => {
          if (m.team1 === oldName) { m.team1 = 'TBD'; m.status = 'tbd'; }
          if (m.team2 === oldName) { m.team2 = 'TBD'; m.status = 'tbd'; }
        });
        Object.values(this.bracketData).forEach(slot => {
          if (slot.team1 === oldName) { slot.team1 = null; slot.status = 'tbd'; }
          if (slot.team2 === oldName) { slot.team2 = null; slot.status = 'tbd'; }
          if (slot.winner === oldName) { slot.winner = null; slot.status = 'tbd'; }
        });
      },
      error: (err) => alert('Erreur suppression équipe: ' + (err?.error?.message || err.message))
    });
  }

  closeTeamModal(): void {
    this.showTeamModal = false;
    this.editingTeam = null;
  }

  assignPlayerToTeam(playerId: number, teamId: number | undefined): void {
    const pIdx = this.allPlayers.findIndex(p => p.id === playerId);
    if (pIdx === -1) return;
    const oldTeamId = this.allPlayers[pIdx].teamId;
    if (oldTeamId) {
      const oldTeam = this.teams.find(t => t.id === oldTeamId);
      if (oldTeam) oldTeam.players = oldTeam.players.filter(pl => pl.id !== playerId);
    }
    this.allPlayers[pIdx].teamId = teamId;
    if (teamId) {
      const newTeam = this.teams.find(t => t.id === teamId);
      if (newTeam) newTeam.players.push(this.allPlayers[pIdx]);
    }
  }

  get filteredPlayerList(): Player[] {
    const q = this.playerSearch.toLowerCase().trim();
    let r = [...this.allPlayers];
    if (q) r = r.filter(p => p.name.toLowerCase().includes(q));
    if (this.playerSkillFilter !== 'ALL') r = r.filter(p => p.skillLevel === this.playerSkillFilter);
    
    // Sorting
    if (this.playerSortBy === 'goals') {
      r.sort((a,b) => b.goals - a.goals);
    } else if (this.playerSortBy === 'assists') {
      r.sort((a,b) => b.assists - a.assists);
    } else if (this.playerSortBy === 'rating') {
      r.sort((a,b) => b.rating - a.rating);
    } else {
      r.sort((a,b) => a.name.localeCompare(b.name));
    }
    
    return r;
  }

  openRoster(team: Team, event: Event): void {
    event.stopPropagation();
    this.selectedRosterTeam = team;
    this.rosterFilter = 'ALL';
    document.body.style.overflow = 'hidden';
  }

  closeRoster(): void {
    this.selectedRosterTeam = null;
    document.body.style.overflow = '';
  }

  get filteredPlayers(): Player[] {
    if (!this.selectedRosterTeam) return [];
    const players = this.selectedRosterTeam.players;
    return this.rosterFilter === 'ALL' ? players : players.filter(p => p.posGroup === this.rosterFilter);
  }

  posLabel(g: string): string {
    return g === 'GK' ? 'Goalkeeper' : g === 'DEF' ? 'Defender' : g === 'MID' ? 'Midfielder' : 'Forward';
  }

  ratingColor(r: number): string {
    if (r >= 8.5) return '#16a34a';
    if (r >= 7.5) return '#2563eb';
    if (r >= 7.0) return '#d97706';
    return '#64748b';
  }

  get topScorer(): Player | null {
    if (!this.selectedRosterTeam) return null;
    return [...this.selectedRosterTeam.players].sort((a, b) => b.goals - a.goals)[0] ?? null;
  }

  get topAssist(): Player | null {
    if (!this.selectedRosterTeam) return null;
    return [...this.selectedRosterTeam.players].sort((a, b) => b.assists - a.assists)[0] ?? null;
  }


  // activityFeed is now handled by a getter below

  upcomingMatches: { id: number; team1: string; team2: string; time: string; date: string; round: string; venue: string }[] = [];
  recentResults: { id: number; team1: string; team2: string; score1: number; score2: number; date: string; round: string }[] = [];

  // ── Bracket helpers ──────────────────────────────────────
  get qfPair1(): BracketSlot[] { return [this.bracketData['qf1'], this.bracketData['qf2']]; }
  get qfPair2(): BracketSlot[] { return [this.bracketData['qf3'], this.bracketData['qf4']]; }
  get sfMatches(): BracketSlot[] { return [this.bracketData['sf1'], this.bracketData['sf2']]; }
  get finalMatch(): BracketSlot  { return this.bracketData['f1']; }
  get selectedMatch(): BracketSlot | null { return this.selectedMatchId ? this.bracketData[this.selectedMatchId] : null; }

  get completedCount(): number { return this.matchesList.filter(m => m.status === 'completed').length; }
  get totalGoals(): number { return this.matchesList.reduce((s,m) => s + (m.score1??0) + (m.score2??0), 0); }
  get bracketProgress(): number { 
     const currentBracketMatches = this.matchesList.filter(m => m.bracketId === this.activeBracket?.id);
     const total = currentBracketMatches.length || 7;
     const done = currentBracketMatches.filter(m => m.status === 'completed').length;
     return Math.round((done / total) * 100);
  }

  getTeam(name: string|null): Team|undefined { return name ? this.teams.find(t => t.name === name) : undefined; }
  getTeamById(id: number | undefined): Team | undefined { return id !== undefined ? this.teams.find(t => t.id === id) : undefined; }
  getColor(name: string|null): string { return this.getTeam(name)?.color ?? 'linear-gradient(135deg,#94a3b8,#64748b)'; }
  getLogo(name: string|null): string  { return this.getTeam(name)?.logo  ?? '?'; }

  isHighlighted(name: string|null): boolean { return !!this.highlightTeam && this.highlightTeam === name; }
  isDimmed(name: string|null): boolean       { return !!this.highlightTeam && this.highlightTeam !== name && !!name; }

  selectTeamHighlight(name: string|null): void { this.highlightTeam = name; }
  clearHighlight(): void { this.highlightTeam = null; }

  selectWinner(matchId: string, winner: string, manualData?: any): void {
    const match = this.bracketData[matchId];
    if (!match || !match.team1 || !match.team2 || match.winner) return;

    let s1, s2;
    if (manualData) {
      s1 = manualData.score1;
      s2 = manualData.score2;
      match.events = manualData.events || [];
      match.stats = manualData.stats;
    } else {
      const isT1 = winner === match.team1;
      const ws = Math.floor(Math.random() * 3) + 1;
      const ls = Math.floor(Math.random() * ws);
      s1 = isT1 ? ws : ls;
      s2 = isT1 ? ls : ws;
      
      // Auto-generate events for fallback simulation
      match.events = [];
      const t1 = this.getTeam(match.team1);
      const t2 = this.getTeam(match.team2);
      if (t1 && s1 > 0) {
        const scorers = t1.players.filter(p => ['FWD', 'MID'].includes(p.posGroup));
        for(let i=0; i<s1; i++) {
          const p = scorers[Math.floor(Math.random() * scorers.length)];
          match.events.push({ playerId: p.id, playerName: p.name, type: 'goal', teamId: t1.id });
        }
      }
      if (t2 && s2 > 0) {
        const scorers = t2.players.filter(p => ['FWD', 'MID'].includes(p.posGroup));
        for(let i=0; i<s2; i++) {
          const p = scorers[Math.floor(Math.random() * scorers.length)];
          match.events.push({ playerId: p.id, playerName: p.name, type: 'goal', teamId: t2.id });
        }
      }
    }

    // 2. Update Bracket Slot
    match.winner = winner;
    match.score1 = s1;
    match.score2 = s2;
    match.status = 'completed';

    // 3. Update Real Player Stats
    if (match.events) {
      match.events.forEach((ev: MatchEvent) => {
        const team = this.getTeamById(ev.teamId);
        if (team) {
          const player = team.players.find(p => p.id === ev.playerId);
          if (player) {
            if (ev.type === 'goal') player.goals++;
            if (ev.type === 'assist') player.assists++;
            player.rating = Math.min(10, player.rating + 0.2);
            player.appearances++;
          }
        }
      });
      // Increment appearances once for everyone who didn't score/assist? 
      // Simplified: Everyone in the match gets an appearance
      const team1 = this.getTeam(match.team1);
      const team2 = this.getTeam(match.team2);
      [team1, team2].forEach(t => {
        if (t) t.players.forEach(p => {
          // Check if not already counted via events
          const hadEvent = match.events!.some(e => e.playerId === p.id);
          if (!hadEvent) {
             p.appearances++;
             p.rating = Math.min(10, p.rating || 5.0 + (Math.random() * 2));
          }
        });
      });
    }

    // 4. Advance Bracket
    if (match.nextMatchId) {
      const next = this.bracketData[match.nextMatchId];
      if (match.nextSlot === 1) next.team1 = winner; else next.team2 = winner;
      if (next.team1 && next.team2) next.status = 'upcoming';
    }

    if (matchId === 'f1') this.champion = winner;
    this.selectedMatchId = matchId;

    this.activityFeed.unshift({ 
      icon: manualData ? '📝' : '⚽', 
      msg: `${match.team1} ${s1} - ${s2} ${match.team2}`, 
      sub: manualData ? 'Detailed result reported' : `${winner} advances!`, 
      time: 'Just now', 
      type: 'complete', 
      color: this.getColor(winner) 
    });
  }

  openResultModal(matchId: string): void {
    const match = this.bracketData[matchId];
    if (!match || !match.team1 || !match.team2 || match.winner) return;
    
    this.reportingMatchId = matchId;
    this.resultForm = {
      score1: 0,
      score2: 0,
      durationMinutes: 90,
      winner: match.team1!,
      events: [],
      stats: {
        possession1: 50, possession2: 50,
        shots1: 4, shots2: 4,
        fouls1: 2, fouls2: 2
      }
    };
    this.showResultModal = true;
  }

  addMatchEvent(playerId: number, playerName: string, type: 'goal'|'assist'|'yellow'|'red', teamId: number): void {
    this.resultForm.events.push({ playerId, playerName, type, teamId });
    // Auto-update scores based on goal events
    if (type === 'goal') {
      const match = this.bracketData[this.reportingMatchId!];
      if (teamId === this.getTeam(match.team1)?.id) this.resultForm.score1++;
      else this.resultForm.score2++;
    }
  }

  removeMatchEvent(index: number): void {
    const ev = this.resultForm.events[index];
    if (ev.type === 'goal') {
      const match = this.bracketData[this.reportingMatchId!];
      if (ev.teamId === this.getTeam(match.team1)?.id) this.resultForm.score1--;
      else this.resultForm.score2--;
    }
    this.resultForm.events.splice(index, 1);
  }

  saveMatchResult(): void {
    if (!this.reportingMatchId) return;
    
    // Auto-determine winner if scores are different
    if (this.resultForm.score1 > this.resultForm.score2) {
      this.resultForm.winner = this.bracketData[this.reportingMatchId].team1!;
    } else if (this.resultForm.score2 > this.resultForm.score1) {
      this.resultForm.winner = this.bracketData[this.reportingMatchId].team2!;
    }
    
    this.selectWinner(this.reportingMatchId, this.resultForm.winner, this.resultForm);
    this.showResultModal = false;
    this.reportingMatchId = null;
  }

  openTeamProfile(name: string | null, event: Event): void {
    if (!name || name === 'TBD') return;
    const team = this.getTeam(name);
    if (team) {
      this.openRoster(team, event);
    }
  }

  resetMatch(matchId: string): void {
    const match = this.bracketData[matchId];
    if (!match || match.status !== 'completed') return;

    // 1. Rollback Player Stats
    if (match.events) {
      match.events.forEach((ev: MatchEvent) => {
        const team = this.getTeamById(ev.teamId);
        if (team) {
          const player = team.players.find(p => p.id === ev.playerId);
          if (player) {
            if (ev.type === 'goal') player.goals = Math.max(0, player.goals - 1);
            if (ev.type === 'assist') player.assists = Math.max(0, player.assists - 1);
            player.appearances = Math.max(0, player.appearances - 1);
          }
        }
      });
      // Rollback appearance for everyone else too
      const team1 = this.getTeam(match.team1);
      const team2 = this.getTeam(match.team2);
      [team1, team2].forEach(t => {
        if (t) t.players.forEach(p => {
          const hadEvent = match.events!.some(e => e.playerId === p.id);
          if (!hadEvent) p.appearances = Math.max(0, p.appearances - 1);
        });
      });
    }

    const prevWinner = match.winner;
    match.winner = null; match.score1 = null; match.score2 = null; 
    match.events = []; 
    match.status = (match.team1 && match.team2) ? 'upcoming' : 'tbd';
    match.durationMinutes = 90;

    // Sync to Match List
    const mInList = this.matchesList.find(m => m.bracketSlotId === matchId);
    if (mInList) {
      mInList.score1 = undefined; mInList.score2 = undefined; mInList.status = 'upcoming'; 
      mInList.team1 = match.team1 || 'TBD'; mInList.team2 = match.team2 || 'TBD';
    }

    if (match.nextMatchId && prevWinner) {
      this.resetMatch(match.nextMatchId); // Recursive reset of dependent matches
    }
  }

  simulateBracket(): void {
    ['qf1','qf2','qf3','qf4','sf1','sf2','f1'].forEach(id => {
      const m = this.bracketData[id];
      if (m.team1 && m.team2 && !m.winner) { const w = Math.random()>.5 ? m.team1 : m.team2; this.selectWinner(id, w); }
    });
    this.activityFeed.unshift({ icon:'🎲', msg:'Bracket Simulated', sub:'Random outcomes generated', time:'Just now', type:'simulate', color:'#f97316' });
  }

  resetBracket(): void {
    // Reset from the end to the beginning to ensure recursive safety
    ['f1', 'sf2', 'sf1', 'qf4', 'qf3', 'qf2', 'qf1'].forEach(id => {
      this.resetMatch(id);
    });

    this.champion = null;
    this.selectedMatchId = null;
    this.showResetConfirm = false;
    this.initMatches(); // Refresh matches list state
    this.activityFeed.unshift({ icon:'🔄', msg:'Bracket Reset', sub:'All results & player stats cleared', time:'Just now', type:'reset', color:'#7c3aed' });
  }

  // ── Stadium CRUD Methods ──────────────────────────────
  
  openAddStadium(): void {
    this.editingStadium = {
      name: '',
      capacity: 500,
      surface: 'Synthetic',
      district: '',
      lighting: 'Full',
      yearBuilt: new Date().getFullYear(),
      description: '',
      icon: '🏟️',
      supportedSports: ['Football']
    };
    this.showStadiumModal = true;
  }

  editStadium(s: Stadium): void {
    this.editingStadium = { ...s, supportedSports: [...s.supportedSports] };
    this.showStadiumModal = true;
  }

  saveStadium(): void {
    if (!this.editingStadium || !this.editingStadium.name) return;

    const payload = {
      nom: this.editingStadium.name,
      typeSport: (this.editingStadium.supportedSports?.[0]?.toUpperCase() || 'FOOTBALL') as any,
      location: this.editingStadium.district || this.editingStadium.name,
      address: this.editingStadium.description || this.editingStadium.name
    };

    if (this.editingStadium.id) {
      this.terrainService.update(this.editingStadium.id, payload).subscribe({
        next: (dto) => {
          const idx = this.stadiumsList.findIndex(s => s.id === this.editingStadium!.id);
          if (idx !== -1) {
            this.stadiumsList[idx] = {
              ...this.stadiumsList[idx],
              name: dto.nom,
              district: dto.location,
              description: dto.address
            };
          }
          this.closeStadiumModal();
        },
        error: (err) => alert('Erreur mise à jour terrain: ' + (err?.error?.message || err?.error?.error || err.message))
      });
    } else {
      this.terrainService.create(payload).subscribe({
        next: (dto) => {
          const newStadium: Stadium = {
            id: dto.id,
            name: dto.nom,
            capacity: this.editingStadium!.capacity || 500,
            surface: this.editingStadium!.surface || 'Synthetic',
            district: dto.location,
            lighting: this.editingStadium!.lighting || 'Standard',
            yearBuilt: this.editingStadium!.yearBuilt || new Date().getFullYear(),
            description: dto.address,
            icon: this.editingStadium!.icon || '🏟️',
            supportedSports: [dto.typeSport]
          };
          this.stadiumsList = [...this.stadiumsList, newStadium];
          this.closeStadiumModal();
        },
        error: (err) => alert('Erreur création terrain: ' + (err?.error?.message || err?.error?.error || err.message))
      });
    }
  }

  deleteStadium(id: number): void {
    if (!confirm('Voulez-vous vraiment supprimer ce terrain ?')) return;
    this.terrainService.delete(id).subscribe({
      next: () => {
        this.stadiumsList = this.stadiumsList.filter(s => s.id !== id);
      },
      error: (err) => alert('Erreur suppression terrain: ' + (err?.error?.message || err?.error?.error || err.message))
    });
  }

  closeStadiumModal(): void {
    this.showStadiumModal = false;
    this.editingStadium = null;
  }

  toggleStadiumSport(sport: string): void {
    if (!this.editingStadium) return;
    const sports = this.editingStadium.supportedSports || [];
    if (sports.includes(sport)) {
      this.editingStadium.supportedSports = sports.filter(s => s !== sport);
    } else {
      this.editingStadium.supportedSports = [...sports, sport];
    }
  }

  get filteredStadiums(): Stadium[] {
    const q = this.stadiumSearch.toLowerCase().trim();
    return q ? this.stadiumsList.filter(s => s.name.toLowerCase().includes(q) || s.district.toLowerCase().includes(q)) : this.stadiumsList;
  }

  openStadiumDetails(s: Stadium): void {
    this.selectedStadiumDetails = s;
    this.showStadiumDetailsModal = true;
    document.body.style.overflow = 'hidden';
  }

  closeStadiumDetails(): void {
    this.selectedStadiumDetails = null;
    this.showStadiumDetailsModal = false;
    document.body.style.overflow = '';
  }

  getStadiumMatchHistory(venueName: string): Match[] {
    return this.matchesList
      .filter(m => m.venue === venueName && m.status === 'completed')
      .sort((a,b) => b.id - a.id);
  }

  getStadiumUpcomingMatches(venueName: string): Match[] {
    return this.matchesList
      .filter(m => m.venue === venueName && m.status === 'upcoming')
      .sort((a,b) => a.id - b.id);
  }

  isStadiumOccupied(venueName: string): boolean {
    return this.matchesList.some(m => m.venue === venueName && (m.status as any) === 'live');
  }

  isTimeOverlap(m1: Match, m2: Match): boolean {
    if (m1.date !== m2.date) return false;
    
    const start1 = this.getMatchStartTime(m1);
    const end1 = start1 + (m1.durationMinutes || 90);
    
    const start2 = this.getMatchStartTime(m2);
    const end2 = start2 + (m2.durationMinutes || 90);

    return (start1 < end2) && (start2 < end1);
  }

  private getMatchStartTime(m: Match): number {
    const [h, min] = m.time.split(':').map(Number);
    return h * 60 + min;
  }

  // ── Match CRUD Methods ───────────────────────────────
  
  openAddMatch(): void {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const dateStr = tomorrow.toISOString().split('T')[0];
    this.editingMatch = {
      team1: this.teams[0]?.name || '',
      team2: this.teams[1]?.name || '',
      date: dateStr,
      time: '18:00',
      round: 'Regular',
      venue: this.stadiumsList[0]?.name || '',
      durationMinutes: 90,
      status: 'upcoming'
    };
    this.showMatchModal = true;
  }

  editMatch(m: Match): void {
    this.editingMatch = { ...m };
    this.showMatchModal = true;
  }

  saveMatch(): void {
    if (!this.editingMatch || !this.editingMatch.team1 || !this.editingMatch.team2) return;

    // Resolve team IDs from names
    const homeTeam = this.teams.find(t => t.name === this.editingMatch!.team1);
    const awayTeam = this.teams.find(t => t.name === this.editingMatch!.team2);
    const terrain = this.stadiumsList.find(s => s.name === this.editingMatch!.venue);

    if (!homeTeam || !awayTeam) {
      alert('⚠️ Équipes introuvables. Assurez-vous que les équipes existent dans la base de données.');
      return;
    }
    if (!terrain) {
      alert('⚠️ Terrain introuvable. Assurez-vous que le terrain existe dans la base de données.');
      return;
    }

    // Build ISO datetime from date + time fields
    const matchDate = `${this.editingMatch.date}T${this.editingMatch.time || '18:00'}:00`;

    const payload = {
      homeTeam: { id: homeTeam.id },
      awayTeam: { id: awayTeam.id },
      terrain: { id: terrain.id },
      matchDate
    };

    if (this.editingMatch.id && !String(this.editingMatch.id).startsWith('1000')) {
      // UPDATE — only for real backend matches (not bracket-generated IDs)
      this.matchService.update(this.editingMatch.id, payload as any).subscribe({
        next: (dto) => {
          const idx = this._manualMatches.findIndex(m => m.id === this.editingMatch!.id);
          if (idx !== -1) {
            this._manualMatches[idx] = {
              ...this._manualMatches[idx],
              team1: dto.homeTeamName,
              team2: dto.awayTeamName,
              venue: dto.terrainName,
              date: dto.matchDate ? new Date(dto.matchDate).toLocaleDateString('fr-FR') : '',
              time: dto.matchDate ? new Date(dto.matchDate).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' }) : ''
            };
          }
          this.closeMatchModal();
        },
        error: (err) => alert('Erreur mise à jour match: ' + (err?.error?.message || err?.error?.error || err.message))
      });
    } else {
      // CREATE
      this.matchService.create(payload as any).subscribe({
        next: (dto) => {
          const newMatch: Match = {
            id: dto.id,
            team1: dto.homeTeamName,
            team2: dto.awayTeamName,
            date: dto.matchDate ? new Date(dto.matchDate).toLocaleDateString('fr-FR', { month: 'short', day: 'numeric' }) : '',
            time: dto.matchDate ? new Date(dto.matchDate).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' }) : '',
            round: this.editingMatch!.round || 'Regular',
            venue: dto.terrainName,
            status: 'upcoming',
            durationMinutes: 90
          };
          this._manualMatches = [...this._manualMatches, newMatch];
          this.closeMatchModal();
        },
        error: (err) => alert('Erreur création match: ' + (err?.error?.message || err?.error?.error || err.message))
      });
    }
  }

  deleteMatch(id: number): void {
    if (!confirm('Voulez-vous supprimer ce match ?')) return;
    // Only delete real backend matches (not bracket-generated ones)
    const match = this._manualMatches.find(m => m.id === id);
    if (match) {
      this.matchService.delete(id).subscribe({
        next: () => {
          this._manualMatches = this._manualMatches.filter(m => m.id !== id);
        },
        error: (err) => alert('Erreur suppression match: ' + (err?.error?.message || err?.error?.error || err.message))
      });
    } else {
      // Bracket match — just remove from bracket display
      this._manualMatches = this._manualMatches.filter(m => m.id !== id);
    }
  }

  closeMatchModal(): void {
    this.showMatchModal = false;
    this.editingMatch = null;
  }

  get filteredMatchList(): Match[] {
    const q = this.matchSearch.toLowerCase().trim();
    if (!q) return this.matchesList;
    return this.matchesList.filter(m => 
      m.team1.toLowerCase().includes(q) || 
      m.team2.toLowerCase().includes(q) || 
      m.venue.toLowerCase().includes(q)
    );
  }

  get upcomingMatchesList(): Match[] {
    return this.matchesList.filter(m => m.status === 'upcoming');
  }

  get recentResultsList(): Match[] {
    return this.matchesList.filter(m => m.status === 'completed');
  }
  get stadiumsList(): Stadium[] { return this._stadiums; }
  set stadiumsList(val: Stadium[]) { this._stadiums = val; }
  private _stadiums: Stadium[] = [];

  getStadium(venueName: string): Stadium | null {
    return this.stadiumsList.find(s => s.name === venueName) ?? null;
  }

  roundLabel(r: string): string { return r==='QF'?'Quarter-Final':r==='SF'?'Semi-Final':r==='F'?'Final':r; }
  selectMatch(id: string): void { this.selectedMatchId = this.selectedMatchId===id ? null : id; }
  closeMatchPanel(): void { this.selectedMatchId = null; }
  setTab(t: TabType): void { this.activeTab=t; this.selectedMatchId=null; }
  get avgGoals(): string { return this.completedCount ? (this.totalGoals/this.completedCount).toFixed(1) : '—'; }

  // ── Dynamic Standings & Form ──────────────────────────────
  get teamStatsMap() {
    const stats: { [key: string]: any } = {};
    this.teams.forEach(t => {
      stats[t.name] = { wins:0, draws:0, losses:0, gf:0, ga:0, matches: [] as string[] };
    });

    this.matchesList.forEach(m => {
      if (m.status !== 'completed' || m.score1 === undefined || m.score2 === undefined) return;
      
      const t1 = stats[m.team1];
      const t2 = stats[m.team2];
      if (!t1 || !t2) return;

      t1.gf += m.score1; t1.ga += m.score2;
      t2.gf += m.score2; t2.ga += m.score1;

      if (m.score1 > m.score2) {
        t1.wins++; t2.losses++;
        t1.matches.push('W'); t2.matches.push('L');
      } else if (m.score1 < m.score2) {
        t2.wins++; t1.losses++;
        t1.matches.push('L'); t2.matches.push('W');
      } else {
        t1.draws++; t2.draws++;
        t1.matches.push('D'); t2.matches.push('D');
      }
    });

    return stats;
  }

  get standings() {
    const stats = this.teamStatsMap;
    return [...this.teams].map(t => {
      const s = stats[t.name] || { wins:0, draws:0, losses:0, gf:0, ga:0, matches:[] };
      return {
        rank: 0,
        team: t,
        played: s.wins + s.draws + s.losses,
        wins: s.wins,
        draws: s.draws,
        losses: s.losses,
        gf: s.gf,
        ga: s.ga,
        gd: s.gf - s.ga,
        pts: s.wins * 3 + s.draws,
        form: s.matches.slice(-5)
      };
    }).sort((a, b) => (b.pts - a.pts) || (b.gd - a.gd) || (b.gf - a.gf))
      .map((item, idx) => ({ ...item, rank: idx + 1 }));
  }

  get activityFeed(): ActivityItem[] {
    // Generate feed from the last 6 completed matches
    const recent = this.matchesList
      .filter(m => m.status === 'completed' && m.score1 !== undefined)
      .sort((a,b) => b.id - a.id) // simplistic 'recent' sort
      .slice(0, 6)
      .map(m => ({
        icon: '⚽',
        msg: `${m.team1} ${m.score1}–${m.score2} ${m.team2}`,
        sub: `${m.round} · Match Terminé`,
        time: m.date,
        type: 'complete' as const,
        color: '#16a34a'
      }));

    if (recent.length === 0) {
      return [
        { icon: '📋', msg: 'Tournoi Initialisé', sub: 'Prêt pour le coup d\'envoi', time: 'Aujourd\'hui', type: 'info', color: '#7c3aed' }
      ];
    }
    return recent;
  }

  // ── Teams ─────────────────────────────────────────────────
  get filteredTeams(): Team[] {
    const q = this.teamSearch.toLowerCase().trim();
    const stats = this.teamStatsMap;
    let r = q ? this.teams.filter(t => t.name.toLowerCase().includes(q)) : [...this.teams];
    
    if (this.teamSortBy === 'winrate') {
      r.sort((a,b) => this.wr(b) - this.wr(a));
    } else if (this.teamSortBy === 'wins') {
      r.sort((a,b) => (stats[b.name]?.wins || 0) - (stats[a.name]?.wins || 0));
    } else if (this.teamSortBy === 'goals') {
      r.sort((a,b) => (stats[b.name]?.gf || 0) - (stats[a.name]?.gf || 0));
    } else {
      r.sort((a,b) => a.name.localeCompare(b.name));
    }
    return r;
  }

  wr(t: Team): number { 
    const s = this.teamStatsMap[t.name];
    if (!s) return 0;
    const n = s.wins + s.draws + s.losses; 
    return n ? Math.round(s.wins / n * 100) : 0; 
  }
  
  getWinRate(t: Team): number { return this.wr(t); }
  get progressPercent(): number { return Math.round(this.currentRoundNumber/this.totalRounds*100); }

  // ── Tournament Lifecycle ──────────────────────────────────
  openCreateTournament(): void {
    this.newBracketForm = {
      name: `Tournoi ${this.brackets.length + 1}`,
      category: 'Pro League',
      color: '#2563eb',
      teamCount: 8
    };
    this.showCreateTournamentModal = true;
  }

  createTournament(): void {
    const id = 't-' + Date.now();
    // For now, we pick 8 random teams from the available pool
    const availableTeams = [...this.teams].sort(() => 0.5 - Math.random()).slice(0, 8);
    const teamNames = availableTeams.map(t => t.name);

    const newTournament: TournamentBracket = {
      id,
      name: this.newBracketForm.name || `Tournament ${this.brackets.length + 1}`,
      category: this.newBracketForm.category,
      color: `linear-gradient(135deg, ${this.newBracketForm.color}, #ffffff33)`,
      status: 'Active',
      dateCreated: new Date().toISOString().split('T')[0],
      champion: null,
      data: this.generateBracketData(teamNames)
    };

    this.brackets.push(newTournament);
    this.activeBracketId = id;
    this.showCreateTournamentModal = false;
    this.initMatches(); // Refresh matches list to include new ones
  }

  deleteTournament(id: string, event?: Event): void {
    if (event) event.stopPropagation();
    if (this.brackets.length <= 1) return; // Keep at least one
    if (confirm('Voulez-vous vraiment supprimer ce tournoi ?')) {
      this.brackets = this.brackets.filter(b => b.id !== id);
      if (this.activeBracketId === id) this.activeBracketId = this.brackets[0].id;
      this.initMatches();
    }
  }

  generateBracketData(teams: string[]): { [key: string]: BracketSlot } {
    // Standard 8-team single elimination knockout
    return {
      'qf1': { id:'qf1', round:'QF', team1:teams[0]||'TBD', team2:teams[7]||'TBD', seed1:1, seed2:8, score1:null, score2:null, winner:null, date:'TBD', time:'18:00', venue:'Terrain Nord', status:'upcoming', nextMatchId:'sf1', nextSlot:1 },
      'qf2': { id:'qf2', round:'QF', team1:teams[3]||'TBD', team2:teams[4]||'TBD', seed1:4, seed2:5, score1:null, score2:null, winner:null, date:'TBD', time:'20:00', venue:'The Cage',       status:'upcoming', nextMatchId:'sf1', nextSlot:2 },
      'qf3': { id:'qf3', round:'QF', team1:teams[2]||'TBD', team2:teams[5]||'TBD', seed1:3, seed2:6, score1:null, score2:null, winner:null, date:'TBD', time:'15:00', venue:'Urban Arena',    status:'upcoming', nextMatchId:'sf2', nextSlot:1 },
      'qf4': { id:'qf4', round:'QF', team1:teams[1]||'TBD', team2:teams[6]||'TBD', seed1:2, seed2:7, score1:null, score2:null, winner:null, date:'TBD', time:'17:30', venue:'Terrain Sud',    status:'upcoming', nextMatchId:'sf2', nextSlot:2 },
      'sf1': { id:'sf1', round:'SF', team1:null, team2:null, seed1:null, seed2:null, score1:null, score2:null, winner:null, date:'TBD', time:'18:00', venue:'Stade Central',  status:'tbd', nextMatchId:'f1', nextSlot:1 },
      'sf2': { id:'sf2', round:'SF', team1:null, team2:null, seed1:null, seed2:null, score1:null, score2:null, winner:null, date:'TBD', time:'20:00', venue:'Stade Central',  status:'tbd', nextMatchId:'f1', nextSlot:2 },
      'f1':  { id:'f1',  round:'F',  team1:null, team2:null, seed1:null, seed2:null, score1:null, score2:null, winner:null, date:'TBD', time:'19:00', venue:'Grand Stade',    status:'tbd', nextMatchId:null,  nextSlot:null },
    };
  }

  generateSampleTournaments(count: number): void {
    const categories = ['Pro League', 'Elite', 'Youth', 'Challenger', 'Community Cup'];
    const colors = ['#2563eb', '#ec4899', '#10b981', '#f59e0b', '#7c3aed', '#6366f1'];
    
    for (let i = 0; i < count; i++) {
        const id = 'gen-' + i + '-' + Date.now();
        const teamsPool = [...this.teams].sort(() => 0.5 - Math.random()).slice(0, 8).map(t => t.name);
        this.brackets.push({
            id,
            name: `Test Tournament #${i + 4}`,
            category: categories[Math.floor(Math.random() * categories.length)],
            color: `linear-gradient(135deg, ${colors[Math.floor(Math.random() * colors.length)]}, #ffffff33)`,
            status: Math.random() > 0.3 ? 'Active' : 'Completed',
            dateCreated: `2025-03-${Math.floor(Math.random() * 20) + 10}`,
            champion: null,
            data: this.generateBracketData(teamsPool)
        });
    }
    this.initMatches();
  }
}
