import { Component, inject, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Pedido } from '../../models/pedido.model';
import { PedidoService } from '../../service/pedido.service';

@Component({
  selector: 'app-pedidos-admin',
  imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink],
  templateUrl: './pedidos-admin.component.html',
  styleUrl: './pedidos-admin.component.css'
})
export class PedidosAdminComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  pedidos: Pedido[] = [];
  pedido?: Pedido;
  estado = '';
  error = '';
  mensaje = '';
  readonly estados = ['Pendiente', 'En preparación', 'En camino', 'Entregado', 'Cancelado'];

  ngOnInit(): void {
    this.cargarPedidos();
    this.route.paramMap.subscribe(params => {
      const pedidoId = Number(params.get('id'));
      this.pedido = undefined;
      if (pedidoId > 0) this.verPedido(pedidoId);
    });
  }

  cargarPedidos(): void {
    this.error = '';
    this.pedidoService.getPedidos().subscribe({
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
      },
      error: () => this.error = 'No fue posible cargar el pedido.'
    });
  }

  actualizarEstado(): void {
    if (!this.pedido) return;
    const pedido = { ...this.pedido, estado: this.estado };
    this.pedidoService.actualizarPedido(pedido.idPedido, this.estado).subscribe({
      next: () => {
        this.pedido = pedido;
        this.mensaje = 'Pedido actualizado correctamente.';
        this.cargarPedidos();
      },
      error: () => this.error = 'No fue posible actualizar el pedido.'
    });
  }

  eliminarPedido(id: number): void {
    this.pedidoService.eliminarPedido(id).subscribe({
      next: () => {
        this.cargarPedidos();
        void this.router.navigate(['/pedidos']);
      },
      error: () => this.error = 'No fue posible eliminar el pedido.'
    });
  }
}
