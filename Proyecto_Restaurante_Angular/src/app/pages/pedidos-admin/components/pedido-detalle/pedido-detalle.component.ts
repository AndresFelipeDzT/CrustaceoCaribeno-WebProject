import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Pedido } from '../../../../models/pedido.model';

@Component({
  selector: 'app-pedido-detalle',
  imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink],
  templateUrl: './pedido-detalle.component.html',
  styleUrl: './pedido-detalle.component.css'
})
export class PedidoDetalleComponent {
  pedido = input.required<Pedido>();
  estado = input.required<string>();
  estados = input.required<string[]>();
  esOperador = input(false);
  operadorId = input<number>();
  administradorId = input<number>();
  mensaje = input('');
  error = input('');
  estadoChange = output<string>();
  actualizarEstado = output<void>();
}
