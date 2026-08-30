import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Historique } from '../models/historique.model';
import { Spectacle } from '../models/spectacle.model';

export const BACKEND_BASE = 'http://localhost:8080';

@Injectable({ providedIn: 'root' })
export class ArchivesService {
  private readonly api = `${BACKEND_BASE}/api`;

  constructor(private http: HttpClient) {}

  // ── Historique ──
  getHistorique(): Observable<Historique[]> {
    return this.http.get<Historique[]>(`${this.api}/historique`);
  }

  creerHistorique(data: Omit<Historique, 'id'>): Observable<Historique> {
    return this.http.post<Historique>(`${this.api}/historique`, data);
  }

  modifierHistorique(id: number, data: Omit<Historique, 'id'>): Observable<Historique> {
    return this.http.put<Historique>(`${this.api}/historique/${id}`, data);
  }

  supprimerHistorique(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/historique/${id}`);
  }

  // ── Spectacles ──
  getSpectacles(): Observable<Spectacle[]> {
    return this.http.get<Spectacle[]>(`${this.api}/spectacles`);
  }

  creerSpectacle(data: Omit<Spectacle, 'id'>): Observable<Spectacle> {
    return this.http.post<Spectacle>(`${this.api}/spectacles`, data);
  }

  modifierSpectacle(id: number, data: Omit<Spectacle, 'id'>): Observable<Spectacle> {
    return this.http.put<Spectacle>(`${this.api}/spectacles/${id}`, data);
  }

  supprimerSpectacle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/spectacles/${id}`);
  }

  uploaderImageSpectacle(id: number, file: File): Observable<Spectacle> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Spectacle>(`${this.api}/spectacles/${id}/image`, formData);
  }
}
