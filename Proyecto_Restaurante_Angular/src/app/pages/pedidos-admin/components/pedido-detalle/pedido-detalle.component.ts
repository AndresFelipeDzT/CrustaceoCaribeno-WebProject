import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Adicional } from '../../../../models/adicional.model';
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

  agruparAdicionales(adicionales: Adicional[]): { adicional: Adicional; cantidad: number }[] {
    const mapa = new Map<number, { adicional: Adicional; cantidad: number }>();
    for (const adicional of adicionales) {
      const actual = mapa.get(adicional.idAdicional);
      if (actual) {
        actual.cantidad++;
      } else {
        mapa.set(adicional.idAdicional, { adicional, cantidad: 1 });
      }
    }
    return Array.from(mapa.values());
  }
}
