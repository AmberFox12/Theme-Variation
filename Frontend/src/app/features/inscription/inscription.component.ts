import { Component, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { RegisterRequest } from '../../core/models/register-request.model';

@Component({
  selector: 'app-inscription',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription.component.html',
  styleUrl: './inscription.component.scss'
})
export class InscriptionComponent {
  form = new FormGroup({
    nom: new FormControl('', Validators.required),
    prenom: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    motDePasse: new FormControl('', [Validators.required, Validators.minLength(6)]),
    telephone: new FormControl('')
  });

  formStatus = toSignal(this.form.statusChanges, { initialValue: 'INVALID' });

  successMessage = signal('');
  errorMessage = signal('');

  constructor(private authService: AuthService) {}

  onSubmit(): void {
    if (this.form.valid) {
      this.authService.register(this.form.value as RegisterRequest).subscribe({
        next: () => {
          this.successMessage.set('Inscription réussie ! Vous pouvez maintenant vous connecter.');
          this.form.reset();
        },
        error: () => this.errorMessage.set('Erreur lors de l\'inscription. Cet email est peut-être déjà utilisé.')
      });
    }
  }
}
