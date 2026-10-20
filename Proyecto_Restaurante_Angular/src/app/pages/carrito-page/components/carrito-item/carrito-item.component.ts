import { CurrencyPipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { Adicional } from '../../../../models/adicional.model';
import { ItemCarrito } from '../../../../models/item-carrito.model';

@Component({
  selector: 'app-carrito-item',
  imports: [CurrencyPipe],
  templateUrl: './carrito-item.component.html',
  styleUrl: './carrito-item.component.css'
})
export class CarritoItemComponent {
  item = input.required<ItemCarrito>();
  adicionalesDisponibles = input.required<Adicional[]>();
  adicionalesSeleccionados = input.required<number[]>();
  cantidadCambiada = output<number>();
  eliminacionSolicitada = output<void>();
  adicionalCambiado = output<{ adicionalId: number; cantidad: number }>();
  adicionalEliminado = output<number>();
  adicionalesGuardados = output<void>();

  adicionalesAgrupados(): { adicional: Adicional; cantidad: number }[] {
    const mapa = new Map<number, { adicional: Adicional; cantidad: number }>();
    for (const adicional of this.item().adicionales) {
      const actual = mapa.get(adicional.idAdicional);
      if (actual) {
        actual.cantidad++;
      } else {
        mapa.set(adicional.idAdicional, { adicional, cantidad: 1 });
      }
    }
    return Array.from(mapa.values());
  }

  cantidadSeleccionada(idAdicional: number): number {
    return this.adicionalesSeleccionados().filter(id => id === idAdicional).length;
  }
}
