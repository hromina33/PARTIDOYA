import { Routes } from '@angular/router';
import { authGuard } from './shared/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'home',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'deportes',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/deportes/deportes.component').then(m => m.DeportesComponent)
  },
  {
    path: 'mis-partidos',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/mis-partidos/mis-partidos.component').then(m => m.MisPartidosComponent)
  },
  {
    path: 'partidos/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/match-detail/match-detail.component').then(m => m.MatchDetailComponent)
  },
  {
    path: 'mi-perfil',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/mi-perfil/mi-perfil.component').then(m => m.MiPerfilComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
