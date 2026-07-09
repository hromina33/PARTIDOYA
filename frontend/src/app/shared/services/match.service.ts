import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface MatchResponse {
  id: number;
  organizerId: number;
  sport: string;
  title: string;
  description: string | null;
  address: string;
  matchDate: string;
  totalSlots: number;
  availableSlots: number;
  price: number | null;
  status: string;
  participantIds: number[];
}

export interface CreateMatchRequest {
  organizerId: number;
  sport: string;
  title: string;
  description: string | null;
  address: string;
  matchDate: string;
  totalSlots: number;
  price: number | null;
}

@Injectable({ providedIn: 'root' })
export class MatchService {
  private readonly apiUrl = `${environment.apiUrl}/matches`;

  constructor(private http: HttpClient) {}

  getOpenMatches(sport?: string): Observable<MatchResponse[]> {
    if (sport) {
      return this.http.get<MatchResponse[]>(this.apiUrl, { params: { sport } });
    }
    return this.http.get<MatchResponse[]>(this.apiUrl);
  }

  getMatchById(id: number): Observable<MatchResponse> {
    return this.http.get<MatchResponse>(`${this.apiUrl}/${id}`);
  }

  createMatch(request: CreateMatchRequest): Observable<MatchResponse> {
    return this.http.post<MatchResponse>(this.apiUrl, request);
  }

  joinMatch(matchId: number, userId: number): Observable<MatchResponse> {
    return this.http.post<MatchResponse>(`${this.apiUrl}/${matchId}/join`, { userId });
  }

  cancelMatch(matchId: number, requesterId: number): Observable<MatchResponse> {
    return this.http.post<MatchResponse>(`${this.apiUrl}/${matchId}/cancel`, { requesterId });
  }

  leaveMatch(matchId: number, userId: number): Observable<MatchResponse> {
    return this.http.post<MatchResponse>(`${this.apiUrl}/${matchId}/leave`, { userId });
  }

  getMatchesByOrganizer(userId: number): Observable<MatchResponse[]> {
    return this.http.get<MatchResponse[]>(`${this.apiUrl}/organized/${userId}`);
  }

  getMatchesByParticipant(userId: number): Observable<MatchResponse[]> {
    return this.http.get<MatchResponse[]>(`${this.apiUrl}/joined/${userId}`);
  }
}
