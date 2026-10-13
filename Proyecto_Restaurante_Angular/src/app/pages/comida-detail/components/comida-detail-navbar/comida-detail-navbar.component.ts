import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../../../service/cliente.service';

@Component({
  selector: 'app-comida-detail-navbar',
  imports: [RouterLink],
  templateUrl: './comida-detail-navbar.component.html',
  styleUrl: './comida-detail-navbar.component.scss'
})
export class ComidaDetailNavbarComponent {
  private clienteService = inject(ClienteService);
  get clienteId(): number | undefined { return this.clienteService.getClienteActualId(); }
}
