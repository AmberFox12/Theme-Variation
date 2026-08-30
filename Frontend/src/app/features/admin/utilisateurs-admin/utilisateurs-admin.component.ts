import { Component, OnInit, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';

interface Compte {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: string;
  telephone: string | null;
}

@Component({
  selector: 'app-utilisateurs-admin',
  templateUrl: './utilisateurs-admin.component.html',
  styleUrl: './utilisateurs-admin.component.scss'
})
export class UtilisateursAdminComponent implements OnInit {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  comptes = signal<Compte[]>([]);
  monEmail = this.authService.email();

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.http.get<Compte[]>('http://localhost:8080/api/comptes').subscribe(c => this.comptes.set(c));
  }

  supprimer(compte: Compte): void {
    const msg = `Supprimer le compte de ${compte.prenom} ${compte.nom} ?\n\nCette action supprimera aussi tous ses élèves et leurs inscriptions. Elle est irréversible.`;
    if (!confirm(msg)) return;
    this.http.delete(`http://localhost:8080/api/comptes/${compte.id}`)
      .subscribe(() => this.charger());
  }

  changerRole(compte: Compte): void {
    const nouveauRole = compte.role === 'ADMIN' ? 'ELEVE' : 'ADMIN';
    const msg = nouveauRole === 'ADMIN'
      ? `Donner les droits ADMIN à ${compte.prenom} ${compte.nom} ?`
      : `Retirer les droits ADMIN de ${compte.prenom} ${compte.nom} ?`;
    if (!confirm(msg)) return;

    this.http.patch<Compte>(`http://localhost:8080/api/comptes/${compte.id}/role`, { role: nouveauRole })
      .subscribe({
        next: () => this.charger(),
        error: (err) => alert(`Erreur lors du changement de rôle : ${err.status} ${err.statusText}`)
      });
  }
}
