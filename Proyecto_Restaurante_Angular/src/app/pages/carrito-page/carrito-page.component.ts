import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Adicional } from '../../models/adicional.model';
import { Carrito } from '../../models/carrito.model';
import { ItemCarrito } from '../../models/item-carrito.model';
import { CarritoService } from '../../service/carrito.service';
import { ClienteService } from '../../service/cliente.service';
import { ComidaService } from '../../service/comida.service';
import { CarritoItemComponent } from './components/carrito-item/carrito-item.component';

@Component({
  selector: 'app-carrito-page',
  imports: [CurrencyPipe, RouterLink, CarritoItemComponent],
  templateUrl: './carrito-page.component.html',
  styleUrl: './carrito-page.component.css'
})
/** Gestiona el carrito del cliente, incluidos sus adicionales seleccionados. */
export class CarritoPageComponent implements OnInit {
  private carritoService = inject(CarritoService);
  private clienteService = inject(ClienteService);
  private comidaService = inject(ComidaService);
  carrito?: Carrito;
  disponibles: { [idItemCarrito: number]: Adicional[] | undefined } = {};
  seleccionados: { [idItemCarrito: number]: number[] | undefined } = {};
  error = '';
  mensaje = '';
  get clienteId(): number | undefined { return this.clienteService.getClienteActualId(); }

  ngOnInit(): void {
    this.cargarCarrito();
  }

  cargarCarrito(): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) {
      this.error = 'Inicia sesión para consultar el carrito.';
      return;
    }
    this.carritoService.getCarrito(clienteId).subscribe({
      next: carrito => {
        this.carrito = carrito;
        for (const item of carrito.items) {
          this.seleccionados[item.idItemCarrito] = item.adicionales.map(adicional => adicional.idAdicional);
          const categoriaId = item.producto.categoria?.idCategoria;
          if (categoriaId !== undefined) {
            this.comidaService.getAdicionales(categoriaId).subscribe({
              next: adicionales => this.disponibles[item.idItemCarrito] = adicionales.filter(adicional => adicional.activo),
              error: () => this.error = 'No fue posible cargar los adicionales del carrito.'
            });
          }
        }
      },
      error: () => this.error = 'No fue posible cargar el carrito.'
    });
  }

  actualizarCantidad(item: ItemCarrito, cantidad: number): void {
    if (cantidad < 1) return;
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;
    this.carritoService.actualizarCantidad(clienteId, item.idItemCarrito, cantidad).subscribe({
      next: () => this.cargarCarrito(),
      error: err => this.error = err?.error?.message || 'No fue posible actualizar la cantidad.'
    });
  }

  cambiarAdicional(item: ItemCarrito, adicionalId: number, event: Event): void {
    const seleccionado = (event.target as HTMLInputElement).checked;
    const actuales = this.seleccionados[item.idItemCarrito] ?? [];
    this.seleccionados[item.idItemCarrito] = seleccionado
      ? [...actuales, adicionalId]
      : actuales.filter(id => id !== adicionalId);
  }

  guardarAdicionales(item: ItemCarrito): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;
    this.carritoService.actualizarAdicionales(
      clienteId,
      item.idItemCarrito,
      this.seleccionados[item.idItemCarrito] ?? []
    ).subscribe({
      next: () => this.cargarCarrito(),
      error: err => this.error = err?.error?.message || 'No fue posible actualizar los adicionales.'
    });
  }

  eliminarItem(item: ItemCarrito): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;
    this.carritoService.eliminarItem(clienteId, item.idItemCarrito).subscribe({
      next: () => this.cargarCarrito(),
      error: err => this.error = err?.error?.message || 'No fue posible eliminar el producto del carrito.'
    });
  }

  confirmarPedido(): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;
    this.carritoService.confirmarPedido(clienteId).subscribe({
      next: () => {
        this.mensaje = 'Pedido confirmado correctamente.';
        this.cargarCarrito();
      },
      error: err => this.error = err?.error?.message || 'No fue posible confirmar el pedido.'
    });
  }
}
