import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../service/cliente.service';

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
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  cliente: Cliente | undefined;
  editando = false;
  guardado = false;
  error = '';

  perfilForm = this.formBuilder.nonNullable.group({
    correo: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(15), Validators.pattern(/^[0-9+\- ]+$/)]],
    direccion: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  ngOnInit(): void {
    this.clienteService.getClienteActual().subscribe({
      next: cliente => {
        this.cliente = cliente;
        if (!cliente) void this.router.navigate(['/login']);
      },
      error: () => this.error = 'No fue posible cargar tu perfil.'
    });
  }

  iniciarEdicion(): void {
    if (!this.cliente) return;
    this.guardado = false;
    this.editando = true;
    this.perfilForm.setValue({
      correo: this.cliente.correo,
      telefono: this.cliente.telefono ?? '',
      direccion: this.cliente.direccion ?? '',
      password: this.cliente.password ?? ''
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
      nombre: this.cliente.nombre,
      apellido: this.cliente.apellido,
      correo: datos.correo.trim(),
      telefono: datos.telefono.trim(),
      direccion: datos.direccion.trim(),
      password: datos.password,
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
