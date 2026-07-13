import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserResponse } from './auth.service';

export interface AdminUserResponse {
  id: number;
  email: string;
  fullName: string;
  role: string;
  plan: string;
  status: string;
  suspensionReason: string | null;
  suspendedUntil: string | null;
  lastAdministrativeActionAt: string | null;
  reservationCount: number;
  matchCount: number;
  averageRating: number | null;
}

export interface AdminUserActionResponse {
  id: number;
  adminId: number;
  action: string;
  reason: string | null;
  createdAt: string;
}

export interface AdminUserDetailResponse {
  user: AdminUserResponse;
  actions: AdminUserActionResponse[];
}

export interface AdminUserPageResponse {
  content: AdminUserResponse[];
  totalElements: number;
  page: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class UserAdminService {
  private readonly apiUrl = `${environment.apiUrl}/users/admin`;

  constructor(private http: HttpClient) {}

  search(adminId: number, search: string, role: string, page: number, size: number): Observable<AdminUserPageResponse> {
    const params = new HttpParams()
      .set('adminId', adminId)
      .set('search', search)
      .set('role', role)
      .set('page', page)
      .set('size', size);
    return this.http.get<AdminUserPageResponse>(this.apiUrl, { params });
  }

  detail(adminId: number, userId: number): Observable<AdminUserDetailResponse> {
    return this.http.get<AdminUserDetailResponse>(`${this.apiUrl}/${userId}`, {
      params: new HttpParams().set('adminId', adminId)
    });
  }

  activate(adminId: number, userId: number, reason: string): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.apiUrl}/${userId}/activate`, { adminId, reason });
  }

  suspend(adminId: number, userId: number, reason: string, suspendedUntil: string | null): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.apiUrl}/${userId}/suspend`, { adminId, reason, suspendedUntil });
  }

  reactivate(adminId: number, userId: number, reason: string): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.apiUrl}/${userId}/reactivate`, { adminId, reason });
  }
}
