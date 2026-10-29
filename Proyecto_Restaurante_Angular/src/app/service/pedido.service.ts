import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { concatMap, map, Observable, of, switchMap, toArray } from 'rxjs';
import { Adicional } from '../models/adicional.model';
import { ItemPedido, ItemPedidoDTO } from '../models/item-pedido.model';
import { Pedido, PedidoDTO } from '../models/pedido.model';
import { ClienteService } from './cliente.service';
import { ComidaService } from './comida.service';

/** Flujo secuencial y ordenado de los estados regulares por los que pasa un pedido. */
export const FLUJO_ESTADOS_PEDIDO = [
  'Pendiente',
  'En preparación',
  'Enviado',
  'En camino',
  'Entregado'
] as const;

export type EstadoPedidoFlujo = typeof FLUJO_ESTADOS_PEDIDO[number];

@Injectable({ providedIn: 'root' })
/** Obtiene pedidos y completa sus datos relacionados para las vistas. */
export class PedidoService {
  private readonly apiUrl = 'http://localhost:8080/api/pedidos';

  readonly flujoEstados = FLUJO_ESTADOS_PEDIDO;

  constructor(
    private http: HttpClient,
    private clienteService: ClienteService,
    private comidaService: ComidaService
  ) {}

  /** Normaliza el estado para comparaciones flexibles (sin tildes, minúsculas, espacios recortados). */
  normalizarEstado(estado?: string | null): string {
    return (estado ?? '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }

  /** Devuelve el siguiente estado lógico en el flujo, o null si ya no tiene siguiente. */
  obtenerSiguienteEstado(estadoActual?: string | null): string | null {
    if (!estadoActual) return this.flujoEstados[0];
    const normalizado = this.normalizarEstado(estadoActual);
    const indice = this.flujoEstados.findIndex(e => this.normalizarEstado(e) === normalizado);
    if (indice >= 0 && indice < this.flujoEstados.length - 1) {
      return this.flujoEstados[indice + 1];
    }
    return null;
  }

  /** Valida si el nuevo estado corresponde exactamente al siguiente estado en el flujo. */
  esSiguienteEstado(estadoActual?: string | null, nuevoEstado?: string | null): boolean {
    const siguiente = this.obtenerSiguienteEstado(estadoActual);
    if (!siguiente || !nuevoEstado) return false;
    return this.normalizarEstado(siguiente) === this.normalizarEstado(nuevoEstado);
  }

  /** Determina si un pedido puede ser cancelado por el operador (no cancelable si ya fue entregado o cancelado). */
  puedeCancelar(estadoActual?: string | null): boolean {
    const norm = this.normalizarEstado(estadoActual);
    return norm !== '' && norm !== 'entregado' && norm !== 'cancelado';
  }

  /** Valida si la transición es permitida para el operador: avanzar al siguiente estado o cancelar (salvo si ya fue entregado). */
  esTransicionValidaOperador(estadoActual?: string | null, nuevoEstado?: string | null): boolean {
    if (!estadoActual || !nuevoEstado) return false;
    if (this.normalizarEstado(estadoActual) === this.normalizarEstado(nuevoEstado)) return false;
    if (this.normalizarEstado(nuevoEstado) === 'cancelado') {
      return this.puedeCancelar(estadoActual);
    }
    return this.esSiguienteEstado(estadoActual, nuevoEstado);
  }

  
  getPedidos(soloActivos = false, clienteId?: number): Observable<Pedido[]> {
    let params = new HttpParams();
    if (soloActivos) params = params.set('soloActivos', 'true');
    if (clienteId !== undefined) params = params.set('clienteId', clienteId);
    return this.http.get<PedidoDTO[]>(this.apiUrl, { params }).pipe(
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

  actualizarPedido(id: number, estado: string, idDomiciliario?: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, {
      estado,
      ...(idDomiciliario === undefined ? {} : { domiciliario: { idDomiciliario } })
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
