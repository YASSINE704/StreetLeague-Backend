import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Recommendation {
  id: number;
  nom: string;
  type: string;
  ville: string;
  adresse: string;
  capacite: number;
  statut: string;
  imageUrl: string | null;
  description: string | null;
  latitude: number;
  longitude: number;
  score: number;
  totalReservations: number;
  reasons: string[];
}

@Injectable({ providedIn: 'root' })
export class RecommendationService {
  private apiUrl = 'http://localhost:18080/api/recommendations';

  constructor(private http: HttpClient) {}

  getRecommendations(userId: number = 0, limit: number = 5): Observable<Recommendation[]> {
    return this.http.get<Recommendation[]>(`${this.apiUrl}?userId=${userId}&limit=${limit}`);
  }
}
