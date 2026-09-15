import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { finalize } from 'rxjs';

import { IncidentResponse } from '../../models';
import { IncidentService } from '../../services/incident.service';

@Component({
  selector: 'app-incidents',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './incidents.component.html',
  styleUrl: './incidents.component.scss'
})
export class IncidentsComponent implements OnInit {

  private readonly incidentService = inject(IncidentService);

  protected incidents = signal<IncidentResponse[]>([]);
  protected currentPage = signal(0);
  protected totalPages = signal(0);
  protected totalElements = signal(0);
  protected isLoading = signal(false);
  protected errorMessage = signal('');

  private readonly pageSize = 10;

  ngOnInit(): void {
    this.loadIncidents();
  }

  protected loadIncidents(): void {

    this.isLoading.set(true);
    this.errorMessage.set('');

    this.incidentService.search({
      page: this.currentPage(),
      size: this.pageSize,
      sort: 'dataAbertura,desc'
    }).pipe(
      finalize(() => this.isLoading.set(false))
    ).subscribe({
      next: (page) => {
        this.incidents.set(
          Array.isArray(page.content) ? page.content : []
        );

        this.currentPage.set(page.number);
        this.totalPages.set(page.totalPages);
        this.totalElements.set(page.totalElements);
      },
      error: (error) => {
        this.errorMessage.set(
          'Não foi possível carregar os incidents.'
        );

        console.error('Erro ao carregar incidents:', error);
      }
    });
  }

  protected previousPage(): void {
    if (this.currentPage() > 0) {
      this.currentPage.update(page => page - 1);
      this.loadIncidents();
    }
  }

  protected nextPage(): void {
    if (this.currentPage() < this.totalPages() - 1) {
      this.currentPage.update(page => page + 1);
      this.loadIncidents();
    }
  }
}
