import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, throwError } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
/** Encapsula el CRUD de clientes y la validación del inicio de sesión. */
export class ClienteService {
  private readonly apiUrl = 'http://localhost:8080/api/clientes';

  constructor(private http: HttpClient, private auth: AuthService) {}

  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  getClienteById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  getClienteActual(): Observable<Cliente | undefined> {
    if (!localStorage.getItem('token')) return of(undefined);
    return this.auth.consultarPerfil<Cliente>(this.apiUrl).pipe(
      catchError(error => error.status === 401 || error.status === 403 || error.status === 404
        ? of(undefined)
        : throwError(() => error))
    );
  }

  getClienteActualId(): number | undefined {
    return this.auth.perfilEnSesion<Cliente>(this.apiUrl)?.idCliente;
  }

  iniciarSesion(correo: string, password: string): Observable<Cliente> {
    return this.auth.iniciarSesion<Cliente>(this.apiUrl, { correo, password });
  }

  addCliente(cliente: Omit<Cliente, 'idCliente'>): Observable<void> {
    return this.http.post<void>(this.apiUrl, cliente);
  }

  updateCliente(id: number, cliente: Omit<Cliente, 'idCliente'>): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, cliente);
  }

  deleteCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
