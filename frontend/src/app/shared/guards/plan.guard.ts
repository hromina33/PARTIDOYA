import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const playerGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }
  if (!authService.isPlayerPlan()) {
    router.navigate([authService.dashboardRoute()]);
    return false;
  }
  return true;
};

export const courtAdminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }
  if (!authService.isCourtPlan()) {
    router.navigate([authService.dashboardRoute()]);
    return false;
  }
  return true;
};

export const platformAdminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }
  if (!authService.isPlatformAdmin()) {
    router.navigate([authService.dashboardRoute()]);
    return false;
  }
  return true;
};
