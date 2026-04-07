import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AffectationProgramme } from '../../shared/models/programme-entrainement.model';

@Injectable({ providedIn: 'root' })
export class AffectationService {
  private apiUrl = 'http://localhost:18080/api/affectations';

  constructor(private http: HttpClient) {}

  create(affectation: AffectationProgramme): Observable<AffectationProgramme> {
    return this.http.post<AffectationProgramme>(this.apiUrl, affectation);
  }

  getByProgramme(programmeId: number): Observable<AffectationProgramme[]> {
    return this.http.get<AffectationProgramme[]>(`${this.apiUrl}/programme/${programmeId}`);
  }

  getByUser(userId: number): Observable<AffectationProgramme[]> {
    return this.http.get<AffectationProgramme[]>(`${this.apiUrl}/user/${userId}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
