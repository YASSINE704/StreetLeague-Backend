import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Exercice, SeanceExercice, TypeExercice } from '../../shared/models/programme-entrainement.model';

@Injectable({ providedIn: 'root' })
export class ExerciceService {
  private apiUrl = 'http://localhost:8080/api/exercices';
  private seanceExerciceUrl = 'http://localhost:8080/api/seance-exercices';

  constructor(private http: HttpClient) {}

  // --- Exercice CRUD ---
  getAll(): Observable<Exercice[]> {
    return this.http.get<Exercice[]>(this.apiUrl);
  }

  getById(id: number): Observable<Exercice> {
    return this.http.get<Exercice>(`${this.apiUrl}/${id}`);
  }

  getByType(type: TypeExercice): Observable<Exercice[]> {
    return this.http.get<Exercice[]>(`${this.apiUrl}/type/${type}`);
  }

  create(exercice: Exercice): Observable<Exercice> {
    return this.http.post<Exercice>(this.apiUrl, exercice);
  }

  update(id: number, exercice: Exercice): Observable<Exercice> {
    return this.http.put<Exercice>(`${this.apiUrl}/${id}`, exercice);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // --- SeanceExercice ---
  getBySeance(seanceId: number): Observable<SeanceExercice[]> {
    return this.http.get<SeanceExercice[]>(`${this.seanceExerciceUrl}/seance/${seanceId}`);
  }

  getByExercice(exerciceId: number): Observable<SeanceExercice[]> {
    return this.http.get<SeanceExercice[]>(`${this.seanceExerciceUrl}/exercice/${exerciceId}`);
  }

  addToSeance(se: SeanceExercice): Observable<SeanceExercice> {
    return this.http.post<SeanceExercice>(this.seanceExerciceUrl, se);
  }

  updateSeanceExercice(id: number, se: SeanceExercice): Observable<SeanceExercice> {
    return this.http.put<SeanceExercice>(`${this.seanceExerciceUrl}/${id}`, se);
  }

  removeFromSeance(id: number): Observable<void> {
    return this.http.delete<void>(`${this.seanceExerciceUrl}/${id}`);
  }
}
