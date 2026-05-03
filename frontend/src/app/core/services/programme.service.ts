import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProgrammeEntrainement, StatutProgramme } from '../../shared/models/programme-entrainement.model';

@Injectable({ providedIn: 'root' })
export class ProgrammeService {
  private apiUrl = 'http://localhost:8080/api/programmes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProgrammeEntrainement[]> {
    return this.http.get<ProgrammeEntrainement[]>(this.apiUrl);
  }

  getById(id: number): Observable<ProgrammeEntrainement> {
    return this.http.get<ProgrammeEntrainement>(`${this.apiUrl}/${id}`);
  }

  getByStatut(statut: StatutProgramme): Observable<ProgrammeEntrainement[]> {
    return this.http.get<ProgrammeEntrainement[]>(`${this.apiUrl}/statut/${statut}`);
  }

  create(programme: ProgrammeEntrainement): Observable<ProgrammeEntrainement> {
    return this.http.post<ProgrammeEntrainement>(this.apiUrl, programme);
  }

  update(id: number, programme: ProgrammeEntrainement): Observable<ProgrammeEntrainement> {
    return this.http.put<ProgrammeEntrainement>(`${this.apiUrl}/${id}`, programme);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
