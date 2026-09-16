import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';

import { Priority, StatsResponse, Status } from '../../models';
import { StatsService } from '../../services/stats.service';

@Component({
  selector: 'app-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.scss'
})
export class StatsComponent implements OnInit {
  private readonly statsService = inject(StatsService);

  protected readonly stats = signal<StatsResponse | null>(null);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal('');

  protected readonly statuses: Status[] = [
    'ABERTA',
    'EM_ANDAMENTO',
    'RESOLVIDA',
    'CANCELADA'
  ];
  protected readonly priorities: Priority[] = ['BAIXA', 'MEDIA', 'ALTA'];
  protected readonly totalIncidents = computed(() => {
    const values = this.stats()?.porStatus;
    return values ? Object.values(values).reduce((total, value) => total + value, 0) : 0;
  });

  ngOnInit(): void {
    this.loadStats();
  }

  protected loadStats(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.statsService.getIncidentStats().subscribe({
      next: (stats) => {
        this.stats.set(stats);
        this.isLoading.set(false);
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set('Não foi possível carregar as estatísticas.');
        console.error('Erro ao carregar estatísticas:', error);
      }
    });
  }

  protected statusLabel(status: Status): string {
    return {
      ABERTA: 'Aberta',
      EM_ANDAMENTO: 'Em andamento',
      RESOLVIDA: 'Resolvida',
      CANCELADA: 'Cancelada'
    }[status];
  }

  protected priorityLabel(priority: Priority): string {
    return {
      BAIXA: 'Baixa',
      MEDIA: 'Média',
      ALTA: 'Alta'
    }[priority];
  }

  protected statusValue(status: Status): number {
    return this.stats()?.porStatus[status] ?? 0;
  }

  protected priorityValue(priority: Priority): number {
    return this.stats()?.porPrioridade[priority] ?? 0;
  }
}
