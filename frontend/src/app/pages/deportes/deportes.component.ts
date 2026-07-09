import { Component, OnInit, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { MatchService, MatchResponse, CreateMatchRequest } from '../../shared/services/match.service';
import { AuthService } from '../../shared/services/auth.service';
import { MapsLoaderService } from '../../shared/services/maps-loader.service';

@Component({
  selector: 'app-deportes',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './deportes.component.html',
  styleUrl: './deportes.component.scss'
})
export class DeportesComponent implements OnInit {
  @ViewChild('addressInput') addressInput?: ElementRef<HTMLInputElement>;

  matches: MatchResponse[] = [];
  sportFilter = 'Fútbol';
  showCreateForm = false;
  errorMessage = '';
  private addressAutocomplete: any = null;

  newMatch: CreateMatchRequest = {
    organizerId: 0,
    sport: '',
    title: '',
    description: null,
    address: '',
    matchDate: '',
    totalSlots: 10,
    price: null
  };

  constructor(
    private matchService: MatchService,
    private authService: AuthService,
    private mapsLoader: MapsLoaderService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const sport = params['sport'];
      if (sport) {
        this.sportFilter = sport;
      }
      this.loadMatches();
    });
  }

  loadMatches(): void {
    const sport = this.sportFilter || undefined;
    this.matchService.getOpenMatches(sport).subscribe({
      next: (data) => { this.matches = data; this.cdr.detectChanges(); },
      error: () => { this.errorMessage = 'Error al cargar partidos.'; this.cdr.detectChanges(); }
    });
  }

  onFilterChange(): void {
    this.loadMatches();
  }

  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
    if (this.showCreateForm) {
      this.newMatch.organizerId = this.authService.getUserId() || 0;
      this.newMatch.sport = this.sportFilter;
      setTimeout(() => this.initAddressAutocomplete(), 0);
    } else {
      this.addressAutocomplete = null;
    }
  }

  private async initAddressAutocomplete(): Promise<void> {
    if (!this.addressInput || this.addressAutocomplete) return;
    try {
      await this.mapsLoader.load();
      const google = window.google;
      this.addressAutocomplete = new google.maps.places.Autocomplete(this.addressInput.nativeElement, {
        componentRestrictions: { country: 'pe' },
        fields: ['formatted_address']
      });
      this.addressAutocomplete.addListener('place_changed', () => {
        const place = this.addressAutocomplete.getPlace();
        if (place?.formatted_address) {
          this.newMatch.address = place.formatted_address;
          this.cdr.detectChanges();
        }
      });
    } catch {
      // autocompletado no disponible; el campo sigue funcionando como texto libre
    }
  }

  createMatch(): void {
    this.errorMessage = '';
    this.matchService.createMatch(this.newMatch).subscribe({
      next: () => {
        this.showCreateForm = false;
        this.addressAutocomplete = null;
        this.resetNewMatch();
        this.loadMatches();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Error al crear el partido.';
        this.cdr.detectChanges();
      }
    });
  }

  joinMatch(matchId: number): void {
    const userId = this.authService.getUserId();
    if (!userId) return;
    this.errorMessage = '';
    this.matchService.joinMatch(matchId, userId).subscribe({
      next: () => this.loadMatches(),
      error: (err) => {
        this.errorMessage = err.error?.message || 'No se pudo unir al partido.';
      }
    });
  }

  cancelMatch(matchId: number): void {
    const userId = this.authService.getUserId();
    if (!userId) return;
    this.errorMessage = '';
    this.matchService.cancelMatch(matchId, userId).subscribe({
      next: () => this.loadMatches(),
      error: (err) => {
        this.errorMessage = err.error?.message || 'No se pudo cancelar el partido.';
      }
    });
  }

  get isAdminCancha(): boolean {
    return this.authService.isAdminCancha();
  }

  isOrganizer(match: MatchResponse): boolean {
    return match.organizerId === this.authService.getUserId();
  }

  canCancel(match: MatchResponse): boolean {
    return match.status === 'OPEN' && new Date(match.matchDate) > new Date();
  }

  hasJoined(match: MatchResponse): boolean {
    const userId = this.authService.getUserId();
    return userId !== null && match.participantIds.includes(userId);
  }

  formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('es-PE', {
      weekday: 'long',
      day: 'numeric',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  private resetNewMatch(): void {
    this.newMatch = {
      organizerId: this.authService.getUserId() || 0,
      sport: this.sportFilter,
      title: '',
      description: null,
      address: '',
      matchDate: '',
      totalSlots: 10,
      price: null
    };
  }
}
