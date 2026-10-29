import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { Cliente } from '../../models/cliente.model';
import { Pedido } from '../../models/pedido.model';
import { ClienteService } from '../../service/cliente.service';
import { PedidoService } from '../../service/pedido.service';

@Component({
  selector: 'app-cliente-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './cliente-profile.component.html',
  styleUrl: './cliente-profile.component.scss'
})
/** Presenta el perfil del cliente identificado en la URL. */
export class ClienteProfileComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private pedidoService = inject(PedidoService);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  cliente: Cliente | undefined;
  pedidos: Pedido[] = [];
  pedidoDetalle?: Pedido;
  pedidoDetalleId?: number;
  cargandoPedidos = true;
  cargandoDetalle = false;
  errorPedidos = '';
  errorDetalle = '';
  editando = false;
  guardado = false;
  error = '';

  perfilForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    apellido: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    correo: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(15), Validators.pattern(/^[0-9+\- ]+$/)]],
    direccion: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    password: ['', [Validators.minLength(4)]]
  });

  get pedidosActivos(): Pedido[] {
    return this.pedidos.filter(pedido => !this.esPedidoPasado(pedido));
  }

  get pedidosPasados(): Pedido[] {
    return this.pedidos.filter(pedido => this.esPedidoPasado(pedido));
  }

  ngOnInit(): void {
    this.clienteService.getClienteActual().subscribe({
      next: cliente => {
        this.cliente = cliente;
        if (!cliente) void this.router.navigate(['/login']);
        else this.cargarPedidos(cliente.idCliente);
      },
      error: () => this.error = 'No fue posible cargar tu perfil.'
    });
  }

  iniciarEdicion(): void {
    if (!this.cliente) return;
    this.guardado = false;
    this.editando = true;
    this.perfilForm.setValue({
      nombre: this.cliente.nombre,
      apellido: this.cliente.apellido,
      correo: this.cliente.correo,
      telefono: this.cliente.telefono ?? '',
      direccion: this.cliente.direccion ?? '',
      password: ''
    });
  }

  cancelarEdicion(): void {
    this.editando = false;
    this.perfilForm.reset();
  }

  guardarCambios(): void {
    if (!this.cliente) return;
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      return;
    }

    const datos = this.perfilForm.getRawValue();
    this.error = '';
    this.clienteService.updateCliente(this.cliente.idCliente, {
      nombre: datos.nombre.trim(),
      apellido: datos.apellido.trim(),
      correo: datos.correo.trim(),
      telefono: datos.telefono.trim(),
      direccion: datos.direccion.trim(),
      password: datos.password || this.cliente.password,
      activo: this.cliente.activo
    }).subscribe({
      next: () => this.clienteService.getClienteById(this.cliente!.idCliente).subscribe({
        next: cliente => {
          this.cliente = cliente;
          this.editando = false;
          this.guardado = true;
        },
        error: err => this.error = err?.error?.message || 'Los cambios se guardaron, pero no fue posible recargar el perfil.'
      }),
      error: err => this.error = err?.error?.message || 'No fue posible guardar los cambios.'
    });
  }

  verDetallePedido(idPedido: number): void {
    if (this.pedidoDetalleId === idPedido) {
      this.pedidoDetalleId = undefined;
      this.pedidoDetalle = undefined;
      this.errorDetalle = '';
      return;
    }

    this.pedidoDetalleId = idPedido;
    this.errorDetalle = '';
    this.pedidoDetalle = undefined;
    const detalleExistente = this.pedidos.find(pedido => pedido.idPedido === idPedido);
    if (detalleExistente?.items) {
      this.pedidoDetalle = detalleExistente;
      this.cargandoDetalle = false;
      return;
    }

    this.cargandoDetalle = true;
    this.pedidoService.getPedidoById(idPedido).subscribe({
      next: pedido => {
        if (this.pedidoDetalleId === idPedido) {
          this.pedidoDetalle = pedido;
          this.cargandoDetalle = false;
        }
      },
      error: () => {
        if (this.pedidoDetalleId === idPedido) {
          this.cargandoDetalle = false;
          this.errorDetalle = 'No fue posible cargar el detalle de este pedido.';
        }
      }
    });
  }

  private cargarPedidos(idCliente: number): void {
    this.cargandoPedidos = true;
    this.pedidoService.getPedidos(false, idCliente).subscribe({
      next: pedidos => {
        this.pedidos = pedidos.sort((a, b) => b.idPedido - a.idPedido);
        this.cargandoPedidos = false;
      },
      error: () => {
        this.errorPedidos = 'No fue posible cargar tus pedidos.';
        this.cargandoPedidos = false;
      }
    });
  }

  private esPedidoPasado(pedido: Pedido): boolean {
    return ['entregado', 'cancelado'].includes(pedido.estado.trim().toLowerCase());
  }

  cerrarSesion(): void {
    void this.router.navigate(['/']);
  }

  borrarCuenta(): void {
    if (!this.cliente) return;
    this.clienteService.deleteCliente(this.cliente.idCliente).subscribe({
      next: () => {
        void this.router.navigate(['/']);
      },
      error: err => this.error = err?.error?.message || 'No fue posible desactivar la cuenta.'
    });
  }
}
