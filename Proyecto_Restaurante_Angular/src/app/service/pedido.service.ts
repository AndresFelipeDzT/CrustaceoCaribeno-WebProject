import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { concatMap, map, Observable, of, switchMap, toArray } from 'rxjs';
import { Adicional } from '../models/adicional.model';
import { ItemPedido, ItemPedidoDTO } from '../models/item-pedido.model';
import { Pedido, PedidoDTO } from '../models/pedido.model';
import { ClienteService } from './cliente.service';
import { ComidaService } from './comida.service';

@Injectable({ providedIn: 'root' })
/** Obtiene pedidos y completa sus datos relacionados para las vistas. */
export class PedidoService {
  private readonly apiUrl = 'http://localhost:8080/api/pedidos';

  constructor(
    private http: HttpClient,
    private clienteService: ClienteService,
    private comidaService: ComidaService
  ) {}

  getPedidos(): Observable<Pedido[]> {
    return this.http.get<PedidoDTO[]>(this.apiUrl).pipe(
      switchMap(pedidos => this.clienteService.getClientes().pipe(
        map(clientes => pedidos.map(pedido => ({
          ...pedido,
          cliente: clientes.find(cliente => cliente.idCliente === pedido.idCliente) ?? null
        })))
      ))
    );
  }

  getPedidoById(id: number): Observable<Pedido> {
    return this.http.get<PedidoDTO>(`${this.apiUrl}/${id}`).pipe(
      switchMap(pedido => this.getItems(id).pipe(
        switchMap(items => pedido.idCliente == null
          ? of({ ...pedido, cliente: null, items })
          : this.clienteService.getClienteById(pedido.idCliente).pipe(
            map(cliente => ({ ...pedido, cliente, items }))
          )
        )
      ))
    );
  }

  actualizarPedido(id: number, estado: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, { estado });
  }

  eliminarPedido(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /** Enriquece los ítems con producto y adicionales requeridos por el detalle. */
  private getItems(idPedido: number): Observable<ItemPedido[]> {
    return this.http.get<ItemPedidoDTO[]>(`${this.apiUrl}/${idPedido}/items`).pipe(
      switchMap(items => items.length === 0
        ? of([])
        : of(...items).pipe(
          concatMap(item => this.comidaService.getComidaById(item.idProducto).pipe(
            switchMap(producto => this.getAdicionales(idPedido, item.idItemPedido).pipe(
              map((adicionales): ItemPedido => ({
                idItemPedido: item.idItemPedido,
                cantidad: item.cantidad,
                precioUnitario: item.precioUnitario,
                producto,
                adicionales,
                subtotal: item.subtotal
              }))
            ))
          )),
          toArray()
        )
      )
    );
  }

  private getAdicionales(idPedido: number, idItemPedido: number): Observable<Adicional[]> {
    return this.http.get<Adicional[]>(
      `${this.apiUrl}/${idPedido}/items/${idItemPedido}/adicionales`
    );
  }
}
