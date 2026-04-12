// Enums matching backend
export type Position = 'GOALKEEPER' | 'DEFENDER' | 'MIDFIELDER' | 'FORWARD' | 'UTILITY';
export type Niveau = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type TypeSport = 'FOOTBALL' | 'BASKETBALL' | 'VOLLEYBALL' | 'PADEL';
export type MatchStatus = 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

// DTOs matching backend responses
export interface PlayerDTO {
  id: number;
  nom: string;
  age: number;
  niveau: Niveau;
  position: Position;
  equipeId?: number;
  equipeName?: string;
  totalGoals: number;
  totalAssists: number;
  matchesPlayed: number;
  averageRating: number;
}

export interface TeamStatisticsDTO {
  teamId: number;
  teamName: string;
  wins: number;
  losses: number;
  draws: number;
  totalMatches: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDifference: number;
  winPercentage: number;
  points: number;
}

export interface TeamDTO {
  id: number;
  nom: string;
  typeSport: TypeSport;
  joueurs: PlayerDTO[];
  statistics: TeamStatisticsDTO;
  totalPlayers: number;
}

export interface TerrainDTO {
  id: number;
  nom: string;
  typeSport: TypeSport;
  location: string;
  address: string;
  availabilityStart?: string;
  availabilityEnd?: string;
}

export interface MatchDTO {
  id: number;
  homeTeamId: number;
  homeTeamName: string;
  awayTeamId: number;
  awayTeamName: string;
  terrainId: number;
  terrainName: string;
  terrainAddress: string;
  matchDate: string;
  status: MatchStatus;
  homeTeamScore?: number;
  awayTeamScore?: number;
  result?: string;
}

export interface PlayerStatsDTO {
  id: number;
  playerId: number;
  playerName: string;
  matchId: number;
  matchDate: string;
  opponent: string;
  goals: number;
  assists: number;
  minutesPlayed: number;
  performanceRating: number;
}

// Request payloads for creating/updating
export interface PlayerRequest {
  nom: string;
  age: number;
  niveau: Niveau;
  position: Position;
  equipe?: { id: number };
}

export interface TeamRequest {
  nom: string;
  typeSport: TypeSport;
}

export interface TerrainRequest {
  nom: string;
  typeSport: TypeSport;
  location: string;
  address: string;
}

export interface MatchRequest {
  homeTeam: { id: number };
  awayTeam: { id: number };
  terrain: { id: number };
  matchDate: string;
}
