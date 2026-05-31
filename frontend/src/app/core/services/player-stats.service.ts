import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerStatsDTO } from '../../shared/models/sports.model';

@Injectable({ providedIn: 'root' })
export class PlayerStatsService {
  private readonly base = 'http://localhost:18080/player-stats';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PlayerStatsDTO[]> {
    return this.http.get<PlayerStatsDTO[]>(this.base);
  }

  getById(id: number): Observable<PlayerStatsDTO> {
    return this.http.get<PlayerStatsDTO>(`${this.base}/${id}`);
  }

  getByPlayer(playerId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/search/player/${playerId}`);
  }

  getByMatch(matchId: number): Observable<PlayerStatsDTO[]> {
    return this.http.get<PlayerStatsDTO[]>(`${this.base}/search/match/${matchId}`);
  }

  create(stats: Partial<PlayerStatsDTO>): Observable<PlayerStatsDTO> {
    return this.http.post<PlayerStatsDTO>(this.base, stats);
  }

  update(id: number, stats: Partial<PlayerStatsDTO>): Observable<PlayerStatsDTO> {
    return this.http.put<PlayerStatsDTO>(`${this.base}/${id}`, stats);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
