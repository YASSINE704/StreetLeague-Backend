import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservationSeance } from '../../shared/models/programme-entrainement.model';

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private apiUrl = 'http://localhost:18080/api/reservations-seances';

  constructor(private http: HttpClient) {}

  /** Réserver une place dans une séance */
  reserver(reservation: { seanceId: number; modePaiement: string }): Observable<ReservationSeance> {
    return this.http.post<ReservationSeance>(this.apiUrl, reservation);
  }

  /** Annuler une réservation */
  annuler(id: number, motif?: string): Observable<ReservationSeance> {
    return this.http.put<ReservationSeance>(`${this.apiUrl}/${id}/annuler`, { motif });
  }

  /** Confirmer une réservation (coach) */
  confirmer(id: number): Observable<ReservationSeance> {
    return this.http.put<ReservationSeance>(`${this.apiUrl}/${id}/confirmer`, {});
  }

  /** Réservations d'une séance */
  getBySeance(seanceId: number): Observable<ReservationSeance[]> {
    return this.http.get<ReservationSeance[]>(`${this.apiUrl}/seance/${seanceId}`);
  }

  /** Mes réservations (utilisateur connecté via JWT) */
  getMesReservations(): Observable<ReservationSeance[]> {
    return this.http.get<ReservationSeance[]>(`${this.apiUrl}/me`);
  }

  /** Réservations d'un sportif par ID */
  getByUser(userId: number): Observable<ReservationSeance[]> {
    return this.http.get<ReservationSeance[]>(`${this.apiUrl}/user/${userId}`);
  }

  /** Places restantes pour une séance */
  getPlacesRestantes(seanceId: number): Observable<{ placesRestantes: number }> {
    return this.http.get<{ placesRestantes: number }>(`${this.apiUrl}/seance/${seanceId}/places`);
  }
}
