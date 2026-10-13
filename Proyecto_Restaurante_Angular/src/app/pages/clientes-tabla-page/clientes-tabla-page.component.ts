import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TableNavbarComponent } from '../../components/navbar/table-navbar/table-navbar.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../service/cliente.service';

@Component({
  selector: 'app-clientes-tabla-page',
  standalone: true,
  imports: [CommonModule, RouterLink, TableNavbarComponent, FooterComponent],
  templateUrl: './clientes-tabla-page.component.html',
  styleUrl: './clientes-tabla-page.component.scss'
})
export class ClientesTablaPageComponent implements OnInit {
  private clienteService = inject(ClienteService);
  clientes: Cliente[] = [];
  error = '';

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.error = '';
    this.clienteService.getClientes().subscribe({
      next: clientes => this.clientes = clientes,
      error: () => this.error = 'No fue posible cargar los clientes.'
    });
  }

  eliminarCliente(id: number): void {
    this.clienteService.deleteCliente(id).subscribe({
      next: () => this.cargarClientes(),
      error: () => this.error = 'No fue posible desactivar el cliente.'
    });
  }
}
