import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Evenement } from '../models/evenement.model';

@Injectable({
  providedIn: 'root'
})
export class AgendaService {
  private apiUrl = 'http://localhost:8080/api/agenda';

  constructor(private http: HttpClient) {}

  getEvenements():Observable<Evenement[]> {
    return this.http.get<Evenement[]>(this.apiUrl);
  }
}
