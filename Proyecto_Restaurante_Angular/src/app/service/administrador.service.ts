import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Administrador } from '../models/administrador.model';

@Injectable({
  providedIn: 'root'
})
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

  iniciarSesion(usuario: string, contrasena: string): Observable<Administrador | undefined> {
    return this.http.get<Administrador[]>(this.apiUrl).pipe(
      map(administradores => administradores.find(administrador =>
        administrador.usuario.trim().toLowerCase() === usuario.trim().toLowerCase() &&
        administrador.contrasena === contrasena
      ))
    );
  }
}
