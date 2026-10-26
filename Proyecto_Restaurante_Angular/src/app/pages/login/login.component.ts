import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ClienteService } from '../../service/cliente.service';
import { AdministradorService } from '../../service/administrador.service';
import { OperadorService } from '../../service/operador.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
/** Reutiliza el formulario de acceso para clientes, administradores y operadores. */
export class LoginComponent {
  private formBuilder = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private administradorService = inject(AdministradorService);
  private operadorService = inject(OperadorService);
  private router = inject(Router);
  error = '';
  readonly esAdministrador = this.router.url.split('?')[0] === '/admin';
  readonly esOperador = this.router.url.split('?')[0] === '/operador';

  loginForm = this.formBuilder.nonNullable.group({
    correo: ['', [Validators.required, ...(!this.esAdministrador && !this.esOperador ? [Validators.email] : [])]],
    password: ['', [Validators.required]]
  });

  ingresar(): void {
    this.error = '';
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { correo, password } = this.loginForm.getRawValue();
    // Los operadores se autentican con usuario y solo acceden a pedidos pendientes.
    if (this.esOperador) {
      this.operadorService.iniciarSesion(correo, password).subscribe({
        next: operador => {
          void this.router.navigate(['/operador/pedidos'], { queryParams: { idOperador: operador.idOperador } });
        },
        error: err => this.error = err?.error?.message || 'No fue posible conectar con el servidor. Intenta de nuevo.'
      });
      return;
    }
    // Los administradores son dirigidos al portal completo de gestión.
    if (this.esAdministrador) {
      this.administradorService.iniciarSesion(correo, password).subscribe({
        next: administrador => {
          void this.router.navigate(['/pedidos'], {
            queryParams: { idAdministrador: administrador.idAdministrador }
          });
        },
        error: err => this.error = err?.error?.message || 'No fue posible conectar con el servidor. Intenta de nuevo.'
      });
      return;
    }

    this.clienteService.iniciarSesion(correo, password).subscribe({
      next: cliente => {
        void this.router.navigate(['/comidas/tarjetas'], {
          queryParams: { id: cliente.idCliente }
        });
      },
      error: err => {
        this.error = err?.error?.message || 'No fue posible conectar con el servidor. Intenta de nuevo.';
      }
    });
  }
}
