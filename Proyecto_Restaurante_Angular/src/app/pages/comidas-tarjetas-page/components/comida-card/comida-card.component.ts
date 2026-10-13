import { Component, inject, input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Producto } from '../../../../models/producto.model';
import { CarritoService } from '../../../../service/carrito.service';
import { ClienteService } from '../../../../service/cliente.service';

@Component({
  selector: 'app-comida-card',
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './comida-card.component.html',
  styleUrl: './comida-card.component.scss'
})
export class ComidaCardComponent {
  private carritoService = inject(CarritoService);
  private clienteService = inject(ClienteService);
  private router = inject(Router);
  comida = input.required<Producto>();
  error = '';
  get clienteId(): number | undefined { return this.clienteService.getClienteActualId(); }
  get conectado(): boolean {
    return this.clienteId !== undefined;
  }

  agregarAlCarrito(): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) {
      void this.router.navigate(['/login']);
      return;
    }
    this.carritoService.agregarProducto(clienteId, this.comida().idProducto).subscribe({
      next: () => void this.router.navigate(['/carrito'], {
        queryParams: { id: clienteId }
      }),
      error: () => this.error = 'No fue posible agregar el producto al carrito.'
    });
  }
}
