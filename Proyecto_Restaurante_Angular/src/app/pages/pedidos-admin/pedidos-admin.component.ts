import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Domiciliario } from '../../models/domiciliario.model';
import { Pedido } from '../../models/pedido.model';
import { PedidoService } from '../../service/pedido.service';
import { AdministradorService } from '../../service/administrador.service';
import { DomiciliarioService } from '../../service/domiciliario.service';
import { PedidoDetalleComponent } from './components/pedido-detalle/pedido-detalle.component';
import { PedidosListaComponent } from './components/pedidos-lista/pedidos-lista.component';

@Component({
  selector: 'app-pedidos-admin',
  imports: [RouterLink, PedidoDetalleComponent, PedidosListaComponent],
  templateUrl: './pedidos-admin.component.html',
  styleUrl: './pedidos-admin.component.css'
})
/** Gestiona pedidos para administradores y reutiliza la vista para operadores. */
export class PedidosAdminComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  private administradorService = inject(AdministradorService);
  private domiciliarioService = inject(DomiciliarioService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  pedidos: Pedido[] = [];
  domiciliarios: Domiciliario[] = [];
  pedido?: Pedido;
  estado = '';
  domiciliarioId: number | null = null;
  error = '';
  mensaje = '';
  /** Determina el rol mediante la ruta para adaptar acciones y resultados visibles. */
  get esOperador(): boolean { return this.router.url.split('?')[0].startsWith('/operador/'); }
  get operadorId(): number | undefined {
    const id = Number(this.route.snapshot.queryParamMap.get('idOperador'));
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }
  get administradorId(): number | undefined { return this.administradorService.getAdministradorActualId(); }
  readonly estados = ['Pendiente', 'En preparación', 'En camino', 'Entregado', 'Cancelado'];

  /** Filtra los domiciliarios disponibles e incluye el asignado actualmente al pedido. */
  get domiciliariosDisponibles(): Domiciliario[] {
    const actualId = this.pedido?.domiciliario?.idDomiciliario ?? this.pedido?.idDomiciliario ?? null;
    return this.domiciliarios.filter(dom => dom.disponible || dom.idDomiciliario === actualId);
  }

  ngOnInit(): void {
    this.cargarPedidos();
    this.cargarDomiciliarios();
    this.route.paramMap.subscribe(params => {
      const pedidoId = Number(params.get('id'));
      this.pedido = undefined;
      if (pedidoId > 0) this.verPedido(pedidoId);
    });
  }

  cargarDomiciliarios(): void {
    this.domiciliarioService.getDomiciliarios().subscribe({
      next: domiciliarios => this.domiciliarios = domiciliarios,
      error: () => this.error = 'No fue posible cargar los domiciliarios disponibles.'
    });
  }

  cargarPedidos(): void {
    this.error = '';
    this.pedidoService.getPedidos().subscribe({
      // Un operador no puede consultar pedidos ya entregados o cancelados.
      next: pedidos => this.pedidos = this.esOperador
        ? pedidos.filter(pedido => !['cancelado', 'entregado'].includes(pedido.estado.trim().toLowerCase()))
        : pedidos,
      error: () => this.error = 'No fue posible cargar los pedidos.'
    });
  }

  verPedido(id: number): void {
    this.error = '';
    this.mensaje = '';
    this.pedidoService.getPedidoById(id).subscribe({
      next: pedido => {
        this.pedido = pedido;
        this.estado = pedido.estado;
        this.domiciliarioId = pedido.domiciliario?.idDomiciliario ?? pedido.idDomiciliario ?? null;
      },
      error: err => this.error = err?.error?.message || 'No fue posible cargar el pedido.'
    });
  }

  actualizarEstado(): void {
    if (!this.pedido) return;
    this.error = '';
    this.mensaje = '';
    this.pedidoService.actualizarPedido(this.pedido.idPedido, this.estado, this.domiciliarioId).subscribe({
      next: () => {
        this.mensaje = 'Pedido actualizado correctamente.';
        this.cargarDomiciliarios();
        this.verPedido(this.pedido!.idPedido);
        this.cargarPedidos();
      },
      error: err => this.error = err?.error?.message || 'No fue posible actualizar el pedido.'
    });
  }

  eliminarPedido(id: number): void {
    this.pedidoService.eliminarPedido(id).subscribe({
      next: () => {
        this.cargarPedidos();
        void this.router.navigate(['/pedidos'], {
          queryParams: this.administradorId === undefined ? {} : { idAdministrador: this.administradorId }
        });
      },
      error: err => this.error = err?.error?.message || 'No fue posible eliminar el pedido.'
    });
  }
}
