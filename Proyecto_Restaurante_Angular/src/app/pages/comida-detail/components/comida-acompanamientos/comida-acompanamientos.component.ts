import { CurrencyPipe } from '@angular/common';
import { Component, Input, inject, OnChanges } from '@angular/core';
import { Adicional } from '../../../../models/adicional.model';
import { ComidaService } from '../../../../service/comida.service';

@Component({
  selector: 'app-comida-acompanamientos',
  imports: [CurrencyPipe],
  templateUrl: './comida-acompanamientos.component.html',
  styleUrl: './comida-acompanamientos.component.scss'
})
export class ComidaAcompanamientosComponent implements OnChanges {
  @Input({ required: true }) categoria = '';
  private comidaService = inject(ComidaService);
  adicionales: Adicional[] = [];

  ngOnChanges(): void {
    this.adicionales = this.comidaService.getAdicionalesPorCategoria(this.categoria);
  }
}
