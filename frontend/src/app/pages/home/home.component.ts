import { Component, OnInit, OnDestroy, NgZone, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatchService, MatchResponse } from '../../shared/services/match.service';
import { AuthService } from '../../shared/services/auth.service';
import { GeolocationService } from '../../shared/services/geolocation.service';

const NEARBY_RADIUS_KM = 50;

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {
  userName: string | null = null;
  upcomingMatches: MatchResponse[] = [];
  almostFullMatches: MatchResponse[] = [];
  nearbyMatches: MatchResponse[] = [];
  joinedMatches: MatchResponse[] = [];

  banners = [
    { title: 'Pichanga Relámpago', subtitle: 'Fútbol 7 este sábado a las 4pm — ¡últimos 3 cupos!', cta: 'Ver partido', theme: 'banner-green' },
    { title: 'Torneo Express', subtitle: 'Arma tu equipo y compite en eliminación directa este fin de semana.', cta: 'Inscribirse', theme: 'banner-orange' },
    { title: 'Nuevas canchas disponibles', subtitle: 'Se sumaron 5 canchas sintéticas en Lima Norte. Reserva ahora.', cta: 'Explorar', theme: 'banner-purple' },
    { title: 'Invita y gana', subtitle: 'Invita a un amigo y ambos reciben 1 mes de Jugador Plus gratis.', cta: 'Invitar amigos', theme: 'banner-blue' },
  ];
  currentBanner = 0;
  private bannerTimer: ReturnType<typeof setInterval> | null = null;

  tickerItems = [
    'Pichanga de Fútbol en 30 min',
    'Carlos se unió a un partido',
    '3 cupos disponibles en Voleibol',
    'Nueva cancha en Miraflores',
    'Andrea creó un partido de Tenis',
    '2 partidos de Básquet hoy',
    'Miguel completó su racha de 5',
  ];

  constructor(
    private matchService: MatchService,
    private authService: AuthService,
    private geolocation: GeolocationService,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.userName = this.authService.getUserName();
    this.matchService.getOpenMatches().subscribe({
      next: (matches) => {
        const sorted = [...matches].sort((a, b) =>
          new Date(a.matchDate).getTime() - new Date(b.matchDate).getTime()
        );
        this.upcomingMatches = sorted.slice(0, 4);
        this.almostFullMatches = matches
          .filter(m => m.availableSlots > 0 && m.availableSlots <= 3)
          .slice(0, 4);
        this.cdr.detectChanges();
        this.loadNearbyMatches(sorted);
      }
    });
    this.loadJoinedMatches();
    this.startAutoplay();
  }

  private loadJoinedMatches(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;
    this.matchService.getMatchesByParticipant(userId).subscribe({
      next: (matches) => {
        this.joinedMatches = [...matches]
          .sort((a, b) => new Date(a.matchDate).getTime() - new Date(b.matchDate).getTime())
          .slice(0, 6);
        this.cdr.detectChanges();
      }
    });
  }

  private async loadNearbyMatches(matches: MatchResponse[]): Promise<void> {
    try {
      const position = await this.geolocation.getPosition();
      const { latitude, longitude } = position.coords;
      this.nearbyMatches = matches
        .filter(m => m.latitude !== null && m.longitude !== null)
        .filter(m => this.geolocation.distanceKm(latitude, longitude, m.latitude!, m.longitude!) <= NEARBY_RADIUS_KM)
        .slice(0, 6);
      this.cdr.detectChanges();
    } catch {
      // ubicación no disponible: la sección "Cerca de ti" simplemente no se muestra
    }
  }

  ngOnDestroy(): void {
    this.stopAutoplay();
  }

  startAutoplay(): void {
    this.stopAutoplay();
    this.bannerTimer = setInterval(() => {
      this.ngZone.run(() => {
        this.nextBanner();
      });
    }, 1500);
  }

  stopAutoplay(): void {
    if (this.bannerTimer) {
      clearInterval(this.bannerTimer);
      this.bannerTimer = null;
    }
  }

  nextBanner(): void {
    this.currentBanner = (this.currentBanner + 1) % this.banners.length;
  }

  prevBanner(): void {
    this.currentBanner = (this.currentBanner - 1 + this.banners.length) % this.banners.length;
  }

  goToBanner(index: number): void {
    this.currentBanner = index;
    this.startAutoplay();
  }

  joinMatch(matchId: number): void {
    const userId = this.authService.getUserId();
    if (!userId) return;
    this.matchService.joinMatch(matchId, userId).subscribe({
      next: () => this.ngOnInit()
    });
  }

  hasJoined(match: MatchResponse): boolean {
    const userId = this.authService.getUserId();
    return userId !== null && match.participantIds.includes(userId);
  }

  formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('es-PE', {
      weekday: 'short',
      day: 'numeric',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getFilledPercent(match: MatchResponse): number {
    return ((match.totalSlots - match.availableSlots) / match.totalSlots) * 100;
  }
}
