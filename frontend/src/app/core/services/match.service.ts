import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MatchDTO, MatchRequest } from '../../shared/models/sports.model';

@Injectable({ providedIn: 'root' })
export class MatchService {
  private readonly base = 'http://localhost:8080/matches';

  constructor(private http: HttpClient) {}

  getAll(): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(this.base);
  }

  getById(id: number): Observable<MatchDTO> {
    return this.http.get<MatchDTO>(`${this.base}/${id}`);
  }

  create(match: MatchRequest): Observable<MatchDTO> {
    return this.http.post<MatchDTO>(this.base, match);
  }

  update(id: number, match: MatchRequest): Observable<MatchDTO> {
    return this.http.put<MatchDTO>(`${this.base}/${id}`, match);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  recordResult(id: number, homeTeamScore: number, awayTeamScore: number): Observable<MatchDTO> {
    return this.http.put<MatchDTO>(`${this.base}/${id}/result`, null, {
      params: { homeTeamScore: homeTeamScore.toString(), awayTeamScore: awayTeamScore.toString() }
    });
  }

  getScheduled(): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(`${this.base}/search/scheduled`);
  }

  getHistory(): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(`${this.base}/search/history`);
  }

  getByTeam(teamId: number): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(`${this.base}/search/team/${teamId}`);
  }

  getByTerrain(terrainId: number): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(`${this.base}/search/terrain/${terrainId}`);
  }
}
