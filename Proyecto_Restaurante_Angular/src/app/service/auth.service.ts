import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { catchError, Observable, switchMap, tap, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private perfil?: { apiUrl: string; token: string; datos: unknown };

  consultarPerfil<T>(apiUrl: string): Observable<T> {
    const token = localStorage.getItem('token');
    return this.http.get<T>(`${apiUrl}/details`).pipe(
      tap({
        next: datos => {
          if (token && token === localStorage.getItem('token')) {
            this.perfil = { apiUrl, token, datos };
          }
        },
        error: () => {
          if (this.perfil?.apiUrl === apiUrl && this.perfil.token === token) {
            this.perfil = undefined;
          }
        }
      })
    );
  }

  perfilEnSesion<T>(apiUrl: string): T | undefined {
    const perfil = this.perfil;
    return perfil?.apiUrl === apiUrl && perfil.token === localStorage.getItem('token')
      ? perfil.datos as T
      : undefined;
  }

  iniciarSesion<T>(apiUrl: string, credenciales: object): Observable<T> {
    return this.http.post(`${apiUrl}/login`, credenciales, { responseType: 'text' }).pipe(
      catchError((error: HttpErrorResponse) => {
        if (typeof error.error === 'string' && error.headers.get('Content-Type')?.includes('application/json')) {
          return throwError(() => new HttpErrorResponse({
            error: JSON.parse(error.error),
            headers: error.headers,
            status: error.status,
            statusText: error.statusText,
            url: error.url ?? undefined
          }));
        }
        return throwError(() => error);
      }),
      switchMap(token => {
        localStorage.setItem('token', token);
        return this.consultarPerfil<T>(apiUrl);
      })
    );
  }
}
