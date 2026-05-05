import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ForecastPoint {
  date: string;
  predicted: number;
}

export interface HistoryPoint {
  date: string;
  count: number;
}

export interface ForecastResponse {
  forecast: ForecastPoint[];
  model: string;
  historyPoints: number;
  history: HistoryPoint[];
  error?: string;
}

@Injectable({ providedIn: 'root' })
export class ForecastService {
  private apiUrl = 'http://localhost:18080/api/forecast';

  constructor(private http: HttpClient) {}

  getForecast(days: number = 7): Observable<ForecastResponse> {
    return this.http.get<ForecastResponse>(`${this.apiUrl}?days=${days}`);
  }
}
