import { Component, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    motDePasse: new FormControl('', Validators.required)
  });

  formStatus = toSignal(this.form.statusChanges, { initialValue: 'INVALID' });
  errorMessage = signal('');

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (this.form.valid) {
      const { email, motDePasse } = this.form.value;
      this.authService.login(email!, motDePasse!).subscribe({
        next: () => this.router.navigate(['/admin']),
        error: () => this.errorMessage.set('Email ou mot de passe incorrect.')
      });
    }
  }
}
