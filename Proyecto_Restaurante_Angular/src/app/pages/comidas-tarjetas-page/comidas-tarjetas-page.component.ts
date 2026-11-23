import { Component, inject, OnInit } from '@angular/core';
import { Producto } from '../../models/producto.model';
import { ComidaService } from '../../service/comida.service';
import { MenuHeroBannerComponent } from './components/hero-banner/hero-banner.component';
import { ComidaListComponent } from './components/comida-list/comida-list.component';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { LandingFooterComponent } from '../../components/footer/landing-footer/landing-footer.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { CarritoService } from '../../service/carrito.service';
import { ClienteService } from '../../service/cliente.service';
import { Cupon } from '../../models/cupon.model';

@Component({
  selector: 'app-comidas-tarjetas-page',
  imports: [NavbarComponent, LandingFooterComponent, MenuHeroBannerComponent, ComidaListComponent],
  templateUrl: './comidas-tarjetas-page.component.html',
  styleUrl: './comidas-tarjetas-page.component.scss'
})
/** Carga el menú visible para los clientes en formato de tarjetas. */
export class ComidasTarjetasPageComponent implements OnInit {
  private comidaService = inject(ComidaService);
  private carritoService = inject(CarritoService);
  private clienteService = inject(ClienteService);
  comidas: Producto[] = [];
  error = '';
  errorCupon = '';
  cuponDisponible: Cupon | null = null;

  ngOnInit(): void {
    this.comidaService.getComidas().subscribe({
      next: comidas => this.comidas = comidas,
      error: () => this.error = 'No fue posible cargar el menú.'
    });
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId !== undefined) {
      this.carritoService.getCuponDisponible(clienteId).subscribe({
        next: cupon => this.cuponDisponible = cupon,
        error: () => this.errorCupon = 'No fue posible consultar tu descuento disponible.'
      });
    }
  }
}
