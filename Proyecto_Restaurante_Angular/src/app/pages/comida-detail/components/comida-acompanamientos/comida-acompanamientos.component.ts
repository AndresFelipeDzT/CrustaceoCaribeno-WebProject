import { CurrencyPipe } from '@angular/common';
import { Component, inject, Input, OnInit } from '@angular/core';
import { Adicional } from '../../../../models/adicional.model';
import { ComidaService } from '../../../../service/comida.service';

@Component({
  selector: 'app-comida-acompanamientos',
  imports: [CurrencyPipe],
  templateUrl: './comida-acompanamientos.component.html',
  styleUrl: './comida-acompanamientos.component.scss'
})
/** Muestra únicamente los adicionales activos del producto. */
export class ComidaAcompanamientosComponent implements OnInit {
  @Input() categoriaId?: number;
  private comidaService = inject(ComidaService);
  adicionales: Adicional[] = [];
  error = '';

  ngOnInit(): void {
    this.comidaService.getAdicionales(this.categoriaId).subscribe({
      next: adicionales => this.adicionales = adicionales.filter(adicional => adicional.activo),
      error: () => this.error = 'No fue posible cargar los adicionales.'
    });
  }

}
