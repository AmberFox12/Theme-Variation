import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { RegisterRequest } from '../models/register-request.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'auth_token';

  estConnecte = signal<boolean>(this.getToken() !== null);
  role = signal<string>(this.extraireRole());

  constructor(private http: HttpClient) {}

  register(data: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/register`, data);
  }

  login(email: string, motDePasse: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, { email, motDePasse }).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.token);
        this.estConnecte.set(true);
        this.role.set(this.extraireRole());
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.estConnecte.set(false);
    this.role.set('');
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private extraireRole(): string {
    const token = this.getToken();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role ?? '';
    } catch {
      return '';
    }
  }
}
