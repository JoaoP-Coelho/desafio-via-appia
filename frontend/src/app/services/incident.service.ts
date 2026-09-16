import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  IncidentCreateRequest,
  IncidentResponse,
  IncidentStatusUpdateRequest,
  IncidentUpdateRequest,
  PageIncidentResponse,
  Priority,
  Status
} from '../models';
import { API_BASE_URL } from '../core/config/api.config';

export interface IncidentSearchParams {
  status?: Status;
  prioridade?: Priority;
  q?: string;
  page?: number;
  size?: number;
  sort?: string;
}

@Injectable({ providedIn: 'root' })
export class IncidentService {
  private readonly resourceUrl = `${API_BASE_URL}/api/incidents`;

  constructor(private readonly http: HttpClient) {}

  create(request: IncidentCreateRequest): Observable<IncidentResponse> {
    return this.http.post<IncidentResponse>(this.resourceUrl, request);
  }

  search(filters: IncidentSearchParams = {}): Observable<PageIncidentResponse> {
    let params = new HttpParams();

    if (filters.status) params = params.set('status', filters.status);
    if (filters.prioridade) params = params.set('prioridade', filters.prioridade);
    if (filters.q) params = params.set('q', filters.q);
    if (filters.page !== undefined) params = params.set('page', filters.page);
    if (filters.size !== undefined) params = params.set('size', filters.size);
    if (filters.sort) params = params.set('sort', filters.sort);

    return this.http.get<PageIncidentResponse>(this.resourceUrl, { params });
  }

  getById(id: string): Observable<IncidentResponse> {
    return this.http.get<IncidentResponse>(`${this.resourceUrl}/${id}`);
  }

  update(id: string, request: IncidentUpdateRequest): Observable<IncidentResponse> {
    return this.http.put<IncidentResponse>(`${this.resourceUrl}/${id}`, request);
  }

  updateStatus(id: string, request: IncidentStatusUpdateRequest): Observable<IncidentResponse> {
    return this.http.patch<IncidentResponse>(`${this.resourceUrl}/${id}/status`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
