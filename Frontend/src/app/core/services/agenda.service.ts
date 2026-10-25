import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Evenement } from '../models/evenement.model';

export interface EvenementRequest {
  titre: string;
  description: string;
  dateHeure: string;
  dateFin?: string;
  lieu: string;
}

@Injectable({ providedIn: 'root' })
export class AgendaService {
  private apiUrl = '/api/agenda';

  constructor(private http: HttpClient) {}

  getEvenements(): Observable<Evenement[]> {
    return this.http.get<Evenement[]>(this.apiUrl);
  }

  getProchainEvenement(): Observable<Evenement | null> {
    return this.http.get<Evenement>(`${this.apiUrl}/prochain`, { observe: 'response' }).pipe(
      map(response => response.status === 204 ? null : response.body)
    );
  }

  getEvenementEnCours(): Observable<Evenement | null> {
    return this.http.get<Evenement>(`${this.apiUrl}/en-cours`, { observe: 'response' }).pipe(
      map(response => response.status === 204 ? null : response.body)
    );
  }

  creer(request: EvenementRequest): Observable<Evenement> {
    return this.http.post<Evenement>(this.apiUrl, request);
  }

  modifier(id: number, request: EvenementRequest): Observable<Evenement> {
    return this.http.put<Evenement>(`${this.apiUrl}/${id}`, request);
  }

  uploaderImage(id: number, file: File): Observable<Evenement> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Evenement>(`${this.apiUrl}/${id}/image`, formData);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
