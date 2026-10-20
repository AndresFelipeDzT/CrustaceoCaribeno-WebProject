import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { concatMap, map, Observable, of, switchMap, toArray } from 'rxjs';
import { Adicional } from '../models/adicional.model';
import { ItemPedido, ItemPedidoDTO } from '../models/item-pedido.model';
import { Pedido, PedidoDTO } from '../models/pedido.model';
import { ClienteService } from './cliente.service';
import { ComidaService } from './comida.service';
import { DomiciliarioService } from './domiciliario.service';

@Injectable({ providedIn: 'root' })
/** Obtiene pedidos y completa sus datos relacionados para las vistas. */
export class PedidoService {
  private readonly apiUrl = 'http://localhost:8080/api/pedidos';

  constructor(
    private http: HttpClient,
    private clienteService: ClienteService,
    private comidaService: ComidaService,
    private domiciliarioService: DomiciliarioService
  ) {}

  getPedidos(): Observable<Pedido[]> {
    return this.http.get<PedidoDTO[]>(this.apiUrl).pipe(
      switchMap(pedidos => this.clienteService.getClientes().pipe(
        switchMap(clientes => this.domiciliarioService.getDomiciliarios().pipe(
          map(domiciliarios => pedidos.map(pedido => ({
            ...pedido,
            cliente: clientes.find(cliente => cliente.idCliente === pedido.idCliente) ?? null,
            domiciliario: domiciliarios.find(dom => dom.idDomiciliario === pedido.idDomiciliario) ?? null
          })))
        ))
      ))
    );
  }

  /** Obtiene los pedidos de un cliente con sus productos, adicionales y domiciliario asignado. */
  getPedidosPorCliente(idCliente: number): Observable<Pedido[]> {
    return this.http.get<PedidoDTO[]>(`${this.apiUrl}?clienteId=${idCliente}`).pipe(
      switchMap(pedidos => pedidos.length === 0
        ? of([])
        : this.domiciliarioService.getDomiciliarios().pipe(
          switchMap(domiciliarios => of(...pedidos).pipe(
            concatMap(pedido => this.getItems(pedido.idPedido).pipe(
              map((items): Pedido => ({
                ...pedido,
                domiciliario: domiciliarios.find(dom => dom.idDomiciliario === pedido.idDomiciliario) ?? null,
                items
              }))
            )),
            toArray()
          ))
        )
      )
    );
  }

  getPedidoById(id: number): Observable<Pedido> {
    return this.http.get<PedidoDTO>(`${this.apiUrl}/${id}`).pipe(
      switchMap(pedido => this.getItems(id).pipe(
        switchMap(items => this.domiciliarioService.getDomiciliarios().pipe(
          switchMap(domiciliarios => {
            const domiciliario = domiciliarios.find(dom => dom.idDomiciliario === pedido.idDomiciliario) ?? null;
            return pedido.idCliente == null
              ? of({ ...pedido, cliente: null, domiciliario, items })
              : this.clienteService.getClienteById(pedido.idCliente).pipe(
                map(cliente => ({ ...pedido, cliente, domiciliario, items }))
              );
          })
        ))
      ))
    );
  }

  /** Actualiza el estado del pedido y, cuando pasa a En camino, el domiciliario seleccionado. */
  actualizarPedido(id: number, estado: string, idDomiciliario?: number | null): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, {
      estado,
      domiciliario: idDomiciliario ? { idDomiciliario } : null
    });
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
