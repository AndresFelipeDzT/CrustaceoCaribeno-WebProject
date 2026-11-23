import { Component, inject, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdministradorService } from '../../../../service/administrador.service';
import { ResumenVentas } from '../../../../models/resumen-ventas.model';

@Component({
  selector: 'app-estadisticas-ventas',
  imports: [CurrencyPipe, FormsModule],
  templateUrl: './estadisticas-ventas.component.html',
  styleUrl: './estadisticas-ventas.component.css'
})
export class EstadisticasVentasComponent implements OnInit {
  private administrador = inject(AdministradorService);
  desde = '';
  hasta = '';
  resumen?: ResumenVentas;
  cargando = false;
  error = '';

  ngOnInit(): void { this.consultar(); }

  consultar(): void {
    if (this.cargando) return;
    this.resumen = undefined;
    if (this.desde && this.hasta && this.desde > this.hasta) {
      this.error = 'Revisa el orden de las fechas.';
      return;
    }
    this.error = '';
    this.cargando = true;
    this.administrador.getEstadisticasVentas(this.desde, this.hasta).subscribe({
      next: resumen => {
        this.resumen = resumen;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No fue posible consultar las ventas.';
        this.cargando = false;
      }
    });
  }
}
