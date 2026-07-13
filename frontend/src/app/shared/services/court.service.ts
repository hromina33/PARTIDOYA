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
  currency: string;
  providerReference: string | null;
  status: string;
  paymentStatus: string;
  createdAt: string;
}

export interface ManagedReservationResponse {
  id: number;
  userId: number;
  customerName: string;
  courtId: number;
  courtName: string;
  date: string;
  startTime: string;
  endTime: string;
  price: number;
  currency: string;
  providerReference: string | null;
  status: string;
  paymentStatus: string;
  createdAt: string;
}

export interface CourtAvailabilityResponse {
  id: number;
  courtId: number;
  courtName: string;
  date: string;
  allDay: boolean;
  startTime: string;
  endTime: string;
  type: 'AVAILABLE' | 'BLOCKED';
  reason: string | null;
  createdAt: string;
}

export interface SaveCourtAvailabilityRequest {
  courtId: number;
  date: string;
  allDay: boolean;
  startTime: string;
  endTime: string;
  type: 'AVAILABLE' | 'BLOCKED';
  reason: string | null;
}

export interface DailyIncomeResponse {
  date: string;
  income: number;
  reservations: number;
}

export interface CourtRankingResponse {
  courtId: number;
  courtName: string;
  reservations: number;
  percentage: number;
}

export interface CourtReportResponse {
  from: string;
  to: string;
  totalIncome: number;
  confirmedReservations: number;
  canceledReservations: number;
  occupancyPercentage: number;
  averageTicket: number;
  dailyIncome: DailyIncomeResponse[];
  topCourt: CourtRankingResponse | null;
  ranking: CourtRankingResponse[];
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

  getManagedReservations(
    ownerId: number,
    from: string,
    to: string,
    courtId?: number
  ): Observable<ManagedReservationResponse[]> {
    let params = new HttpParams().set('from', from).set('to', to);
    if (courtId) params = params.set('courtId', courtId);
    return this.http.get<ManagedReservationResponse[]>(`${this.apiUrl}/managed/${ownerId}/reservations`, { params });
  }

  getManagedAvailability(ownerId: number, from: string, to: string, courtId?: number): Observable<CourtAvailabilityResponse[]> {
    let params = new HttpParams().set('from', from).set('to', to);
    if (courtId) params = params.set('courtId', courtId);
    return this.http.get<CourtAvailabilityResponse[]>(`${this.apiUrl}/managed/${ownerId}/availability`, { params });
  }

  createAvailability(ownerId: number, request: SaveCourtAvailabilityRequest): Observable<CourtAvailabilityResponse> {
    return this.http.post<CourtAvailabilityResponse>(`${this.apiUrl}/managed/${ownerId}/availability`, request);
  }

  updateAvailability(
    ownerId: number,
    availabilityId: number,
    request: SaveCourtAvailabilityRequest
  ): Observable<CourtAvailabilityResponse> {
    return this.http.put<CourtAvailabilityResponse>(`${this.apiUrl}/managed/${ownerId}/availability/${availabilityId}`, request);
  }

  deleteAvailability(ownerId: number, availabilityId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/managed/${ownerId}/availability/${availabilityId}`);
  }

  getAvailableSchedules(id: number, date: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/${id}/available-schedules`, {
      params: new HttpParams().set('date', date)
    });
  }

  getManagedReport(ownerId: number, from: string, to: string, courtId?: number): Observable<CourtReportResponse> {
    let params = new HttpParams().set('from', from).set('to', to);
    if (courtId) params = params.set('courtId', courtId);
    return this.http.get<CourtReportResponse>(`${this.apiUrl}/managed/${ownerId}/reports`, { params });
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

  reserveCourt(
    id: number,
    userId: number,
    date: string,
    startTime: string,
    endTime: string,
    paymentMethod: string,
    culqiToken: string,
    payerEmail: string,
    idempotencyKey: string
  ): Observable<ReservationResponse> {
    return this.http.post<ReservationResponse>(`${this.apiUrl}/${id}/reservations`, {
      userId,
      date,
      startTime,
      endTime,
      paymentMethod,
      culqiToken,
      payerEmail,
      idempotencyKey
    });
  }
}
