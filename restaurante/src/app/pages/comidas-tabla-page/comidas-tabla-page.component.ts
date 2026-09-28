import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FooterComponent } from '../../components/footer/footer.component';
import { TableNavbarComponent } from '../../components/navbar/table-navbar/table-navbar.component';
import { Comida } from '../../models/comida.model';
import { ComidaService } from '../../service/comida.service';
import { TableHeroBannerComponent } from './components/hero-banner/hero-banner.component';
import { ComidaTableComponent } from './components/comida-table/comida-table.component';

@Component({
  selector: 'app-comidas-tabla-page',
  imports: [RouterLink, FooterComponent, TableNavbarComponent, TableHeroBannerComponent, ComidaTableComponent],
  templateUrl: './comidas-tabla-page.component.html',
  styleUrl: './comidas-tabla-page.component.scss'
})
export class ComidasTablaPageComponent implements OnInit {
  private comidaService = inject(ComidaService);
  comidas: Comida[] = [];

  ngOnInit(): void {
    this.comidas = [...this.comidaService.getComidas()];
  }

  eliminarComida(id: number): void {
    this.comidaService.deleteComida(id);
    this.comidas = [...this.comidaService.getComidas()];
  }

  activarComida(id: number): void {
    this.comidaService.activarComida(id);
    this.comidas = [...this.comidaService.getComidas()];
  }

}
