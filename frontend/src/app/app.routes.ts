import { Routes } from '@angular/router';
import { authGuard } from './shared/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'contactanos',
    loadComponent: () => import('./pages/contactanos/contactanos.component').then(m => m.ContactanosComponent)
  },
  {
    path: 'planes-jugador',
    loadComponent: () => import('./pages/planes-jugador/planes-jugador.component').then(m => m.PlanesJugadorComponent)
  },
  {
    path: 'planes-cancha',
    loadComponent: () => import('./pages/planes-cancha/planes-cancha.component').then(m => m.PlanesCanchaComponent)
  },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'deportes',
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
