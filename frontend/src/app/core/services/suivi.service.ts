import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SuiviSeance } from '../../shared/models/programme-entrainement.model';

@Injectable({ providedIn: 'root' })
export class SuiviService {
  private apiUrl = 'http://localhost:8080/api/suivis';

  constructor(private http: HttpClient) {}

  getAll(): Observable<SuiviSeance[]> {
    return this.http.get<SuiviSeance[]>(this.apiUrl);
  }

  getById(id: number): Observable<SuiviSeance> {
    return this.http.get<SuiviSeance>(`${this.apiUrl}/${id}`);
  }

  getBySeance(seanceId: number): Observable<SuiviSeance> {
    return this.http.get<SuiviSeance>(`${this.apiUrl}/seance/${seanceId}`);
  }

  create(suivi: SuiviSeance): Observable<SuiviSeance> {
    return this.http.post<SuiviSeance>(this.apiUrl, suivi);
  }

  update(id: number, suivi: SuiviSeance): Observable<SuiviSeance> {
    return this.http.put<SuiviSeance>(`${this.apiUrl}/${id}`, suivi);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
