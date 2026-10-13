import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private readonly apiUrl = 'http://localhost:8080/api/clientes';

  constructor(private http: HttpClient, private router: Router) {}

  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  getClienteById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  getClienteActual(): Observable<Cliente | undefined> {
    const id = this.getClienteActualId();
    return id === undefined ? of(undefined) : this.getClienteById(id);
  }

  getClienteActualId(): number | undefined {
    const idParam = this.router.parseUrl(this.router.url).queryParams['id'];
    if (typeof idParam !== 'string') return undefined;
    const id = Number(idParam);
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  iniciarSesion(correo: string, password: string): Observable<Cliente | undefined> {
    return this.getClientes().pipe(
      map(clientes => clientes.find(cliente =>
        cliente.activo &&
        cliente.correo.trim().toLowerCase() === correo.trim().toLowerCase() &&
        cliente.password === password
      ))
    );
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
