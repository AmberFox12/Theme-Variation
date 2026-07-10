import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cours } from '../models/cours.model';

@Injectable({ providedIn: 'root' })
export class CoursService {
  private apiUrl = 'http://localhost:8080/api/cours';

  constructor(private http: HttpClient) {}

  getCours(): Observable<Cours[]> {
    return this.http.get<Cours[]>(this.apiUrl);
  }
}
