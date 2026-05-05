import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerDTO, PlayerStatsDTO, PlayerRequest } from '../../shared/models/sports.model';

@Injectable({ providedIn: 'root' })
export class PlayerService {
  private readonly base = 'http://localhost:18080/players';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PlayerDTO[]> {
    return this.http.get<PlayerDTO[]>(this.base);
  }

  getMe(): Observable<PlayerDTO> {
    return this.http.get<PlayerDTO>(`${this.base}/me`);
  }

  getById(id: number): Observable<PlayerDTO> {
    return this.http.get<PlayerDTO>(`${this.base}/${id}`);
  }

  create(player: PlayerRequest): Observable<PlayerDTO> {
    return this.http.post<PlayerDTO>(this.base, player);
  }

  update(id: number, player: PlayerRequest): Observable<PlayerDTO> {
    return this.http.put<PlayerDTO>(`${this.base}/${id}`, player);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getStatistics(playerId: number): Observable<PlayerStatsDTO[]> {
    return this.http.get<PlayerStatsDTO[]>(`${this.base}/${playerId}/statistics`);
  }
}
