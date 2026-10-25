import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { toSignal, takeUntilDestroyed } from '@angular/core/rxjs-interop';
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

  menuOuvert = signal<boolean>(false);

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

  private readonly _closeOnNav = this.router.events.pipe(
    filter(e => e instanceof NavigationEnd),
    takeUntilDestroyed()
  ).subscribe(() => this.menuOuvert.set(false));

  toggleMenu(): void {
    this.menuOuvert.update(v => !v);
  }

  logout(): void {
    this.menuOuvert.set(false);
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
