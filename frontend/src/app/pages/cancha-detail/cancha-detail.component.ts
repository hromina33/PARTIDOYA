import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { CourtResponse, CourtService, ReservationResponse } from '../../shared/services/court.service';
import { MapsLoaderService } from '../../shared/services/maps-loader.service';
import { PaymentService } from '../../shared/services/payment.service';

@Component({
  selector: 'app-cancha-detail',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cancha-detail.component.html',
  styleUrl: './cancha-detail.component.scss'
})
export class CanchaDetailComponent implements OnInit {
  @ViewChild('mapContainer') mapContainer?: ElementRef<HTMLDivElement>;

  court: CourtResponse | null = null;
  loading = true;
  errorMessage = '';
  successMessage = '';
  mapError = '';

  selectedDate = '';
  selectedSchedule = '';
  paymentMethod = 'Culqi Sandbox';
  reserving = false;
  reservation: ReservationResponse | null = null;

  constructor(
    private courtService: CourtService,
    private authService: AuthService,
    private paymentService: PaymentService,
    private mapsLoader: MapsLoaderService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.router.navigate(['/canchas']);
      return;
    }
    this.loadCourt(id);
  }

  get canReserve(): boolean {
    return !!this.selectedDate && !!this.selectedSchedule && !this.reserving;
  }

  get primaryImage(): string | null {
    return this.court?.mainImageUrl || this.court?.imageUrls?.[0] || null;
  }

  get primarySport(): string {
    return this.court?.sports?.[0] || 'Fútbol';
  }

  get selectedStartTime(): string {
    return this.selectedSchedule.split('-')[0] || '';
  }

  get reservationMatchDate(): string {
    return this.selectedDate && this.selectedStartTime ? `${this.selectedDate}T${this.selectedStartTime}` : '';
  }

  get reservationTitle(): string {
    return this.court ? `Pichanga en ${this.court.name}` : '';
  }

  get totalToPay(): number {
    return this.court?.pricePerHour || 0;
  }

  private loadCourt(id: number): void {
    this.courtService.getCourtById(id).subscribe({
      next: (court) => {
        this.court = court;
        this.loading = false;
        setTimeout(() => this.initMap(), 0);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Cancha no encontrada.';
        this.loading = false;
      }
    });
  }

  async reserve(): Promise<void> {
    if (!this.court || !this.canReserve) return;
    const userId = this.authService.getUserId();
    const payerEmail = this.authService.getUserEmail();
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }
    if (!payerEmail) {
      this.errorMessage = 'Vuelve a iniciar sesion para procesar el pago.';
      return;
    }
    const [startTime, endTime] = this.selectedSchedule.split('-');
    this.reserving = true;
    this.errorMessage = '';
    this.successMessage = '';
    let culqiToken = '';
    try {
      culqiToken = await this.paymentService.openCulqiCheckout(this.totalToPay, `Reserva ${this.court.name}`);
    } catch (err: any) {
      this.errorMessage = err?.message || 'No se pudo procesar el pago.';
      this.reserving = false;
      return;
    }
    const idempotencyKey = `reservation-${this.court.id}-${userId}-${this.selectedDate}-${startTime}-${Date.now()}`;
    this.courtService.reserveCourt(this.court.id, userId, this.selectedDate, startTime, endTime, this.paymentMethod,
      culqiToken, payerEmail, idempotencyKey).subscribe({
      next: (reservation) => {
        this.reservation = reservation;
        this.successMessage = 'Pago realizado correctamente. Reserva confirmada.';
        this.reserving = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'No se pudo procesar el pago.';
        this.reserving = false;
      }
    });
  }

  private async initMap(): Promise<void> {
    if (!this.court || !this.mapContainer) return;
    if (this.court.latitude === null || this.court.longitude === null) {
      this.mapError = 'Esta cancha aún no tiene coordenadas válidas.';
      return;
    }
    try {
      await this.mapsLoader.load();
      const google = window.google;
      const center = { lat: this.court.latitude, lng: this.court.longitude };
      const map = new google.maps.Map(this.mapContainer.nativeElement, {
        center,
        zoom: 15,
        disableDefaultUI: true,
        zoomControl: true
      });
      new google.maps.Marker({ position: center, map, title: this.court.name });
    } catch {
      this.mapError = 'No se pudo cargar el mapa.';
    }
  }
}
