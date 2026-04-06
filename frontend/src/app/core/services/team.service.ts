import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TeamDTO, TeamStatisticsDTO, PlayerDTO, TeamRequest } from '../../shared/models/sports.model';

@Injectable({ providedIn: 'root' })
export class TeamService {
  private readonly base = 'http://localhost:18080/teams';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TeamDTO[]> {
    return this.http.get<TeamDTO[]>(this.base);
  }

  getById(id: number): Observable<TeamDTO> {
    return this.http.get<TeamDTO>(`${this.base}/${id}`);
  }

  create(team: TeamRequest): Observable<TeamDTO> {
    return this.http.post<TeamDTO>(this.base, team);
  }

  update(id: number, team: TeamRequest): Observable<TeamDTO> {
    return this.http.put<TeamDTO>(`${this.base}/${id}`, team);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  addPlayer(teamId: number, playerId: number): Observable<TeamDTO> {
    return this.http.post<TeamDTO>(`${this.base}/${teamId}/players/${playerId}`, {});
  }

  removePlayer(teamId: number, playerId: number): Observable<TeamDTO> {
    return this.http.delete<TeamDTO>(`${this.base}/${teamId}/players/${playerId}`);
  }

  getLineup(teamId: number): Observable<PlayerDTO[]> {
    return this.http.get<PlayerDTO[]>(`${this.base}/${teamId}/lineup`);
  }

  getStatistics(teamId: number): Observable<TeamStatisticsDTO> {
    return this.http.get<TeamStatisticsDTO>(`${this.base}/${teamId}/statistics`);
  }
}
