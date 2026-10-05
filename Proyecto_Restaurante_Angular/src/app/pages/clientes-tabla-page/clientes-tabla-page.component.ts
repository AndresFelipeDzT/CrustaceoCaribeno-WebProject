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

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clientes = [...this.clienteService.getClientes()];
  }

  eliminarCliente(id: number): void {
    this.clienteService.deleteCliente(id);
    this.cargarClientes();
  }

  activarCliente(id: number): void {
    this.clienteService.activarCliente(id);
    this.cargarClientes();
  }
}
