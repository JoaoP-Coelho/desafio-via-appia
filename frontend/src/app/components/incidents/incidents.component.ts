import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import Swal from 'sweetalert2';

import { IncidentResponse, Priority, Status } from '../../models';
import {
  IncidentSearchParams,
  IncidentService
} from '../../services/incident.service';
import { IncidentModalComponent } from '../incident-modal/incident-modal.component';

@Component({
  selector: 'app-incidents',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IncidentModalComponent],
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
  protected isModalOpen = signal(false);
  protected selectedIncident = signal<IncidentResponse | null>(null);
  protected deletingIncidentId = signal<string | null>(null);

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

  protected openIncidentModal(): void {
    this.selectedIncident.set(null);
    this.isModalOpen.set(true);
  }

  protected editIncident(incident: IncidentResponse): void {
    this.selectedIncident.set(incident);
    this.isModalOpen.set(true);
  }

  protected closeIncidentModal(): void {
    this.isModalOpen.set(false);
  }

  protected handleIncidentCreated(): void {
    this.isModalOpen.set(false);
    this.selectedIncident.set(null);
    this.currentPage.set(0);
    this.loadIncidents();
  }

  protected async deleteIncident(incident: IncidentResponse): Promise<void> {
    const result = await Swal.fire({
      title: 'Excluir incident?',
      text: `O incident "${incident.titulo}" será excluído permanentemente.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sim, excluir',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: 'rgb(163 29 33 / 1)',
      cancelButtonColor: 'grey',
    });

    if (!result.isConfirmed) {
      return;
    }

    this.deletingIncidentId.set(incident.id);
    this.errorMessage.set('');

    this.incidentService.delete(incident.id).pipe(
      finalize(() => this.deletingIncidentId.set(null))
    ).subscribe({
      next: () => {
        if (this.incidents().length === 1 && this.currentPage() > 0) {
          this.currentPage.update(page => page - 1);
        }
        void Swal.fire({
          title: 'Excluído!',
          text: 'O incident foi excluído com sucesso.',
          icon: 'success',
          confirmButtonColor: 'rgb(163 29 33 / 1)'
        }).then(() => this.loadIncidents());
      },
      error: (error) => {
        this.errorMessage.set('Não foi possível excluir o incident.');
        console.error('Erro ao excluir incident:', error);
        void Swal.fire({
          title: 'Erro',
          text: 'Não foi possível excluir o incident.',
          icon: 'error',
          confirmButtonColor: 'rgb(163 29 33 / 1)'
        });
      }
    });
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
