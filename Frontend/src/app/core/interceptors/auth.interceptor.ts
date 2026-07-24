import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const reqAvecToken = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(reqAvecToken);
  }

  return next(req);
};
