import { CurrencyPipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Producto } from '../../../../models/producto.model';

@Component({
  selector: 'app-comida-plato',
  imports: [CurrencyPipe],
  templateUrl: './comida-plato.component.html',
  styleUrl: './comida-plato.component.scss'
})
/** Presenta la información principal de un plato. */
export class ComidaPlatoComponent {
  @Input({ required: true }) comida!: Producto;
}
