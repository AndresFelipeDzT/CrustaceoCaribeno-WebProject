import { Component, input } from '@angular/core';
import { Comida } from '../../../../models/comida.model';
import { ComidaCardComponent } from '../comida-card/comida-card.component';

@Component({
  selector: 'app-comida-list',
  imports: [ComidaCardComponent],
  templateUrl: './comida-list.component.html',
  styleUrl: './comida-list.component.scss'
})
export class ComidaListComponent {
  comidas = input.required<Comida[]>();
  readonly categorias = [
    { valor: 'Entrada', titulo: 'Entrada' },
    { valor: 'Plato Fuerte', titulo: 'Plato Fuerte' },
    { valor: 'Especialidades De La Casa', titulo: 'Especialidades De La Casa' },
    { valor: 'Postre', titulo: 'Postre' },
    { valor: 'Bebida', titulo: 'Bebida' }
  ];

  comidasPorCategoria(categoria: string): Comida[] {
    return this.comidas().filter(comida => comida.categoria === categoria);
  }
}
