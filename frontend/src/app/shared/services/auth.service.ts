import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AuthResponse {
  token: string;
  userId: number;
  email: string;
  fullName: string;
  role: string;
  plan: string;
}

export interface UserResponse {
  id: number;
  email: string;
  fullName: string;
  emailVerified: boolean;
  role: string;
  plan: string;
  status?: string;
  suspensionReason?: string | null;
  suspendedUntil?: string | null;
  lastAdministrativeActionAt?: string | null;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  register(fullName: string, email: string, password: string, role: string): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiUrl}/auth/register`, {
      fullName,
      email,
      password,
      role
    });
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, {
      email,
      password
    }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userId', response.userId.toString());
        localStorage.setItem('userName', response.fullName);
        localStorage.setItem('email', response.email);
        localStorage.setItem('role', response.role);
        localStorage.setItem('plan', response.plan);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('email');
    localStorage.removeItem('role');
    localStorage.removeItem('plan');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getUserId(): number | null {
    const id = localStorage.getItem('userId');
    return id ? parseInt(id, 10) : null;
  }

  getUserName(): string | null {
    return localStorage.getItem('userName');
  }

  getUserEmail(): string | null {
    return localStorage.getItem('email');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getPlan(): string | null {
    return localStorage.getItem('plan');
  }

  isPlayerPlan(): boolean {
    const plan = this.getPlan();
    return this.getRole() === 'JUGADOR' && (plan === 'JUGADOR_BASICO' || plan === 'JUGADOR_PLUS');
  }

  isCourtPlan(): boolean {
    const plan = this.getPlan();
    return this.getRole() === 'ADMIN_CANCHA' && (plan === 'CANCHA_EMPRENDEDOR' || plan === 'CANCHA_BUSINESS');
  }

  isPlatformAdmin(): boolean {
    return this.getRole() === 'ADMIN_GENERAL' && this.getPlan() === 'ADMIN_GENERAL';
  }

  dashboardRoute(): string {
    return '/home';
  }

  setPlan(plan: string): void {
    localStorage.setItem('plan', plan);
  }

  isAdminCancha(): boolean {
    return this.getRole() === 'ADMIN_CANCHA';
  }
}
