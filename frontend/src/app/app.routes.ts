import { Routes } from '@angular/router';
import { authGuard } from './shared/guards/auth.guard';
import { guestGuard } from './shared/guards/guest.guard';
import { courtAdminGuard, playerGuard } from './shared/guards/plan.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    path: 'register',
    canActivate: [guestGuard],
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
    canActivate: [authGuard],
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'deportes',
    canActivate: [playerGuard],
    loadComponent: () => import('./pages/deportes/deportes.component').then(m => m.DeportesComponent)
  },
  {
    path: 'canchas',
    canActivate: [playerGuard],
    loadComponent: () => import('./pages/canchas/canchas.component').then(m => m.CanchasComponent)
  },
  {
    path: 'canchas/:id',
    canActivate: [playerGuard],
    loadComponent: () => import('./pages/cancha-detail/cancha-detail.component').then(m => m.CanchaDetailComponent)
  },
  {
    path: 'admin-canchas',
    canActivate: [courtAdminGuard],
    loadComponent: () => import('./pages/admin-canchas/admin-canchas.component').then(m => m.AdminCanchasComponent)
  },
  {
    path: 'mis-partidos',
    canActivate: [playerGuard],
    loadComponent: () => import('./pages/mis-partidos/mis-partidos.component').then(m => m.MisPartidosComponent)
  },
  {
    path: 'partidos/:id',
    canActivate: [playerGuard],
    loadComponent: () => import('./pages/match-detail/match-detail.component').then(m => m.MatchDetailComponent)
  },
  {
    path: 'mi-perfil',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/mi-perfil/mi-perfil.component').then(m => m.MiPerfilComponent)
  },
  {
    path: 'notificaciones',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/notificaciones/notificaciones.component').then(m => m.NotificacionesComponent)
  },
  {
    path: 'notificaciones/:matchId/:requestId',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/notificacion-detalle/notificacion-detalle.component').then(m => m.NotificacionDetalleComponent)
  },
  {
    path: 'profile',
    redirectTo: 'mi-perfil'
  },
  {
    path: '**',
    redirectTo: ''
  }
];
