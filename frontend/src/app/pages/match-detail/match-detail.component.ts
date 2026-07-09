import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatchService, MatchResponse } from '../../shared/services/match.service';
import { AuthService } from '../../shared/services/auth.service';

@Component({
  selector: 'app-match-detail',
  imports: [CommonModule, RouterLink],
  templateUrl: './match-detail.component.html',
  styleUrl: './match-detail.component.scss'
})
export class MatchDetailComponent implements OnInit {
  match: MatchResponse | null = null;
  loading = true;
  errorMessage = '';

  constructor(
    private matchService: MatchService,
    private authService: AuthService,
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
        this.cdr.detectChanges();
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
