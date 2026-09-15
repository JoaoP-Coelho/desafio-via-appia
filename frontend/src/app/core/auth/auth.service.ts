import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { Login } from '../../models/auth/login';
import { API_BASE_URL } from '../config/api.config';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'incident-manager-token';

  constructor(private readonly http: HttpClient) {}

  login(credentials: Login): Observable<string> {
    return this.http
      .post(`${API_BASE_URL}/api/login`, credentials, { responseType: 'text' })
      .pipe(tap((token) => localStorage.setItem(this.tokenKey, token)));
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

}
