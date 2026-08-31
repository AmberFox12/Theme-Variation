import { Component, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-mot-de-passe-oublie',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './mot-de-passe-oublie.component.html',
  styleUrl: './mot-de-passe-oublie.component.scss'
})
export class MotDePasseOublieComponent {
  emailControl = new FormControl('', [Validators.required, Validators.email]);
  enCours = signal(false);
  confirme = signal(false);
  erreur = signal('');

  constructor(private authService: AuthService) {}

  onSubmit(): void {
    if (this.emailControl.invalid || this.enCours()) return;
    this.erreur.set('');
    this.enCours.set(true);
    this.authService.demanderReset(this.emailControl.value!).subscribe({
      next: () => { this.enCours.set(false); this.confirme.set(true); },
      error: () => { this.enCours.set(false); this.erreur.set('Une erreur est survenue. Veuillez réessayer.'); }
    });
  }
}
