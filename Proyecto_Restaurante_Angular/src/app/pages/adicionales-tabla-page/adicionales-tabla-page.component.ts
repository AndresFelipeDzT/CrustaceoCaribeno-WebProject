import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FooterComponent } from '../../components/footer/footer.component';
import { TableNavbarComponent } from '../../components/navbar/table-navbar/table-navbar.component';
import { Adicional } from '../../models/adicional.model';
import { Categoria } from '../../models/categoria.model';
import { ComidaService } from '../../service/comida.service';
import { AdicionalTableComponent } from './components/adicional-table/adicional-table.component';

@Component({
  selector: 'app-adicionales-tabla-page',
  imports: [RouterLink, FooterComponent, TableNavbarComponent, AdicionalTableComponent],
  templateUrl: './adicionales-tabla-page.component.html',
  styleUrl: './adicionales-tabla-page.component.scss'
})
export class AdicionalesTablaPageComponent implements OnInit {
  private comidaService = inject(ComidaService);
  adicionales: Adicional[] = [];
  categorias: Categoria[] = [];
  error = '';

  ngOnInit(): void {
    this.comidaService.getCategorias().subscribe({
      next: categorias => this.categorias = categorias,
      error: () => this.error = 'No fue posible cargar las categorías.'
    });
    this.cargarAdicionales();
  }

  cargarAdicionales(): void {
    this.error = '';
    this.comidaService.getAdicionales(undefined, true).subscribe({
      next: adicionales => this.adicionales = adicionales,
      error: () => this.error = 'No fue posible cargar los adicionales.'
    });
  }

  eliminarAdicional(id: number): void {
    this.comidaService.deleteAdicional(id).subscribe({
      next: () => this.cargarAdicionales(),
      error: () => this.error = 'No fue posible desactivar el adicional.'
    });
  }

  activarAdicional(adicional: Adicional): void {
    this.comidaService.updateAdicional(adicional.idAdicional, {
      nombre: adicional.nombre,
      precio: adicional.precio,
      categoriaIds: adicional.categoriaIds
    }).subscribe({
      next: () => this.cargarAdicionales(),
      error: () => this.error = 'No fue posible activar el adicional.'
    });
  }
}
