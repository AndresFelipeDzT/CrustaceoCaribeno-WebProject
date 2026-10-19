import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs';
import { Operador } from '../models/operador.model';

@Injectable({ providedIn: 'root' })
export class OperadorService {
  private readonly apiUrl = 'http://localhost:8080/api/operadores';
  private http = inject(HttpClient);

  getOperadores(): Observable<Operador[]> {
    return this.http.get<Operador[]>(this.apiUrl);
  }

  iniciarSesion(usuario: string, contrasena: string): Observable<Operador | undefined> {
    return this.getOperadores().pipe(map(operadores => operadores.find(operador =>
      operador.usuario.trim().toLowerCase() === usuario.trim().toLowerCase() && operador.contrasena === contrasena
    )));
  }

  getOperadorById(id: number): Observable<Operador> {
    return this.http.get<Operador>(`${this.apiUrl}/${id}`);
  }

  addOperador(operador: Omit<Operador, 'idOperador'>): Observable<Operador> {
    return this.http.post<Operador>(this.apiUrl, operador);
  }

  updateOperador(id: number, operador: Operador): Observable<Operador> {
    return this.http.put<Operador>(`${this.apiUrl}/${id}`, operador);
  }

  deleteOperador(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
