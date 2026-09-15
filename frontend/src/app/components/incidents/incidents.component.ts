import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { finalize } from 'rxjs';

import { IncidentResponse, Priority, Status } from '../../models';
import {
  IncidentSearchParams,
  IncidentService
} from '../../services/incident.service';

@Component({
  selector: 'app-incidents',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './incidents.component.html',
  styleUrl: './incidents.component.scss'
})
export class IncidentsComponent implements OnInit {

  private readonly formBuilder = inject(FormBuilder);
  private readonly incidentService = inject(IncidentService);

  protected incidents = signal<IncidentResponse[]>([]);
  protected currentPage = signal(0);
  protected totalPages = signal(0);
  protected totalElements = signal(0);
  protected isLoading = signal(false);
  protected errorMessage = signal('');

  protected readonly filterForm = this.formBuilder.nonNullable.group({
    q: '',
    status: this.formBuilder.control<Status | ''>(''),
    prioridade: this.formBuilder.control<Priority | ''>(''),
    size: 10
  });

  ngOnInit(): void {
    this.loadIncidents();
  }

  protected loadIncidents(): void {

    this.isLoading.set(true);
    this.errorMessage.set('');

    const values = this.filterForm.getRawValue();
    const filters: IncidentSearchParams = {
      page: this.currentPage(),
      size: values.size,
      sort: 'dataAbertura,desc',
      q: values.q || undefined,
      status: values.status || undefined,
      prioridade: values.prioridade || undefined
    };

    this.incidentService.search(filters).pipe(
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

  protected applyFilters(): void {
    this.currentPage.set(0);
    this.loadIncidents();
  }

  protected clearFilters(): void {
    this.filterForm.reset({
      q: '',
      status: '',
      prioridade: '',
      size: 10
    });
    this.applyFilters();
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
