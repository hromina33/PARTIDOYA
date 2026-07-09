import { Component, OnInit, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatchService, MatchResponse } from '../../shared/services/match.service';
import { AuthService } from '../../shared/services/auth.service';
import { MapsLoaderService } from '../../shared/services/maps-loader.service';

@Component({
  selector: 'app-match-detail',
  imports: [CommonModule, RouterLink],
  templateUrl: './match-detail.component.html',
  styleUrl: './match-detail.component.scss'
})
export class MatchDetailComponent implements OnInit {
  @ViewChild('mapContainer') mapContainer?: ElementRef<HTMLDivElement>;

  match: MatchResponse | null = null;
  loading = true;
  errorMessage = '';

  mapLoading = false;
  mapError = '';
  routeDuration = '';
  routeDistance = '';

  constructor(
    private matchService: MatchService,
    private authService: AuthService,
    private mapsLoader: MapsLoaderService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.router.navigate(['/deportes']);
      return;
    }
    this.loadMatch(id);
  }

  private loadMatch(id: number): void {
    this.matchService.getMatchById(id).subscribe({
      next: (data) => {
        this.match = data;
        this.loading = false;
        this.mapLoading = true;
        this.cdr.detectChanges();
        setTimeout(() => this.initMap(), 0);
      },
      error: () => {
        this.errorMessage = 'Partido no encontrado.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get isOrganizer(): boolean {
    return this.match?.organizerId === this.authService.getUserId();
  }

  get hasJoined(): boolean {
    const userId = this.authService.getUserId();
    return userId !== null && (this.match?.participantIds.includes(userId) ?? false);
  }

  get filledPercent(): number {
    if (!this.match) return 0;
    return ((this.match.totalSlots - this.match.availableSlots) / this.match.totalSlots) * 100;
  }

  get currentUserId(): number | null {
    return this.authService.getUserId();
  }

  get canCancel(): boolean {
    return !!this.match && this.match.status === 'OPEN' && new Date(this.match.matchDate) > new Date();
  }

  get googleMapsUrl(): string {
    if (!this.match) return '#';
    return `https://www.google.com/maps/dir/?api=1&destination=${encodeURIComponent(this.match.address)}`;
  }

  private async initMap(): Promise<void> {
    try {
      await this.mapsLoader.load();
      const position = await this.getUserPosition();
      this.renderDirections(position);
    } catch (err: any) {
      this.mapLoading = false;
      this.mapError = err?.message || 'No se pudo cargar el mapa.';
      this.cdr.detectChanges();
    }
  }

  private getUserPosition(): Promise<GeolocationPosition> {
    return new Promise((resolve, reject) => {
      if (!navigator.geolocation) {
        reject(new Error('Tu navegador no soporta geolocalización.'));
        return;
      }
      navigator.geolocation.getCurrentPosition(
        resolve,
        () => reject(new Error('Activa el permiso de ubicación para ver la ruta.')),
        { timeout: 8000 }
      );
    });
  }

  private renderDirections(position: GeolocationPosition): void {
    if (!this.match || !this.mapContainer) {
      this.mapLoading = false;
      return;
    }
    const google = window.google;
    const origin = { lat: position.coords.latitude, lng: position.coords.longitude };

    const map = new google.maps.Map(this.mapContainer.nativeElement, {
      center: origin,
      zoom: 13,
      disableDefaultUI: true,
      zoomControl: true,
      styles: DARK_MAP_STYLE
    });

    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer({
      map,
      suppressMarkers: false,
      polylineOptions: { strokeColor: '#c6f135', strokeWeight: 5 }
    });

    directionsService.route(
      {
        origin,
        destination: this.match.address,
        travelMode: google.maps.TravelMode.DRIVING
      },
      (result: any, status: string) => {
        this.mapLoading = false;
        if (status === 'OK') {
          directionsRenderer.setDirections(result);
          const leg = result.routes[0].legs[0];
          this.routeDuration = leg.duration.text;
          this.routeDistance = leg.distance.text;
        } else {
          this.mapError = 'No se pudo calcular la ruta a la cancha.';
        }
        this.cdr.detectChanges();
      }
    );
  }

  joinMatch(): void {
    if (!this.match || !this.currentUserId) return;
    this.matchService.joinMatch(this.match.id, this.currentUserId).subscribe({
      next: (data) => {
        this.match = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'No se pudo unir al partido.';
        this.cdr.detectChanges();
      }
    });
  }

  leaveMatch(): void {
    if (!this.match || !this.currentUserId) return;
    this.matchService.leaveMatch(this.match.id, this.currentUserId).subscribe({
      next: (data) => {
        this.match = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'No se pudo abandonar el partido.';
        this.cdr.detectChanges();
      }
    });
  }

  cancelMatch(): void {
    if (!this.match || !this.currentUserId) return;
    this.matchService.cancelMatch(this.match.id, this.currentUserId).subscribe({
      next: (data) => {
        this.match = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'No se pudo cancelar el partido.';
        this.cdr.detectChanges();
      }
    });
  }

  formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('es-PE', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      OPEN: 'Abierto',
      FULL: 'Completo',
      CANCELLED: 'Cancelado',
      FINISHED: 'Finalizado'
    };
    return labels[status] || status;
  }
}

const DARK_MAP_STYLE = [
  { elementType: 'geometry', stylers: [{ color: '#0c1120' }] },
  { elementType: 'labels.text.fill', stylers: [{ color: 'rgba(255,255,255,0.55)' }] },
  { elementType: 'labels.text.stroke', stylers: [{ color: '#0c1120' }] },
  { featureType: 'road', elementType: 'geometry', stylers: [{ color: '#2a3350' }] },
  { featureType: 'road.arterial', elementType: 'geometry', stylers: [{ color: '#2a3350' }] },
  { featureType: 'road.highway', elementType: 'geometry', stylers: [{ color: '#39456b' }] },
  { featureType: 'poi', elementType: 'geometry', stylers: [{ color: '#141a2c' }] },
  { featureType: 'poi', elementType: 'labels', stylers: [{ visibility: 'off' }] },
  { featureType: 'water', elementType: 'geometry', stylers: [{ color: '#0a0f1a' }] },
  { featureType: 'transit', stylers: [{ visibility: 'off' }] },
  { featureType: 'administrative', elementType: 'geometry', stylers: [{ color: 'rgba(255,255,255,0.08)' }] }
];
