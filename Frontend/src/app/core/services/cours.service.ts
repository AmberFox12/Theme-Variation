import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cours } from '../models/cours.model';
import { CoursRequest } from '../models/cours-request.model';
import { TypeDanse } from '../models/type-danse.model';

@Injectable({ providedIn: 'root' })
export class CoursService {
  private apiUrl = '/api/cours';
  private typesDanseUrl = '/api/types-danse';

  constructor(private http: HttpClient) {}

  getCours(): Observable<Cours[]> {
    return this.http.get<Cours[]>(this.apiUrl);
  }

  getTypesDanse(): Observable<TypeDanse[]> {
    return this.http.get<TypeDanse[]>(this.typesDanseUrl);
  }

  creer(request: CoursRequest): Observable<Cours> {
    return this.http.post<Cours>(this.apiUrl, request);
  }

  modifier(id: number, request: CoursRequest): Observable<Cours> {
    return this.http.put<Cours>(`${this.apiUrl}/${id}`, request);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
