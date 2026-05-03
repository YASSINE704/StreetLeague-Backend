import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SeanceEntrainement } from '../../shared/models/programme-entrainement.model';

@Injectable({ providedIn: 'root' })
export class SeanceService {
  private apiUrl = 'http://localhost:8080/api/seances';

  constructor(private http: HttpClient) {}

  getAll(): Observable<SeanceEntrainement[]> {
    return this.http.get<SeanceEntrainement[]>(this.apiUrl);
  }

  getById(id: number): Observable<SeanceEntrainement> {
    return this.http.get<SeanceEntrainement>(`${this.apiUrl}/${id}`);
  }

  getByProgramme(programmeId: number): Observable<SeanceEntrainement[]> {
    return this.http.get<SeanceEntrainement[]>(`${this.apiUrl}/programme/${programmeId}`);
  }

  create(seance: SeanceEntrainement): Observable<SeanceEntrainement> {
    return this.http.post<SeanceEntrainement>(this.apiUrl, seance);
  }

  update(id: number, seance: SeanceEntrainement): Observable<SeanceEntrainement> {
    return this.http.put<SeanceEntrainement>(`${this.apiUrl}/${id}`, seance);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
