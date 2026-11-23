import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { LandingFooterComponent } from '../../components/footer/landing-footer/landing-footer.component';
import { Cliente } from '../../models/cliente.model';
import { Pedido } from '../../models/pedido.model';
import { EstadoPedido } from '../../models/estado-pedido.model';
import { AuthService } from '../../service/auth.service';
import { ClienteService } from '../../service/cliente.service';
import { PedidoService } from '../../service/pedido.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { EMPTY, timer } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-cliente-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NavbarComponent, LandingFooterComponent],
  templateUrl: './cliente-profile.component.html',
  styleUrl: './cliente-profile.component.scss'
})
/** Presenta el perfil del cliente autenticado. */
export class ClienteProfileComponent implements OnInit {
  private auth = inject(AuthService);
  private clienteService = inject(ClienteService);
  private pedidoService = inject(PedidoService);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  private destroyRef = inject(DestroyRef);
  cliente: Cliente | undefined;
  pedidos: Pedido[] = [];
  pedidoDetalle?: Pedido;
  pedidoDetalleId?: number;
  historiales: Partial<Record<number, EstadoPedido[]>> = {};
  cargandoHistorial: Record<number, boolean> = {};
  errorHistorial: Record<number, string> = {};
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
        else this.observarPedidos(cliente.idCliente);
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
    const correo = datos.correo.trim();
    const cambioCorreo = correo !== this.cliente.correo;
    this.error = '';
    this.clienteService.updateCliente(this.cliente.idCliente, {
      nombre: datos.nombre.trim(),
      apellido: datos.apellido.trim(),
      correo,
      telefono: datos.telefono.trim(),
      direccion: datos.direccion.trim(),
      password: datos.password,
      activo: this.cliente.activo
    }).subscribe({
      next: () => {
        if (cambioCorreo) {
          this.auth.cerrarSesion();
          void this.router.navigate(['/login']);
          return;
        }
        this.clienteService.getClienteActual().subscribe({
          next: cliente => {
            this.cliente = cliente;
            this.editando = false;
            this.guardado = true;
          },
          error: err => this.error = err?.error?.message || 'Los cambios se guardaron, pero no fue posible recargar el perfil.'
        });
      },
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
    this.errorHistorial[idPedido] = '';
    this.pedidoDetalle = undefined;
    this.cargarHistorial(idPedido);
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

  private observarPedidos(idCliente: number): void {
    timer(0, 10000).pipe(
      switchMap(() => this.pedidoService.getPedidos(false, idCliente).pipe(
        catchError(() => {
          this.errorPedidos = 'No fue posible actualizar tus pedidos. Se volverá a intentar.';
          this.cargandoPedidos = false;
          return EMPTY;
        })
      )),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(pedidos => {
      this.errorPedidos = '';
      this.pedidos = pedidos.sort((a, b) => b.idPedido - a.idPedido);
      this.cargandoPedidos = false;
      if (this.pedidoDetalleId !== undefined) {
        this.cargarHistorial(this.pedidoDetalleId, true);
      }
    });
  }

  private cargarHistorial(idPedido: number, actualizar = false): void {
    if (!actualizar && this.historiales[idPedido]) return;
    this.cargandoHistorial[idPedido] = true;
    this.pedidoService.getHistorialEstados(idPedido).subscribe({
      next: historial => {
        this.historiales[idPedido] = historial;
        this.cargandoHistorial[idPedido] = false;
      },
      error: () => {
        this.errorHistorial[idPedido] = 'No fue posible cargar el seguimiento del pedido.';
        this.cargandoHistorial[idPedido] = false;
      }
    });
  }

  private esPedidoPasado(pedido: Pedido): boolean {
    return ['entregado', 'cancelado'].includes(pedido.estado.trim().toLowerCase());
  }

  cerrarSesion(): void {
    void this.router.navigate(['/cerrar-sesion']);
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
