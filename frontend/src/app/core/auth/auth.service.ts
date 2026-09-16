import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { Login } from '../../models/auth/login';
import { Role } from '../../models/enums/role';
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

  isAuthenticated(): boolean {
    const token = this.getToken();

    if (!token) {
      return false;
    }

    try {
      const payload = this.getPayload(token) as { exp?: number };
      return !payload.exp || payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  getRole(): Role | null {
    const token = this.getToken();

    if (!token) {
      return null;
    }

    try {
      const payload = this.getPayload(token) as { role?: Role };
      return payload.role ?? null;
    } catch {
      return null;
    }
  }

  hasWritePermission(): boolean {
    return this.isAuthenticated() && this.getRole() === 'WRITER';
  }

  private getPayload(token: string): Record<string, unknown> {
    const encodedPayload = token.split('.')[1]
      .replace(/-/g, '+')
      .replace(/_/g, '/');
    return JSON.parse(atob(encodedPayload));
  }

}
