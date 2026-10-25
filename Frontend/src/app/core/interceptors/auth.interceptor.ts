import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  const reqAvecToken = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(reqAvecToken).pipe(
    tap(event => {
      if (event instanceof HttpResponse) {
        const nouveauToken = event.headers.get('X-Refresh-Token');
        if (nouveauToken) {
          authService.updateToken(nouveauToken);
        }
      }
    }),
    catchError(err => {
      if (err.status === 401 && authService.estConnecte()) {
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};
