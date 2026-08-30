import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parametre } from '../models/parametre.model';

@Injectable({ providedIn: 'root' })
export class ParametreService {
  private api = 'http://localhost:8080/api/parametres';

  constructor(private http: HttpClient) {}

  get(): Observable<Parametre> {
    return this.http.get<Parametre>(this.api);
  }

  update(p: Parametre): Observable<Parametre> {
    return this.http.put<Parametre>(this.api, p);
  }
}
