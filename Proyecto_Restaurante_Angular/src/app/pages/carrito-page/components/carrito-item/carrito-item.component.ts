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
  adicionalCambiado = output<{ adicionalId: number; event: Event }>();
  adicionalesGuardados = output<void>();
}
