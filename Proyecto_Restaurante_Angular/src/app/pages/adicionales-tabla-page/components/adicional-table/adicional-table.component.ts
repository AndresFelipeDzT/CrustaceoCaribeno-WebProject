import { CurrencyPipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Adicional } from '../../../../models/adicional.model';
import { Categoria } from '../../../../models/categoria.model';

@Component({
  selector: 'app-adicional-table',
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './adicional-table.component.html',
  styleUrl: './adicional-table.component.scss'
})
/** Renderiza las filas de adicionales y comunica las acciones al contenedor. */
export class AdicionalTableComponent {
  adicionales = input.required<Adicional[]>();
  administradorId = input<number>();
  categorias = input.required<Categoria[]>();
  adicionalEliminado = output<number>();
  adicionalActivado = output<Adicional>();

  nombresCategorias(adicional: Adicional): string {
    const nombres = adicional.categoriaIds.map(id =>
      this.categorias().find(categoria => categoria.idCategoria === id)?.nombre ?? `Categoría ${id}`
    );
    return nombres.length > 0 ? nombres.join(', ') : 'Sin categorías';
  }
}
