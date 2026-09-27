import { CurrencyPipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Comida } from '../../../../models/comida.model';

@Component({
  selector: 'app-comida-table',
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './comida-table.component.html',
  styleUrl: './comida-table.component.scss'
})
export class ComidaTableComponent {
  comidas = input.required<Comida[]>();
  comidaEliminada = output<number>();
}
