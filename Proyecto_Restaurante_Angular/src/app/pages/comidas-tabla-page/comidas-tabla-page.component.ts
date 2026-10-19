import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Producto } from '../../models/producto.model';
import { ComidaService } from '../../service/comida.service';
import { ComidaTableComponent } from './components/comida-table/comida-table.component';

@Component({
  selector: 'app-comidas-tabla-page',
  imports: [RouterLink, ComidaTableComponent],
  templateUrl: './comidas-tabla-page.component.html',
  styleUrl: './comidas-tabla-page.component.scss'
})
export class ComidasTablaPageComponent implements OnInit {
  private comidaService = inject(ComidaService);
  private route = inject(ActivatedRoute);
  comidas: Producto[] = [];
  error = '';

  get administradorId(): number | undefined {
    const id = Number(this.route.snapshot.queryParamMap.get('idAdministrador'));
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  ngOnInit(): void {
    this.cargarComidas();
  }

  cargarComidas(): void {
    this.error = '';
    this.comidaService.getComidas(true).subscribe({
      next: comidas => this.comidas = comidas,
      error: () => this.error = 'No fue posible cargar los productos.'
    });
  }

  eliminarComida(id: number): void {
    this.comidaService.deleteComida(id).subscribe({
      next: () => this.cargarComidas(),
      error: () => this.error = 'No fue posible desactivar el producto.'
    });
  }

  activarComida(id: number): void {
    this.comidaService.activarComida(id).subscribe({
      next: () => this.cargarComidas(),
      error: () => this.error = 'No fue posible activar el producto.'
    });
  }
}
