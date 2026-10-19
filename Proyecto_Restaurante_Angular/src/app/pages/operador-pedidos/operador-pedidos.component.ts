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
export class OperadorPedidosComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  pedidos: Pedido[] = [];
  pedidoSeleccionado?: Pedido;
  estado = '';
  error = '';
  mensaje = '';
  readonly estados = ['Pendiente', 'En preparación', 'En camino', 'Entregado', 'Cancelado'];

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
  }

  actualizarEstado(): void {
    if (!this.pedidoSeleccionado) return;
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
