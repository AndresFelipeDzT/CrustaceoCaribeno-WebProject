import { Component, inject, OnInit } from '@angular/core';
import { Producto } from '../../models/producto.model';
import { ComidaService } from '../../service/comida.service';
import { MenuHeroBannerComponent } from './components/hero-banner/hero-banner.component';
import { ComidaListComponent } from './components/comida-list/comida-list.component';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-comidas-tarjetas-page',
  imports: [NavbarComponent, FooterComponent, MenuHeroBannerComponent, ComidaListComponent],
  templateUrl: './comidas-tarjetas-page.component.html',
  styleUrl: './comidas-tarjetas-page.component.scss'
})
/** Carga el menú visible para los clientes en formato de tarjetas. */
export class ComidasTarjetasPageComponent implements OnInit {
  private comidaService = inject(ComidaService);
  comidas: Producto[] = [];
  error = '';

  ngOnInit(): void {
    this.comidaService.getComidas().subscribe({
      next: comidas => this.comidas = comidas,
      error: () => this.error = 'No fue posible cargar el menú.'
    });
  }
}
