import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Adicional } from '../../models/adicional.model';
import { Carrito } from '../../models/carrito.model';
import { Cupon, CuponAplicacion } from '../../models/cupon.model';
import { ItemCarrito } from '../../models/item-carrito.model';
import { CarritoService } from '../../service/carrito.service';
import { ClienteService } from '../../service/cliente.service';
import { ComidaService } from '../../service/comida.service';
import { CarritoItemComponent } from './components/carrito-item/carrito-item.component';

@Component({
  selector: 'app-carrito-page',
  imports: [CurrencyPipe, FormsModule, RouterLink, CarritoItemComponent],
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
  cuponDisponible: Cupon | null = null;
  codigoCupon = '';
  cotizacionCupon?: CuponAplicacion;
  get clienteId(): number | undefined { return this.clienteService.getClienteActualId(); }

  ngOnInit(): void {
    this.cargarCarrito();
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId !== undefined) this.cargarCuponDisponible(clienteId);
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
        if (this.codigoCupon.trim() && this.cotizacionCupon) {
          this.revalidarCupon();
        }
        for (const item of carrito.items) {
          this.seleccionados[item.idItemCarrito] = item.adicionales.map(adicional => adicional.idAdicional);
          const categoriaId = item.producto.categoria?.idCategoria;
          if (categoriaId !== undefined && !this.disponibles[item.idItemCarrito]) {
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
    const nuevos = seleccionado
      ? [...actuales, adicionalId]
      : actuales.filter(id => id !== adicionalId);
    this.seleccionados[item.idItemCarrito] = nuevos;

    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;

    this.carritoService.actualizarAdicionales(clienteId, item.idItemCarrito, nuevos).subscribe({
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
    this.carritoService.confirmarPedido(clienteId, this.codigoCupon.trim() || undefined).subscribe({
      next: () => {
        this.mensaje = 'Pedido confirmado correctamente. Gracias por tu compra.';
        this.codigoCupon = '';
        this.cotizacionCupon = undefined;
        this.cargarCuponDisponible(clienteId);
        this.cargarCarrito();
      },
      error: err => this.error = err?.error?.message || 'No fue posible confirmar el pedido.'
    });
  }

  aplicarCupon(codigo: string = this.codigoCupon): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;
    this.codigoCupon = codigo.trim();
    this.error = '';
    this.carritoService.validarCupon(clienteId, this.codigoCupon).subscribe({
      next: cotizacion => {
        this.cotizacionCupon = cotizacion;
        this.codigoCupon = cotizacion.codigoCupon;
      },
      error: err => {
        this.cotizacionCupon = undefined;
        this.error = err?.error?.message || 'No fue posible validar el código de descuento.';
      }
    });
  }

  quitarCupon(): void {
    this.codigoCupon = '';
    this.cotizacionCupon = undefined;
  }

  private cargarCuponDisponible(idCliente: number): void {
    this.carritoService.getCuponDisponible(idCliente).subscribe({
      next: cupon => this.cuponDisponible = cupon,
      error: () => this.error = 'No fue posible consultar tu código de descuento.'
    });
  }

  private revalidarCupon(): void {
    const clienteId = this.clienteService.getClienteActualId();
    if (clienteId === undefined) return;
    this.carritoService.validarCupon(clienteId, this.codigoCupon).subscribe({
      next: cotizacion => this.cotizacionCupon = cotizacion,
      error: err => {
        this.cotizacionCupon = undefined;
        this.error = err?.error?.message || 'No fue posible recalcular el descuento.';
      }
    });
  }
}
