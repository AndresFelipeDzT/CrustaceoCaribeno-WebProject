import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { concatMap, map, Observable, of, switchMap, toArray } from 'rxjs';
import { Adicional } from '../models/adicional.model';
import { Carrito, CarritoDTO } from '../models/carrito.model';
import { Cupon, CuponAplicacion } from '../models/cupon.model';
import { ItemCarrito, ItemCarritoDTO } from '../models/item-carrito.model';
import { PedidoDTO } from '../models/pedido.model';
import { RepetirPedido } from '../models/repetir-pedido.model';
import { ComidaService } from './comida.service';

@Injectable({ providedIn: 'root' })
/** Encapsula las operaciones de carrito e ítems del cliente. */
export class CarritoService {
  private readonly apiUrl = 'http://localhost:8080/api/carritos';

  constructor(private http: HttpClient, private comidaService: ComidaService) {}

  getCarrito(idCliente: number): Observable<Carrito> {
    return this.http.get<CarritoDTO>(`${this.apiUrl}/${idCliente}`).pipe(
      switchMap(carrito => this.http.get<ItemCarritoDTO[]>(`${this.apiUrl}/${idCliente}/items`).pipe(
        switchMap(items => items.length === 0
          ? of({ ...carrito, items: [] })
          : of(...items).pipe(
            concatMap(item => this.comidaService.getComidaById(item.idProducto).pipe(
              switchMap(producto => this.getAdicionales(idCliente, item.idItemCarrito).pipe(
                map((adicionales): ItemCarrito => ({
                  idItemCarrito: item.idItemCarrito,
                  cantidad: item.cantidad,
                  producto,
                  adicionales,
                  subtotal: item.subtotal
                }))
              ))
            )),
            toArray(),
            map(cartItems => ({ ...carrito, items: cartItems }))
          )
        )
      ))
    );
  }

  repetirPedido(idCliente: number, idPedido: number): Observable<RepetirPedido> {
    return this.http.post<RepetirPedido>(`${this.apiUrl}/${idCliente}/repetir/${idPedido}`, {});
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

  getCuponDisponible(idCliente: number): Observable<Cupon | null> {
    return this.http.get<Cupon | null>(`${this.apiUrl}/${idCliente}/cupon`);
  }

  validarCupon(idCliente: number, codigoCupon: string): Observable<CuponAplicacion> {
    return this.http.post<CuponAplicacion>(`${this.apiUrl}/${idCliente}/cupon/validar`, { codigoCupon });
  }

  confirmarPedido(idCliente: number, codigoCupon?: string): Observable<PedidoDTO> {
    return this.http.post<PedidoDTO>(`${this.apiUrl}/${idCliente}/confirmar`, { codigoCupon });
  }

  private getAdicionales(idCliente: number, idItemCarrito: number): Observable<Adicional[]> {
    return this.http.get<Adicional[]>(
      `${this.apiUrl}/${idCliente}/items/${idItemCarrito}/adicionales`
    );
  }
}
