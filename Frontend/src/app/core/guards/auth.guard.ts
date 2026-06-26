import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  return true; // À implémenter : vérifier si l'utilisateur est connecté
};
