import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ClienteService } from '../../service/cliente.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private clienteService = inject(ClienteService);
  private router = inject(Router);
  get conectado(): boolean { return !!this.clienteService.getClienteActual(); }

  cerrarSesion(): void {
    this.clienteService.cerrarSesion();
    void this.router.navigate(['/']);
  }
}
