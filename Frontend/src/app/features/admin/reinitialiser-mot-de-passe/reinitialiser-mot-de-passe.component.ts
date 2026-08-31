import { Component, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-reinitialiser-mot-de-passe',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './reinitialiser-mot-de-passe.component.html',
  styleUrl: './reinitialiser-mot-de-passe.component.scss'
})
export class ReinitialiserMotDePasseComponent implements OnInit {
  token = signal('');
  tokenManquant = signal(false);
  enCours = signal(false);
  confirme = signal(false);
  erreur = signal('');

  form = new FormGroup({
    nouveauMotDePasse: new FormControl('', [Validators.required, Validators.minLength(8)]),
    confirmation: new FormControl('', Validators.required)
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const t = this.route.snapshot.queryParamMap.get('token');
    if (!t) { this.tokenManquant.set(true); return; }
    this.token.set(t);
  }

  get motDePasseInvalide(): boolean {
    const mdp = this.form.value.nouveauMotDePasse ?? '';
    const conf = this.form.value.confirmation ?? '';
    return mdp !== conf;
  }

  onSubmit(): void {
    if (this.form.invalid || this.motDePasseInvalide || this.enCours()) return;
    this.erreur.set('');
    this.enCours.set(true);
    this.authService.reinitialiserMotDePasse(this.token(), this.form.value.nouveauMotDePasse!).subscribe({
      next: () => {
        this.enCours.set(false);
        this.confirme.set(true);
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: (err) => {
        this.enCours.set(false);
        this.erreur.set(err?.error?.message ?? 'Lien invalide ou expiré. Veuillez refaire une demande.');
      }
    });
  }
}
