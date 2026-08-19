import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Eleve, Inscription } from '../models/inscription.model';
import { Cours } from '../models/cours.model';

@Injectable({ providedIn: 'root' })
export class InscriptionService {
  private readonly api = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Inscription[]> {
    return this.http.get<Inscription[]>(`${this.api}/inscriptions`);
  }

  getEleves(): Observable<Eleve[]> {
    return this.http.get<Eleve[]>(`${this.api}/eleves`);
  }

  getCours(): Observable<Cours[]> {
    return this.http.get<Cours[]>(`${this.api}/cours`);
  }

  creerEleve(data: { nom: string; prenom: string; email: string; telephone: string; dateNaissance?: string }): Observable<Eleve> {
    return this.http.post<Eleve>(`${this.api}/eleves`, data);
  }

  creerInscription(eleveId: number, coursId: number, statut: string): Observable<Inscription> {
    return this.http.post<Inscription>(`${this.api}/inscriptions`, { eleveId, coursId, statut });
  }

  updateStatut(id: number, statut: string): Observable<Inscription> {
    return this.http.put<Inscription>(`${this.api}/inscriptions/${id}/statut`, { statut });
  }

  supprimerInscription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/inscriptions/${id}`);
  }

  soumettrePublique(data: {
    nom: string; prenom: string; email: string; telephone: string; adresse: string;
    eleves: { nom: string; prenom: string; dateNaissance: string | null; coursIds: number[] }[];
  }): Observable<{ message: string; nombreEleves: number; nombreInscriptions: number }> {
    return this.http.post<any>(`${this.api}/inscriptions/publique`, data);
  }
}
