import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Pedido } from '../../models/pedido.model';
import { Domiciliario } from '../../models/domiciliario.model';
import { PedidoService } from '../../service/pedido.service';
import { DomiciliarioService } from '../../service/domiciliario.service';
import { AdministradorService } from '../../service/administrador.service';
import { PedidoDetalleComponent } from './components/pedido-detalle/pedido-detalle.component';
import { PedidosListaComponent } from './components/pedidos-lista/pedidos-lista.component';
import { catchError, of, switchMap, throwError } from 'rxjs';

@Component({
  selector: 'app-pedidos-admin',
  imports: [RouterLink, PedidoDetalleComponent, PedidosListaComponent],
  templateUrl: './pedidos-admin.component.html',
  styleUrl: './pedidos-admin.component.css'
})
/** Gestiona pedidos para administradores y reutiliza la vista para operadores. */
export class PedidosAdminComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  private domiciliarioService = inject(DomiciliarioService);
  private administradorService = inject(AdministradorService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  pedidos: Pedido[] = [];
  domiciliarios: Domiciliario[] = [];
  idDomiciliarioSeleccionado?: number;
  pedido?: Pedido;
  estado = '';
  error = '';
  mensaje = '';
  /** Determina el rol mediante la ruta para adaptar acciones y resultados visibles. */
  get esOperador(): boolean { return this.router.url.split('?')[0].startsWith('/operador/'); }
  get operadorId(): number | undefined {
    const id = Number(this.route.snapshot.queryParamMap.get('idOperador'));
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }
  get administradorId(): number | undefined { return this.administradorService.getAdministradorActualId(); }
  readonly estados = ['Pendiente', 'En preparación', 'Enviado', 'En camino', 'Entregado', 'Cancelado'];
  get domiciliariosDisponibles(): Domiciliario[] {
    return this.domiciliarios.filter(domiciliario => domiciliario.activo && domiciliario.disponible);
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

  cargarPedidos(): void {
    this.error = '';
    // El portal de operador solicita al backend únicamente los pedidos que no se han completado.
    this.pedidoService.getPedidos(this.esOperador).subscribe({
      next: pedidos => this.pedidos = pedidos,
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
        this.idDomiciliarioSeleccionado = pedido.idDomiciliario ?? undefined;
      },
      error: err => this.error = err?.error?.message || 'No fue posible cargar el pedido.'
    });
  }

  actualizarEstado(): void {
    if (!this.pedido) return;
    this.error = '';
    this.mensaje = '';

    const estadoNormalizado = this.estado.trim().toLowerCase();
    const estadoActualNormalizado = this.pedido.estado.trim().toLowerCase();
    const requiereAsignacion = estadoNormalizado === 'enviado' && estadoActualNormalizado !== 'enviado';
    const domiciliarioAnteriorId = this.pedido.idDomiciliario ?? undefined;

    if (requiereAsignacion) {
      const domiciliarioSeleccionado = this.domiciliariosDisponibles.find(
        domiciliario => domiciliario.idDomiciliario === this.idDomiciliarioSeleccionado
      );
      if (!domiciliarioSeleccionado) {
        this.error = 'Selecciona un domiciliario disponible para enviar el pedido.';
        return;
      }

      this.asignarDomiciliario(domiciliarioSeleccionado, domiciliarioAnteriorId);
      return;
    }

    this.pedidoService.actualizarPedido(this.pedido.idPedido, this.estado).subscribe({
      next: () => {
        this.confirmarActualizacion(this.estado);
        if (['entregado', 'cancelado'].includes(estadoNormalizado) && domiciliarioAnteriorId !== undefined) {
          this.liberarDomiciliario(domiciliarioAnteriorId);
        }
      },
      error: err => this.error = err?.error?.message || 'No fue posible actualizar el pedido.'
    });
  }

  cambiarEstado(estado: string): void {
    this.estado = estado;
    if (estado.trim().toLowerCase() === 'enviado' && this.pedido?.estado.trim().toLowerCase() !== 'enviado') {
      this.idDomiciliarioSeleccionado = this.domiciliariosDisponibles[0]?.idDomiciliario;
    }
  }

  private cargarDomiciliarios(): void {
    this.domiciliarioService.getDomiciliarios().subscribe({
      next: domiciliarios => this.domiciliarios = domiciliarios,
      error: () => this.error = 'No fue posible consultar la disponibilidad de los domiciliarios.'
    });
  }

  private asignarDomiciliario(domiciliario: Domiciliario, domiciliarioAnteriorId?: number): void {
    if (!this.pedido) return;

    this.domiciliarioService.getDomiciliarioById(domiciliario.idDomiciliario).pipe(
      switchMap(actual => {
        if (!actual.activo || !actual.disponible) {
          return throwError(() => new Error('El domiciliario seleccionado está inactivo o no está disponible. Actualiza la selección.'));
        }
        return this.domiciliarioService.cambiarDisponibilidad(actual, false);
      }),
      switchMap(domiciliarioOcupado => this.pedidoService.actualizarPedido(
        this.pedido!.idPedido,
        this.estado,
        domiciliarioOcupado.idDomiciliario
      ).pipe(
        catchError(error => this.domiciliarioService.cambiarDisponibilidad(
          { ...domiciliarioOcupado, disponible: false },
          true
        ).pipe(
          catchError(() => throwError(() => new Error(
            'No se guardó el pedido y no fue posible restablecer la disponibilidad del domiciliario.'
          ))),
          switchMap(() => throwError(() => error))
        ))
      ))
    ).subscribe({
      next: () => {
        const idAsignado = this.idDomiciliarioSeleccionado;
        this.confirmarActualizacion(this.estado, idAsignado);
        if (domiciliarioAnteriorId !== undefined && domiciliarioAnteriorId !== idAsignado) {
          this.liberarDomiciliario(domiciliarioAnteriorId);
        }
      },
      error: err => this.error = err?.error?.message || err?.message || 'No fue posible asignar el domiciliario.'
    });
  }

  private confirmarActualizacion(estado: string, idDomiciliario?: number): void {
    if (!this.pedido) return;
    this.pedido = {
      ...this.pedido,
      estado,
      ...(idDomiciliario === undefined ? {} : { idDomiciliario })
    };
    this.estado = estado;
    this.mensaje = 'Pedido actualizado correctamente.';
    this.cargarPedidos();
    this.cargarDomiciliarios();
  }

  private liberarDomiciliario(idDomiciliario: number): void {
    this.domiciliarioService.getDomiciliarioById(idDomiciliario).pipe(
      switchMap(domiciliario => !domiciliario.activo || domiciliario.disponible
        ? of(domiciliario)
        : this.domiciliarioService.cambiarDisponibilidad(domiciliario, true)
      )
    ).subscribe({
      next: () => this.cargarDomiciliarios(),
      error: () => this.error = 'El pedido se actualizó, pero no fue posible liberar al domiciliario.'
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
