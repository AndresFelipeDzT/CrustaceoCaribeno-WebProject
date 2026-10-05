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
export class ClienteProfileComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  cliente: Cliente | undefined;
  editando = false;
  guardado = false;

  perfilForm = this.formBuilder.nonNullable.group({
    correo: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(15), Validators.pattern(/^[0-9+\- ]+$/)]],
    direccion: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  ngOnInit(): void {
    this.cliente = this.clienteService.getClienteActual();
    if (!this.cliente) void this.router.navigate(['/login']);
  }

  iniciarEdicion(): void {
    if (!this.cliente) return;
    this.guardado = false;
    this.editando = true;
    this.perfilForm.setValue({
      correo: this.cliente.correo,
      telefono: this.cliente.telefono ?? '',
      direccion: this.cliente.direccion ?? '',
      password: this.cliente.password
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
    this.clienteService.updateCliente(this.cliente.idCliente, {
      nombre: this.cliente.nombre,
      apellido: this.cliente.apellido,
      correo: datos.correo.trim(),
      telefono: datos.telefono.trim(),
      direccion: datos.direccion.trim(),
      password: datos.password,
      activo: this.cliente.activo
    });
    this.cliente = this.clienteService.getClienteById(this.cliente.idCliente);
    this.editando = false;
    this.guardado = true;
  }

  cerrarSesion(): void {
    this.clienteService.cerrarSesion();
    void this.router.navigate(['/']);
  }

  borrarCuenta(): void {
    if (!this.cliente) return;
    this.clienteService.deleteCliente(this.cliente.idCliente);
    this.clienteService.cerrarSesion();
    void this.router.navigate(['/']);
  }
}
