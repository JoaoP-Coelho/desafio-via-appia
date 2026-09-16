import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CommentCreateRequest, CommentResponse } from '../models';
import { API_BASE_URL } from '../core/config/api.config';

@Injectable({ providedIn: 'root' })
export class CommentService {
  private readonly incidentsUrl = `${API_BASE_URL}/api/incidents`;

  constructor(private readonly http: HttpClient) {}

  create(incidentId: string, request: CommentCreateRequest): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(
      `${this.incidentsUrl}/${incidentId}/comments`,
      request
    );
  }

  getByIncident(incidentId: string): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(
      `${this.incidentsUrl}/${incidentId}/comments`
    );
  }
}
