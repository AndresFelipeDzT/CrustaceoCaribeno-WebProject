import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../service/cliente.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private clienteService = inject(ClienteService);
  get clienteId(): number | undefined { return this.clienteService.getClienteActualId(); }
  get conectado(): boolean { return this.clienteId !== undefined; }
}
