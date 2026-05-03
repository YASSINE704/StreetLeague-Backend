import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TerrainDTO, MatchDTO, TerrainRequest } from '../../shared/models/sports.model';

@Injectable({ providedIn: 'root' })
export class TerrainService {
  private readonly base = 'http://localhost:8080/terrains';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TerrainDTO[]> {
    return this.http.get<TerrainDTO[]>(this.base);
  }

  getById(id: number): Observable<TerrainDTO> {
    return this.http.get<TerrainDTO>(`${this.base}/${id}`);
  }

  create(terrain: TerrainRequest): Observable<TerrainDTO> {
    return this.http.post<TerrainDTO>(this.base, terrain);
  }

  update(id: number, terrain: TerrainRequest): Observable<TerrainDTO> {
    return this.http.put<TerrainDTO>(`${this.base}/${id}`, terrain);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getBookings(id: number): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(`${this.base}/${id}/bookings`);
  }

  checkAvailability(id: number, dateTime: string): Observable<{ available: boolean }> {
    return this.http.get<{ available: boolean }>(`${this.base}/${id}/availability`, {
      params: { dateTime }
    });
  }
}
