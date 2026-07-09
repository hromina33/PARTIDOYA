import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface SportPreferenceResponse {
  sportName: string;
  skillLevel: string;
}

export interface ProfileResponse {
  id: number;
  userId: number;
  phoneNumber: string | null;
  avatarUrl: string | null;
  primarySport: string | null;
  skillLevel: string | null;
  availability: string[];
  sportPreferences: SportPreferenceResponse[];
}

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getProfileByUserId(userId: number): Observable<ProfileResponse> {
    return this.http.get<ProfileResponse>(`${this.apiUrl}/profiles/user/${userId}`);
  }

  updatePrimarySport(userId: number, primarySport: string): Observable<ProfileResponse> {
    return this.http.patch<ProfileResponse>(`${this.apiUrl}/profiles/user/${userId}/primary-sport`, { primarySport });
  }

  updateGamePreferences(userId: number, skillLevel: string, availability: string[]): Observable<ProfileResponse> {
    return this.http.patch<ProfileResponse>(`${this.apiUrl}/profiles/user/${userId}/game-preferences`, { skillLevel, availability });
  }

  updateFullName(userId: number, fullName: string): Observable<{ id: number; email: string; fullName: string; emailVerified: boolean; role: string; plan: string }> {
    return this.http.patch<{ id: number; email: string; fullName: string; emailVerified: boolean; role: string; plan: string }>(`${this.apiUrl}/users/${userId}`, { fullName });
  }

  updatePlan(userId: number, plan: string): Observable<{ id: number; email: string; fullName: string; emailVerified: boolean; role: string; plan: string }> {
    return this.http.patch<{ id: number; email: string; fullName: string; emailVerified: boolean; role: string; plan: string }>(`${this.apiUrl}/users/${userId}/plan`, { plan });
  }
}
