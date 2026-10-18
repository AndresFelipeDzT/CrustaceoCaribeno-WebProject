import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ClienteService } from '../../service/cliente.service';
import { AdministradorService } from '../../service/administrador.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private formBuilder = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private administradorService = inject(AdministradorService);
  private router = inject(Router);
  error = '';
  readonly esAdministrador = this.router.url.split('?')[0] === '/admin';

  loginForm = this.formBuilder.nonNullable.group({
    correo: ['', [Validators.required, ...(this.esAdministrador ? [] : [Validators.email])]],
    password: ['', [Validators.required]]
  });

  ingresar(): void {
    this.error = '';
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { correo, password } = this.loginForm.getRawValue();
    if (this.esAdministrador) {
      this.administradorService.iniciarSesion(correo, password).subscribe({
        next: administrador => {
          if (!administrador) {
            this.error = 'El usuario o la contraseña no son correctos.';
            return;
          }
          void this.router.navigate(['/pedidos'], {
            queryParams: { idAdministrador: administrador.idAdministrador }
          });
        },
        error: () => this.error = 'No fue posible conectar con el servidor. Intenta de nuevo.'
      });
      return;
    }

    this.clienteService.iniciarSesion(correo, password).subscribe({
      next: cliente => {
        if (!cliente) {
          this.error = 'El correo o la contraseña no son correctos.';
          return;
        }
        void this.router.navigate(['/comidas/tarjetas'], {
          queryParams: { id: cliente.idCliente }
        });
      },
      error: () => {
        this.error = 'No fue posible conectar con el servidor. Intenta de nuevo.';
      }
    });
  }
}
