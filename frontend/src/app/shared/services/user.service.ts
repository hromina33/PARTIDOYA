import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserResponse } from './auth.service';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly apiUrl = `${environment.apiUrl}/users`;
  private readonly cache = new Map<number, UserResponse>();

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<UserResponse> {
    const cached = this.cache.get(id);
    if (cached) return of(cached);
    return this.http.get<UserResponse>(`${this.apiUrl}/${id}`).pipe(
      tap(user => this.cache.set(id, user))
    );
  }
}
