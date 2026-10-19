import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Pedido } from '../../../../models/pedido.model';

@Component({
  selector: 'app-pedidos-lista',
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './pedidos-lista.component.html',
  styleUrl: './pedidos-lista.component.css'
})
export class PedidosListaComponent {
  pedidos = input.required<Pedido[]>();
  esOperador = input(false);
  operadorId = input<number>();
  administradorId = input<number>();
  mensaje = input('');
  error = input('');
  pedidoEliminado = output<number>();
}
