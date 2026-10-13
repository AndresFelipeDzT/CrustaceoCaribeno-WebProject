import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Carrito } from '../models/carrito.model';
import { Pedido } from '../models/pedido.model';

@Injectable({ providedIn: 'root' })
export class CarritoService {
  private readonly apiUrl = 'http://localhost:8080/api/carritos';

  constructor(private http: HttpClient) {}

  getCarrito(idCliente: number): Observable<Carrito> {
    return this.http.get<Carrito>(`${this.apiUrl}/${idCliente}`);
  }

  agregarProducto(idCliente: number, idProducto: number, idsAdicionales: number[] = []): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${idCliente}/items`, {
      idProducto,
      idsAdicionales
    });
  }

  actualizarCantidad(idCliente: number, idItemCarrito: number, cantidad: number): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/${idCliente}/items/${idItemCarrito}/cantidad`,
      { cantidad }
    );
  }

  actualizarAdicionales(idCliente: number, idItemCarrito: number, idsAdicionales: number[]): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/${idCliente}/items/${idItemCarrito}/adicionales`,
      { idsAdicionales }
    );
  }

  eliminarItem(idCliente: number, idItemCarrito: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idCliente}/items/${idItemCarrito}`);
  }

  confirmarPedido(idCliente: number): Observable<Pedido> {
    return this.http.post<Pedido>(`${this.apiUrl}/${idCliente}/confirmar`, null);
  }
}
