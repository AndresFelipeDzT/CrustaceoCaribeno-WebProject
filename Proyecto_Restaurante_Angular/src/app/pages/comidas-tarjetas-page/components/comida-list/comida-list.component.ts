import { Component, input } from '@angular/core';
import { Producto } from '../../../../models/producto.model';
import { ComidaCardComponent } from '../comida-card/comida-card.component';

@Component({
  selector: 'app-comida-list',
  imports: [ComidaCardComponent],
  templateUrl: './comida-list.component.html',
  styleUrl: './comida-list.component.scss'
})
export class ComidaListComponent {
  comidas = input.required<Producto[]>();
  readonly categorias = [
    { valor: 'Entrada', titulo: 'Entrada' },
    { valor: 'Plato Fuerte', titulo: 'Plato Fuerte' },
    { valor: 'Especialidades De La Casa', titulo: 'Especialidades De La Casa' },
    { valor: 'Postre', titulo: 'Postre' },
    { valor: 'Bebida', titulo: 'Bebida' }
  ];

  comidasPorCategoria(categoria: string): Producto[] {
    return this.comidas().filter(comida =>
      this.normalizarCategoria(comida.categoria?.nombre ?? '') === this.normalizarCategoria(categoria)
    );
  }

  private normalizarCategoria(categoria: string): string {
    const nombre = categoria.normalize('NFD').replace(/[\u0300-\u036f]/g, '').trim().toLowerCase();
    return nombre.startsWith('especialidades de la casa') ? 'especialidad de la casa' : nombre;
  }
}
