import { Injectable, inject } from '@angular/core';
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
