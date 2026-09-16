import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { StatsResponse } from '../models';
import { API_BASE_URL } from '../core/config/api.config';

@Injectable({ providedIn: 'root' })
export class StatsService {
  private readonly resourceUrl = `${API_BASE_URL}/stats/incidents`;

  constructor(private readonly http: HttpClient) {}

  getIncidentStats(): Observable<StatsResponse> {
    return this.http.get<StatsResponse>(this.resourceUrl);
  }
}
