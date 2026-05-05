import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TypeExercice } from '../../shared/models/programme-entrainement.model';

export interface AIRecommendationRequest {
  objectifProgramme: string;
  typeSeance: TypeExercice | string;
  intensite: string;
  nbParticipants: number;
  dureeSeanceMinutes: number;
  niveauJoueurs?: string;
  lieuType?: string;
  enPleinAir?: boolean;
}

export interface AIExerciseRecommendation {
  nom: string;
  type: TypeExercice | string;
  description?: string;
  difficulte?: string;
  dureeMinutes?: number;
  equipement?: string;
  objectif?: string;
  niveauRecommande?: string;
  consigneSecurite?: string;
  scoreRelevance?: number;
  raison?: string;
}

export interface AIRecommendationResponse {
  status: 'ok' | 'fallback' | string;
  message?: string;
  nbRecommandations: number;
  recommandations: AIExerciseRecommendation[];
}

@Injectable({ providedIn: 'root' })
export class AiRecommendationService {
  private apiUrl = 'http://localhost:8080/api/coaching/ai';

  constructor(private http: HttpClient) {}

  health(): Observable<{ available: boolean; message: string }> {
    return this.http.get<{ available: boolean; message: string }>(`${this.apiUrl}/health`);
  }

  recommend(context: AIRecommendationRequest): Observable<AIRecommendationResponse> {
    return this.http.post<AIRecommendationResponse>(`${this.apiUrl}/recommend`, context);
  }
}
