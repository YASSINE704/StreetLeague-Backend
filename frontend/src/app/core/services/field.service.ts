import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Endroit, SousEspace, Equipement, Reservation, TypeEndroit, StatutEndroit } from '../models/endroit.model';

@Injectable({
  providedIn: 'root'
})
export class FieldService {
  private apiUrl = 'http://localhost:18080/api';

  constructor(private http: HttpClient) {}

  // Endroits
  getAllEndroits(): Observable<Endroit[]> {
    return this.http.get<Endroit[]>(`${this.apiUrl}/endroits`);
  }

  getEndroitById(id: number): Observable<Endroit> {
    return this.http.get<Endroit>(`${this.apiUrl}/endroits/${id}`);
  }

  createEndroit(endroit: Endroit): Observable<Endroit> {
    return this.http.post<Endroit>(`${this.apiUrl}/endroits`, endroit);
  }

  updateEndroit(id: number, endroit: Endroit): Observable<Endroit> {
    return this.http.put<Endroit>(`${this.apiUrl}/endroits/${id}`, endroit);
  }

  deleteEndroit(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/endroits/${id}`);
  }

  uploadImage(id: number, file: File): Observable<Endroit> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Endroit>(`${this.apiUrl}/endroits/${id}/image`, formData);
  }

  getEndroitsByType(type: TypeEndroit): Observable<Endroit[]> {
    return this.http.get<Endroit[]>(`${this.apiUrl}/endroits/type/${type}`);
  }

  getEndroitsByStatut(statut: StatutEndroit): Observable<Endroit[]> {
    return this.http.get<Endroit[]>(`${this.apiUrl}/endroits/statut/${statut}`);
  }

  // SousEspaces
  getAllSousEspaces(): Observable<SousEspace[]> {
    return this.http.get<SousEspace[]>(`${this.apiUrl}/sous-espaces`);
  }

  getSousEspaceById(id: number): Observable<SousEspace> {
    return this.http.get<SousEspace>(`${this.apiUrl}/sous-espaces/${id}`);
  }

  getSousEspacesByEndroit(endroitId: number): Observable<SousEspace[]> {
    return this.http.get<SousEspace[]>(`${this.apiUrl}/sous-espaces/endroit/${endroitId}`);
  }

  createSousEspace(endroitId: number, sousEspace: SousEspace): Observable<SousEspace> {
    return this.http.post<SousEspace>(`${this.apiUrl}/sous-espaces/endroit/${endroitId}`, sousEspace);
  }

  updateSousEspace(id: number, sousEspace: SousEspace): Observable<SousEspace> {
    return this.http.put<SousEspace>(`${this.apiUrl}/sous-espaces/${id}`, sousEspace);
  }

  deleteSousEspace(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/sous-espaces/${id}`);
  }

  // Equipements
  getEquipementsBySousEspace(sousEspaceId: number): Observable<Equipement[]> {
    return this.http.get<Equipement[]>(`${this.apiUrl}/equipements/sous-espace/${sousEspaceId}`);
  }

  createEquipement(sousEspaceId: number, equipement: Equipement): Observable<Equipement> {
    return this.http.post<Equipement>(`${this.apiUrl}/equipements/sous-espace/${sousEspaceId}`, equipement);
  }

  updateEquipement(id: number, equipement: Equipement): Observable<Equipement> {
    return this.http.put<Equipement>(`${this.apiUrl}/equipements/${id}`, equipement);
  }

  deleteEquipement(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/equipements/${id}`);
  }

  // Reservations
  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/reservations`);
  }

  getReservationsBySousEspace(sousEspaceId: number): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/reservations/sous-espace/${sousEspaceId}`);
  }

  createReservation(sousEspaceId: number, reservation: Reservation): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.apiUrl}/reservations/sous-espace/${sousEspaceId}`, reservation);
  }

  confirmerReservation(id: number): Observable<Reservation> {
    return this.http.patch<Reservation>(`${this.apiUrl}/reservations/${id}/confirmer`, {});
  }

  annulerReservation(id: number, motif: string): Observable<Reservation> {
    return this.http.patch<Reservation>(`${this.apiUrl}/reservations/${id}/annuler?motif=${encodeURIComponent(motif)}`, {});
  }

  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reservations/${id}`);
  }
}
