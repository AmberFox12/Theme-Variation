import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map, fromEvent } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';

const PAGES_GRADIENT = ['/', '/archives', '/inscription', '/contact'];

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  authService = inject(AuthService);
  private router = inject(Router);

  estPageGradient = toSignal(
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      map(() => PAGES_GRADIENT.includes(this.router.url))
    ),
    { initialValue: PAGES_GRADIENT.includes(this.router.url) }
  );

  estDefilee = toSignal(
    fromEvent(document, 'scroll').pipe(
      map(() => window.scrollY > 80)
    ),
    { initialValue: false }
  );

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
