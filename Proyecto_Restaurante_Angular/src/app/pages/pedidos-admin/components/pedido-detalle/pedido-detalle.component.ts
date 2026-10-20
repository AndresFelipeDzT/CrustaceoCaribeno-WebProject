import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Domiciliario } from '../../../../models/domiciliario.model';
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
  domiciliarios = input<Domiciliario[]>([]);
  domiciliarioId = input<number | null>(null);
  esOperador = input(false);
  operadorId = input<number>();
  administradorId = input<number>();
  mensaje = input('');
  error = input('');
  estadoChange = output<string>();
  domiciliarioIdChange = output<number | null>();
  actualizarEstado = output<void>();

  get requiereDomiciliario(): boolean {
    const valor = this.estado().trim().toLowerCase();
    return valor === 'en camino' || valor === 'enviado';
  }
}
