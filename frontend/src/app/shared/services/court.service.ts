import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CourtResponse {
  id: number;
  ownerId: number;
  name: string;
  complexName: string | null;
  description: string | null;
  address: string;
  district: string;
  latitude: number | null;
  longitude: number | null;
  pricePerHour: number;
  active: boolean;
  published: boolean;
  availableForReservations: boolean;
  sports: string[];
  schedules: string[];
  mainImageUrl: string | null;
  imageUrls: string[];
  services: string[];
  features: string[];
}

export interface SaveCourtRequest {
  ownerId?: number;
  requesterId?: number;
  name: string;
  complexName: string | null;
  description: string | null;
  address: string;
  district: string;
  latitude: number | null;
  longitude: number | null;
  pricePerHour: number;
  sports: string[];
  schedules: string[];
  mainImageUrl: string | null;
  imageUrls: string[];
  services: string[];
  features: string[];
}

export interface ReservationResponse {
  id: number;
  userId: number;
  courtId: number;
  date: string;
  startTime: string;
  endTime: string;
  price: number;
  status: string;
  paymentStatus: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class CourtService {
  private readonly apiUrl = `${environment.apiUrl}/courts`;

  constructor(private http: HttpClient) {}

  searchCourts(sport?: string, district?: string): Observable<CourtResponse[]> {
    let params = new HttpParams();
    if (sport) params = params.set('sport', sport);
    if (district) params = params.set('district', district);
    return this.http.get<CourtResponse[]>(this.apiUrl, { params });
  }

  getCourtById(id: number): Observable<CourtResponse> {
    return this.http.get<CourtResponse>(`${this.apiUrl}/${id}`);
  }

  getManagedCourts(ownerId: number): Observable<CourtResponse[]> {
    return this.http.get<CourtResponse[]>(`${this.apiUrl}/managed/${ownerId}`);
  }

  createCourt(request: SaveCourtRequest): Observable<CourtResponse> {
    return this.http.post<CourtResponse>(this.apiUrl, request);
  }

  updateCourt(id: number, request: SaveCourtRequest): Observable<CourtResponse> {
    return this.http.put<CourtResponse>(`${this.apiUrl}/${id}`, request);
  }

  setPublication(id: number, requesterId: number, published: boolean): Observable<CourtResponse> {
    return this.http.post<CourtResponse>(`${this.apiUrl}/${id}/publication`, { requesterId, published });
  }

  reserveCourt(id: number, userId: number, date: string, startTime: string, endTime: string, paymentMethod: string): Observable<ReservationResponse> {
    return this.http.post<ReservationResponse>(`${this.apiUrl}/${id}/reservations`, {
      userId,
      date,
      startTime,
      endTime,
      paymentMethod
    });
  }
}
