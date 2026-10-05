import { CurrencyPipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Comida } from '../../../../models/comida.model';

@Component({
  selector: 'app-comida-plato',
  imports: [CurrencyPipe],
  templateUrl: './comida-plato.component.html',
  styleUrl: './comida-plato.component.scss'
})
export class ComidaPlatoComponent {
  @Input({ required: true }) comida!: Comida;
}
