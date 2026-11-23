import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ResumenVentas } from '../models/resumen-ventas.model';
import { Observable } from 'rxjs';
import { Administrador } from '../models/administrador.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
/** Consulta el perfil del administrador autenticado. */
export class AdministradorService {
  private readonly apiUrl = 'http://localhost:8080/api/administradores';
  private auth = inject(AuthService);

  private http = inject(HttpClient);

  getEstadisticasVentas(desde: string, hasta: string): Observable<ResumenVentas> {
    const params: Record<string, string> = {};
    if (desde) params['desde'] = desde;
    if (hasta) params['hasta'] = hasta;
    return this.http.get<ResumenVentas>(`${this.apiUrl}/estadisticas/ventas`, { params });
  }

  getAdministradorActual(): Observable<Administrador> {
    return this.auth.consultarPerfil<Administrador>(this.apiUrl);
  }

  getAdministradorActualId(): number | undefined {
    return this.auth.perfilEnSesion<Administrador>(this.apiUrl)?.idAdministrador;
  }

  /** Envía las credenciales al backend para autenticar al administrador. */
  iniciarSesion(usuario: string, contrasena: string): Observable<Administrador> {
    return this.auth.iniciarSesion<Administrador>(this.apiUrl, { usuario, contrasena });
  }
}
