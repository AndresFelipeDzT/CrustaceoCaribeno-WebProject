import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TableNavbarComponent } from '../../components/navbar/table-navbar/table-navbar.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../service/cliente.service';

@Component({
  selector: 'app-cliente-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, TableNavbarComponent, FooterComponent],
  templateUrl: './cliente-detail.component.html',
  styleUrl: './cliente-detail.component.scss'
})
export class ClienteDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private clienteService = inject(ClienteService);
  cliente: Cliente | undefined;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cliente = this.clienteService.getClienteById(id);
  }
}
