import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Administrador } from '../models/administrador.model';

@Injectable({
  providedIn: 'root'
})
/** Consulta administradores y resuelve el administrador indicado en la URL actual. */
export class AdministradorService {
  private readonly apiUrl = 'http://localhost:8080/api/administradores';
  private http = inject(HttpClient);
  private router = inject(Router);

  getAdministradorActualId(): number | undefined {
    const idParam = this.router.parseUrl(this.router.url).queryParams['idAdministrador'];
    if (typeof idParam !== 'string') return undefined;
    const id = Number(idParam);
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  /** Envía las credenciales al backend para autenticar al administrador. */
  iniciarSesion(usuario: string, contrasena: string): Observable<Administrador> {
    return this.http.post<Administrador>(`${this.apiUrl}/login`, { usuario, contrasena });
  }
}
