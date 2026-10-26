import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Domiciliario } from '../models/domiciliario.model';

@Injectable({
  providedIn: 'root'
})
/** Provee las operaciones CRUD y de disponibilidad de domiciliarios. */
export class DomiciliarioService {
  private readonly apiUrl = 'http://localhost:8080/api/domiciliarios';
  private http = inject(HttpClient);

  getDomiciliarios(): Observable<Domiciliario[]> {
    return this.http.get<Domiciliario[]>(this.apiUrl);
  }

  getDomiciliarioById(id: number): Observable<Domiciliario> {
    return this.http.get<Domiciliario>(`${this.apiUrl}/${id}`);
  }

  addDomiciliario(domiciliario: Omit<Domiciliario, 'idDomiciliario'>): Observable<Domiciliario> {
    return this.http.post<Domiciliario>(this.apiUrl, domiciliario);
  }

  updateDomiciliario(id: number, domiciliario: Domiciliario): Observable<Domiciliario> {
    return this.http.put<Domiciliario>(`${this.apiUrl}/${id}`, domiciliario);
  }

  deleteDomiciliario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  cambiarDisponibilidad(domiciliario: Domiciliario, disponible: boolean): Observable<Domiciliario> {
    return this.http.put<Domiciliario>(`${this.apiUrl}/${domiciliario.idDomiciliario}`, {
      ...domiciliario,
      disponible
    });
  }

  cambiarEstadoActivo(domiciliario: Domiciliario, activo: boolean): Observable<Domiciliario> {
    return this.http.put<Domiciliario>(`${this.apiUrl}/${domiciliario.idDomiciliario}`, {
      ...domiciliario,
      activo
    });
  }
}
