import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CommentResponse, IncidentResponse, Priority, Status } from '../../models';
import { IncidentCreateRequest } from '../../models/request/incident-create-request';
import { IncidentUpdateRequest } from '../../models/request/incident-update-request';
import { IncidentService } from '../../services/incident.service';
import { CommentService } from '../../services/comment.service';

@Component({
  selector: 'app-incident-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './incident-modal.component.html',
  styleUrl: './incident-modal.component.scss'
})
export class IncidentModalComponent implements OnChanges {
  private readonly formBuilder = inject(FormBuilder);
  private readonly incidentService = inject(IncidentService);
  private readonly commentService = inject(CommentService);

  @Output() closed = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();
  @Input() incident: IncidentResponse | null = null;
  @Input() mode: 'create' | 'edit' | 'view' = 'create';

  protected readonly priorities: Priority[] = ['BAIXA', 'MEDIA', 'ALTA'];
  protected readonly statuses: Status[] = ['ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'CANCELADA'];
  protected readonly incidentForm = this.formBuilder.nonNullable.group({
    titulo: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(120)]],
    descricao: ['', Validators.maxLength(5000)],
    prioridade: this.formBuilder.nonNullable.control<Priority>('MEDIA', Validators.required),
    status: this.formBuilder.nonNullable.control<Status>('ABERTA', Validators.required),
    responsavelEmail: ['', [Validators.required, Validators.email]],
    tags: ['']
  });

  protected isSubmitting = signal(false);
  protected errorMessage = signal('');
  protected comments = signal<CommentResponse[]>([]);
  protected isLoadingComments = signal(false);
  protected isSubmittingComment = signal(false);
  protected commentErrorMessage = signal('');
  protected readonly commentForm = this.formBuilder.nonNullable.group({
    mensagem: ['', [Validators.required, Validators.maxLength(5000)]]
  });

  protected get isEditMode(): boolean {
    return this.mode === 'edit';
  }

  protected get isViewMode(): boolean {
    return this.mode === 'view';
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['incident'] || changes['mode']) {
      this.setFormState();

      if (this.incident) {
        this.incidentForm.patchValue({
          titulo: this.incident.titulo,
          descricao: this.incident.descricao ?? '',
          prioridade: this.incident.prioridade,
          status: this.incident.status,
          responsavelEmail: this.incident.responsavelEmail,
          tags: this.incident.tags.join(', ')
        });
      } else {
        this.incidentForm.reset({
          titulo: '',
          descricao: '',
          prioridade: 'MEDIA',
          status: 'ABERTA',
          responsavelEmail: '',
          tags: ''
        });
      }

      if (this.isViewMode && this.incident) {
        this.loadComments();
      } else {
        this.comments.set([]);
      }
    }
  }

  private loadComments(): void {
    if (!this.incident) {
      return;
    }

    this.isLoadingComments.set(true);
    this.commentErrorMessage.set('');
    this.commentService.getByIncident(this.incident.id).subscribe({
      next: (comments) => {
        this.comments.set(comments);
        this.isLoadingComments.set(false);
      },
      error: (error) => {
        this.isLoadingComments.set(false);
        this.commentErrorMessage.set('Não foi possível carregar os comentários.');
        console.error('Erro ao carregar comentários:', error);
      }
    });
  }

  protected submitComment(): void {
    if (!this.incident || this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }

    this.isSubmittingComment.set(true);
    this.commentErrorMessage.set('');
    const request = {
      mensagem: this.commentForm.controls.mensagem.value.trim()
    };

    if (!request.mensagem) {
      this.commentForm.markAllAsTouched();
      this.isSubmittingComment.set(false);
      return;
    }

    this.commentService.create(this.incident.id, request).subscribe({
      next: (comment) => {
        this.comments.update(current => [...current, comment]);
        this.commentForm.reset({ mensagem: '' });
        this.isSubmittingComment.set(false);
      },
      error: (error) => {
        this.isSubmittingComment.set(false);
        this.commentErrorMessage.set('Não foi possível adicionar o comentário.');
        console.error('Erro ao criar comentário:', error);
      }
    });
  }

  private setFormState(): void {
    const controls = this.incidentForm.controls;

    if (this.isViewMode) {
      controls.titulo.disable();
      controls.descricao.disable();
      controls.prioridade.disable();
      controls.responsavelEmail.disable();
      controls.tags.disable();
      controls.status.enable();
      return;
    }

    controls.titulo.enable();
    controls.descricao.enable();
    controls.prioridade.enable();
    controls.status.enable();
    controls.responsavelEmail.enable();
    controls.tags.enable();
  }

  protected close(): void {
    if (!this.isSubmitting()) {
      this.closed.emit();
    }
  }

  protected submit(): void {
    this.errorMessage.set('');

    if (this.incidentForm.invalid) {
      this.incidentForm.markAllAsTouched();
      return;
    }

    const values = this.incidentForm.getRawValue();
    this.isSubmitting.set(true);
    const tags = values.tags.split(',').map(tag => tag.trim()).filter(Boolean);
    if (this.isViewMode && this.incident) {
      this.isSubmitting.set(true);
      this.incidentService.updateStatus(this.incident.id, { status: values.status }).subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.saved.emit();
        },
        error: () => {
          this.isSubmitting.set(false);
          this.errorMessage.set('Não foi possível atualizar o status do incident.');
        }
      });
      return;
    }

    const commonFields = {
      titulo: values.titulo.trim(),
      descricao: values.descricao.trim() || undefined,
      prioridade: values.prioridade,
      responsavelEmail: values.responsavelEmail.trim(),
      tags
    };
    const request: IncidentCreateRequest | IncidentUpdateRequest = this.isEditMode
      ? { ...commonFields, status: values.status }
      : commonFields;
    const request$ = this.incident
      ? this.incidentService.update(this.incident.id, request as IncidentUpdateRequest)
      : this.incidentService.create(request as IncidentCreateRequest);

    request$.subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.saved.emit();
      },
      error: () => {
        this.isSubmitting.set(false);
        this.errorMessage.set(this.isEditMode
          ? 'Não foi possível atualizar o incident.'
          : 'Não foi possível cadastrar o incident.');
      }
    });
  }

  protected updateStatus(): void {
    if (this.isViewMode) {
      this.submit();
    }
  }
}
