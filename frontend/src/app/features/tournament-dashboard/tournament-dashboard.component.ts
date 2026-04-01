import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';

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
export class TournamentDashboardComponent {
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

  teams: Team[] = [
    { id:1, name:'Fury FC', logo:'F', color:'linear-gradient(135deg,#f97316,#ef4444)', players:[
      { id:1,  name:'Marcus Webb',     number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:2,  name:'Devon Osei',      number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:3,  name:'Tyler Asante',    number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:4,  name:'Jalen Brooks',    number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:5,  name:'Kofi Mensah',     number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:6,  name:'Elijah Cruz',     number:8,  position:'CDM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:7,  name:'Jordan Patel',    number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:8,  name:'André Fontaine',  number:7,  position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:9,  name:'Dante Rivera',    number:11, position:'RW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:10, name:'Kwame Okonkwo',   number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:2, name:'Street Kings', logo:'S', color:'linear-gradient(135deg,#0ea5e9,#2563eb)', players:[
      { id:11, name:'Isaiah Clarke',   number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:12, name:'Caleb Nwosu',     number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:13, name:'Finn Adeyemi',    number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:14, name:'Leon Mbeki',      number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:15, name:'Marcus Haynes',   number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:16, name:'Theo Grant',      number:6,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:17, name:'Kai Solomon',     number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:18, name:'Zion Baptiste',   number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:19, name:'Nate Williams',   number:11, position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:20, name:'Rico Ferreira',   number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:3, name:'Urban Wolves', logo:'U', color:'linear-gradient(135deg,#10b981,#059669)', players:[
      { id:21, name:'Tobias Müller',   number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:22, name:'Sam Okoro',       number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:23, name:'Aaron Diallo',    number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:24, name:'Luca Ferrero',    number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:25, name:'Ben Achebe',      number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:26, name:'Sven Larsson',    number:6,  position:'CDM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:27, name:'Omar Sharif',     number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:28, name:'Pablo Reyes',     number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:29, name:'Yusuf Hassan',    number:7,  position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:30, name:'Victor Asante',   number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:4, name:'Concrete Boys', logo:'C', color:'linear-gradient(135deg,#6366f1,#4338ca)', players:[
      { id:31, name:'Dion Traore',      number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:32, name:'Kyle Eze',         number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:33, name:'Jamal Osei',       number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:34, name:'Chris Ndiaye',     number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:35, name:'Ray Otieno',       number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:36, name:'Ben Addo',         number:6,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:37, name:'Ethan Kofi',       number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:38, name:'Rashid Ali',       number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:39, name:'Felix Amara',      number:7,  position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:40, name:'Malik Sousa',      number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:5, name:'Blacktop Elite', logo:'B', color:'linear-gradient(135deg,#f43f5e,#e11d48)', players:[
      { id:41, name:'Noel Kamara',      number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:42, name:'Tunde Bello',      number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:43, name:'Emeka Dike',       number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:44, name:'Joel Musa',        number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:45, name:'Kwesi Amoah',      number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:46, name:'Seun Ola',         number:6,  position:'CDM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:47, name:'Didier Yao',       number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:48, name:'Tony Essien',      number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:49, name:'Bright Odion',     number:11, position:'RW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:50, name:'Samuel Boateng',   number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:6, name:'North Side FC', logo:'N', color:'linear-gradient(135deg,#8b5cf6,#7c3aed)', players:[
      { id:51, name:'Daniel Owusu',     number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:52, name:'Kelvin Abara',     number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:53, name:'Chidi Ofor',       number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:54, name:'Drew Mensah',      number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:55, name:'Femi Igwe',        number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:56, name:'Akin Cole',        number:6,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:57, name:'Tobi Adisa',       number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:58, name:'Ola Badmus',       number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:59, name:'Niyi Eze',         number:11, position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:60, name:'Obi Nwosu',        number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:7, name:'The Underdogs', logo:'T', color:'linear-gradient(135deg,#64748b,#475569)', players:[
      { id:61, name:'Henry Oppong',     number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:62, name:'Sam Quartey',      number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:63, name:'Eric Danso',       number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:64, name:'Pete Asante',      number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:65, name:'George Acheampong',number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:66, name:'Frank Boateng',    number:6,  position:'CDM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:67, name:'Moses Tetteh',     number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:68, name:'Ike Asante',       number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:69, name:'Joe Mensah',       number:7,  position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:70, name:'Ken Fordjour',     number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
    { id:8, name:'Raw Talents', logo:'R', color:'linear-gradient(135deg,#ec4899,#be185d)', players:[
      { id:71, name:'Leo Quaye',        number:1,  position:'GK',  posGroup:'GK',  goals:0, assists:0, appearances:0, rating:0.0 },
      { id:72, name:'Nelson Afful',     number:4,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:73, name:'Ato Bonsu',        number:5,  position:'CB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:74, name:'Yaw Darko',        number:3,  position:'LB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:75, name:'Nana Amponsah',    number:2,  position:'RB',  posGroup:'DEF', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:76, name:'Kwame Darko',      number:6,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:77, name:'Francis Asare',    number:8,  position:'CM',  posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:78, name:'Grace Osei',       number:10, position:'CAM', posGroup:'MID', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:79, name:'Bright Asante',    number:11, position:'LW',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
      { id:80, name:'Prince Koomson',   number:9,  position:'ST',  posGroup:'FWD', goals:0, assists:0, appearances:0, rating:0.0 },
    ]},
  ];

  constructor(public authService: AuthService) {
    this.initAllPlayers();
    this.initMatches();
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

    this._manualMatches = []; // No historical fake matches
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

    if (this.editingPlayer.id) {
      // Update
      const idx = this.allPlayers.findIndex(p => p.id === this.editingPlayer!.id);
      if (idx !== -1) {
        const oldTeamId = this.allPlayers[idx].teamId;
        const newTeamId = this.editingPlayer.teamId;

        const updatedPlayers = [...this.allPlayers];
        updatedPlayers[idx] = { ...this.editingPlayer } as Player;
        this.allPlayers = updatedPlayers;

        if (oldTeamId !== newTeamId) {
          this.assignPlayerToTeam(this.editingPlayer.id, newTeamId);
        } else if (newTeamId) {
          const team = this.teams.find(t => t.id === newTeamId);
          if (team) {
            const pIdx = team.players.findIndex(p => p.id === this.editingPlayer!.id);
            if (pIdx !== -1) {
              const updatedRoster = [...team.players];
              updatedRoster[pIdx] = { ...this.allPlayers[idx] };
              team.players = updatedRoster;
            }
          }
        }
      }
    } else {
      // Create
      const newId = Math.max(...this.allPlayers.map(p => p.id), 0) + 1;
      const newPlayer = {
        ...this.editingPlayer,
        id: newId,
        number: this.editingPlayer.number || Math.floor(Math.random() * 99) + 1,
        rating: Math.floor(Math.random() * 3) + 6
      } as Player;
      this.allPlayers = [...this.allPlayers, newPlayer];
      if (newPlayer.teamId) {
        this.assignPlayerToTeam(newPlayer.id, newPlayer.teamId);
      }
    }
    this.closePlayerModal();
  }

  deletePlayer(id: number): void {
    const p = this.allPlayers.find(pl => pl.id === id);
    if (!p) return;
    if (confirm(`Voulez-vous vraiment supprimer ${p.name}?`)) {
      this.allPlayers = this.allPlayers.filter(pl => pl.id !== id);
      if (p.teamId) {
        const team = this.teams.find(t => t.id === p.teamId);
        if (team) team.players = team.players.filter(pl => pl.id !== id);
      }
    }
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
    
    if (this.editingTeam.id) {
      const idx = this.teams.findIndex(t => t.id === this.editingTeam!.id);
      if (idx !== -1) {
        const oldName = this.teams[idx].name;
        const newName = this.editingTeam.name;

        // 1. Update Team record
        this.teams[idx] = { ...this.teams[idx], ...this.editingTeam } as Team;

        // 2. Cascade Name Change to Matches
        if (oldName !== newName) {
          this.matchesList.forEach(m => {
            if (m.team1 === oldName) m.team1 = newName;
            if (m.team2 === oldName) m.team2 = newName;
          });

          // 3. Cascade to Bracket
          Object.values(this.bracketData).forEach(slot => {
            if (slot.team1 === oldName) slot.team1 = newName;
            if (slot.team2 === oldName) slot.team2 = newName;
            if (slot.winner === oldName) slot.winner = newName;
            // Champion link
            if (this.champion === oldName) this.champion = newName;
          });
        }
      }
    } else {
      const newId = Math.max(...this.teams.map(t => t.id), 0) + 1;
      const newTeam = { ...this.editingTeam, id: newId, logo: this.editingTeam.name.charAt(0).toUpperCase(), players: [] } as Team;
      this.teams = [...this.teams, newTeam];
    }
    this.closeTeamModal();
  }

  deleteTeam(id: number): void {
    const t = this.teams.find(team => team.id === id);
    if (!t) return;
    if (confirm(`Voulez-vous vraiment supprimer l'équipe ${t.name}? Tous les matchs associés seront marqués 'TBD'.`)) {
      const oldName = t.name;
      this.allPlayers.forEach(p => { if (p.teamId === id) p.teamId = undefined; });
      this.teams = this.teams.filter(team => team.id !== id);

      // Cleanup matches
      this.matchesList.forEach(m => {
        if (m.team1 === oldName) { m.team1 = 'TBD'; m.status = 'tbd'; }
        if (m.team2 === oldName) { m.team2 = 'TBD'; m.status = 'tbd'; }
      });
      // Cleanup bracket
      Object.values(this.bracketData).forEach(slot => {
        if (slot.team1 === oldName) { slot.team1 = null; slot.status = 'tbd'; }
        if (slot.team2 === oldName) { slot.team2 = null; slot.status = 'tbd'; }
        if (slot.winner === oldName) { slot.winner = null; slot.status = 'tbd'; }
      });
    }
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

  upcomingMatches = [
    { id:1, team1:'Fury FC',       team2:'Urban Wolves',   time:'18:00', date:'Mar 14', round:'QF', venue:'Terrain Nord'  },
    { id:2, team1:'Street Kings',  team2:'Blacktop Elite', time:'20:00', date:'Mar 14', round:'QF', venue:'The Cage'      },
    { id:3, team1:'Concrete Boys', team2:'Raw Talents',    time:'15:00', date:'Mar 15', round:'QF', venue:'Urban Arena'   },
    { id:4, team1:'North Side FC', team2:'The Underdogs',  time:'17:30', date:'Mar 15', round:'QF', venue:'Terrain Sud'   },
  ];
  recentResults = [
    { id:5, team1:'Fury FC',        team2:'North Side FC',  score1:3, score2:1, date:'Mar 10', round:'R16' },
    { id:6, team1:'Urban Wolves',   team2:'The Underdogs',  score1:4, score2:0, date:'Mar 10', round:'R16' },
    { id:7, team1:'Street Kings',   team2:'Concrete Boys',  score1:2, score2:2, date:'Mar 11', round:'R16' },
    { id:8, team1:'Blacktop Elite', team2:'Raw Talents',    score1:1, score2:3, date:'Mar 11', round:'R16' },
  ];

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
    if (this.editingStadium.id) {
      const idx = this.stadiumsList.findIndex(s => s.id === this.editingStadium!.id);
      if (idx !== -1) {
        const updated = [...this.stadiumsList];
        updated[idx] = { ...this.editingStadium } as Stadium;
        this.stadiumsList = updated;
      }
    } else {
      const newId = Math.max(...this.stadiumsList.map(s => s.id), 0) + 1;
      this.stadiumsList = [...this.stadiumsList, { ...this.editingStadium, id: newId } as Stadium];
    }
    this.closeStadiumModal();
  }

  deleteStadium(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer ce terrain ?')) {
      this.stadiumsList = this.stadiumsList.filter(s => s.id !== id);
    }
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
    this.editingMatch = {
      team1: '',
      team2: '',
      date: new Date().toISOString().split('T')[0],
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
    
    // CONFLICT CHECK
    const conflict = this.matchesList.find(m => 
      m.id !== this.editingMatch?.id &&
      m.venue === this.editingMatch?.venue &&
      this.isTimeOverlap(m, this.editingMatch as Match)
    );

    if (conflict) {
      alert(`⚠️ Conflict Detected! ${this.editingMatch.venue} is already occupied by ${conflict.team1} vs ${conflict.team2} during this period.`);
      return;
    }

    let savedMatch: Match;
    if (this.editingMatch.id) {
      const idx = this.matchesList.findIndex(m => m.id === this.editingMatch!.id);
      if (idx !== -1) {
        this.matchesList[idx] = { ...this.editingMatch } as Match;
        savedMatch = this.matchesList[idx];
      } else return;
    } else {
      const newId = Math.max(...this.matchesList.map(m => m.id), 0) + 1;
      savedMatch = { ...this.editingMatch, id: newId } as Match;
      this.matchesList = [...this.matchesList, savedMatch];
    }

    // SYNC TO BRACKET if applicable
    if (savedMatch.bracketSlotId) {
      const slot = this.bracketData[savedMatch.bracketSlotId];
      if (slot) {
        slot.team1 = savedMatch.team1;
        slot.team2 = savedMatch.team2;
        slot.score1 = savedMatch.score1 ?? null;
        slot.score2 = savedMatch.score2 ?? null;
        slot.date = savedMatch.date;
        slot.time = savedMatch.time;
        slot.venue = savedMatch.venue;
        slot.status = savedMatch.status === 'completed' ? 'completed' : (slot.team1 && slot.team2 ? 'upcoming' : 'tbd');
        
        // If status changed to completed, we need to advance the winner manually
        if (savedMatch.status === 'completed' && savedMatch.score1 !== undefined && savedMatch.score2 !== undefined) {
          const winner = savedMatch.score1 > savedMatch.score2 ? savedMatch.team1 : savedMatch.team2;
          slot.winner = winner;
          if (slot.nextMatchId) {
            const next = this.bracketData[slot.nextMatchId];
            if (slot.nextSlot === 1) next.team1 = winner; else next.team2 = winner;
            // Update next match in list too
            const nextInList = this.matchesList.find(m => m.bracketSlotId === slot.nextMatchId);
            if (nextInList) {
              if (slot.nextSlot === 1) nextInList.team1 = winner; else nextInList.team2 = winner;
            }
          }
          if (savedMatch.bracketSlotId === 'f1') this.champion = winner;
        }
      }
    }

    this.closeMatchModal();
  }

  deleteMatch(id: number): void {
    if (confirm('Voulez-vous supprimer ce match ?')) {
      this.matchesList = this.matchesList.filter(m => m.id !== id);
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
  private _stadiums: Stadium[] = [
    { id:1, name:'Terrain Nord', capacity:1200, surface:'Synthetic Turf (3G)', district:'North District', lighting:'Full Floodlights', yearBuilt:2019, description:'A modern synthetic arena...', icon:'🏟️', supportedSports:['Football'] },
    { id:2, name:'The Cage', capacity:800, surface:'Asphalt (Street)', district:'Downtown Core', lighting:'LED Panels', yearBuilt:2014, description:'Iconic street-court...', icon:'⛓️', supportedSports:['Football', 'Basketball'] },
    { id:3, name:'Urban Arena', capacity:1500, surface:'Compact Grass', district:'East Side', lighting:'Floodlights', yearBuilt:2021, description:'The largest open ground...', icon:'🌿', supportedSports:['Football'] },
    { id:4, name:'Terrain Sud', capacity:950, surface:'Synthetic Turf (2G)', district:'South Quarter', lighting:'Standard Pylons', yearBuilt:2016, description:'A community-built pitch...', icon:'🏟️', supportedSports:['Football'] },
    { id:5, name:'Stade Central', capacity:3000, surface:'Natural Grass', district:'City Centre', lighting:'Stadium-Grade LED', yearBuilt:2018, description:'The crown jewel...', icon:'🌟', supportedSports:['Football'] },
    { id:6, name:'Grand Stade', capacity:5000, surface:'Premium Natural Grass', district:'Grand Boulevard', lighting:'Pro-Grade Broadcast', yearBuilt:2022, description:'The final destination...', icon:'🏆', supportedSports:['Football'] }
  ];

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
