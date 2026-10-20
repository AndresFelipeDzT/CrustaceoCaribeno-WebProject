import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, input } from '@angular/core';
import { Pedido } from '../../../../models/pedido.model';

@Component({
  selector: 'app-cliente-pedidos',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './cliente-pedidos.component.html',
  styleUrl: './cliente-pedidos.component.scss'
})
/** Muestra los pedidos activos y pasados del cliente dentro de su portal. */
export class ClientePedidosComponent {
  pedidosActivos = input.required<Pedido[]>();
  pedidosPasados = input.required<Pedido[]>();
}
