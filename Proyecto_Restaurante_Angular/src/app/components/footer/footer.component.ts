import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../service/cliente.service';

@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
/** Pie de página público; conserva el cliente activo en sus enlaces. */
export class FooterComponent {
  private clienteService = inject(ClienteService);
  get clienteId(): number | undefined { return this.clienteService.getClienteActualId(); }
}
