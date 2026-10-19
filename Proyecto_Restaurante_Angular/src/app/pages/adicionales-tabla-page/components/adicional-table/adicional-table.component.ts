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
export class AdicionalTableComponent {
  adicionales = input.required<Adicional[]>();
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
