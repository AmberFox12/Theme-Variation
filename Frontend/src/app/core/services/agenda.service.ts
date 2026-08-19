import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Evenement } from '../models/evenement.model';

export interface EvenementRequest {
  titre: string;
  description: string;
  dateHeure: string;
  lieu: string;
}

@Injectable({ providedIn: 'root' })
export class AgendaService {
  private apiUrl = 'http://localhost:8080/api/agenda';

  constructor(private http: HttpClient) {}

  getEvenements(): Observable<Evenement[]> {
    return this.http.get<Evenement[]>(this.apiUrl);
  }

  getProchainEvenement(): Observable<Evenement | null> {
    return this.http.get<Evenement>(`${this.apiUrl}/prochain`, { observe: 'response' }).pipe(
      map(response => response.status === 204 ? null : response.body)
    );
  }

  creer(request: EvenementRequest): Observable<Evenement> {
    return this.http.post<Evenement>(this.apiUrl, request);
  }

  modifier(id: number, request: EvenementRequest): Observable<Evenement> {
    return this.http.put<Evenement>(`${this.apiUrl}/${id}`, request);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
