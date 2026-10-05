import { Component, input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Comida } from '../../../../models/comida.model';

@Component({
  selector: 'app-comida-card',
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './comida-card.component.html',
  styleUrl: './comida-card.component.scss'
})
export class ComidaCardComponent {
  comida = input.required<Comida>();
}
