import { Component, inject, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Pedido } from '../../models/pedido.model';
import { PedidoService } from '../../service/pedido.service';

@Component({
  selector: 'app-operador-pedidos',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './operador-pedidos.component.html',
  styleUrl: './operador-pedidos.component.scss'
})
/** Portal alternativo de pedidos para operadores. */
export class OperadorPedidosComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  pedidos: Pedido[] = [];
  pedidoSeleccionado?: Pedido;
  estado = '';
  error = '';
  mensaje = '';

  get siguienteEstado(): string | null {
    return this.pedidoSeleccionado
      ? this.pedidoService.obtenerSiguienteEstado(this.pedidoSeleccionado.estado)
      : null;
  }

  get puedeCancelar(): boolean {
    return this.pedidoSeleccionado
      ? this.pedidoService.puedeCancelar(this.pedidoSeleccionado.estado)
      : false;
  }

  get estados(): string[] {
    if (!this.pedidoSeleccionado) return [];
    const opciones = [this.pedidoSeleccionado.estado];
    const siguiente = this.siguienteEstado;
    if (siguiente) opciones.push(siguiente);
    if (this.puedeCancelar && !opciones.some(o => this.pedidoService.normalizarEstado(o) === 'cancelado')) {
      opciones.push('Cancelado');
    }
    return opciones;
  }

  get operadorId(): number | undefined {
    const id = Number(this.route.snapshot.queryParamMap.get('idOperador'));
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  ngOnInit(): void { this.cargarPedidos(); }

  cargarPedidos(): void {
    this.error = '';
    this.pedidoService.getPedidos().subscribe({
      next: pedidos => {
        this.pedidos = pedidos.filter(pedido => !['entregado', 'cancelado'].includes(pedido.estado.trim().toLowerCase()));
        this.pedidoSeleccionado = this.pedidos.find(p => p.idPedido === this.pedidoSeleccionado?.idPedido) ?? this.pedidos[0];
        this.estado = this.pedidoSeleccionado?.estado ?? '';
      },
      error: () => this.error = 'No fue posible cargar los pedidos.'
    });
  }

  seleccionar(pedido: Pedido): void {
    this.pedidoSeleccionado = pedido;
    this.estado = pedido.estado;
    this.mensaje = '';
    this.error = '';
  }

  actualizarEstado(): void {
    if (!this.pedidoSeleccionado) return;
    this.error = '';
    this.mensaje = '';

    const siguiente = this.siguienteEstado;
    const puedeCancelar = this.puedeCancelar;
    const estadoNorm = this.pedidoService.normalizarEstado(this.estado);
    const estadoActualNorm = this.pedidoService.normalizarEstado(this.pedidoSeleccionado.estado);

    if (estadoNorm === estadoActualNorm) {
      const opcionesTexto = [siguiente, puedeCancelar ? 'Cancelado' : null].filter(Boolean).join(' o ');
      this.error = opcionesTexto
        ? `Selecciona un nuevo estado (${opcionesTexto}) para actualizar el pedido.`
        : 'Este pedido ya no puede cambiar de estado.';
      return;
    }

    if (!this.pedidoService.esTransicionValidaOperador(this.pedidoSeleccionado.estado, this.estado)) {
      if (estadoNorm === 'cancelado') {
        this.error = 'No es posible cancelar un pedido que ya fue entregado.';
      } else {
        this.error = siguiente
          ? `Como operador, solo puedes cambiar el estado al siguiente (${siguiente})${puedeCancelar ? ' o a Cancelado' : ''}.`
          : 'Este pedido ya no puede cambiar de estado.';
      }
      return;
    }

    const pedidoActualizado = { ...this.pedidoSeleccionado, estado: this.estado };
    this.pedidoService.actualizarPedido(pedidoActualizado.idPedido, this.estado).subscribe({
      next: () => {
        this.mensaje = 'Estado del pedido actualizado.';
        this.pedidoSeleccionado = pedidoActualizado;
        this.cargarPedidos();
      },
      error: () => this.error = 'No fue posible actualizar el estado del pedido.'
    });
  }

  cerrarSesion(): void { void this.router.navigate(['/operador']); }
}
