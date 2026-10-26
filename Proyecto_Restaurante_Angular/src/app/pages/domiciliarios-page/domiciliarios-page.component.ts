import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Domiciliario } from '../../models/domiciliario.model';
import { DomiciliarioService } from '../../service/domiciliario.service';
import { PedidoService } from '../../service/pedido.service';

@Component({
  selector: 'app-domiciliarios-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './domiciliarios-page.component.html',
  styleUrl: './domiciliarios-page.component.scss'
})
/** Lista domiciliarios y permite gestionar su disponibilidad. */
export class DomiciliariosPageComponent implements OnInit {
  private domiciliarioService = inject(DomiciliarioService);
  private pedidoService = inject(PedidoService);
  private router = inject(Router);
  domiciliarios: Domiciliario[] = [];
  domiciliarioSeleccionado?: Domiciliario;
  error = '';
  mensaje = '';

  get administradorId(): number | undefined {
    const idParam = this.router.parseUrl(this.router.url).queryParams['idAdministrador'];
    if (typeof idParam !== 'string') return undefined;
    const id = Number(idParam);
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  ngOnInit(): void {
    this.cargarDomiciliarios();
  }

  cargarDomiciliarios(): void {
    this.error = '';
    this.domiciliarioService.getDomiciliarios().subscribe({
      next: domiciliarios => {
        this.domiciliarios = domiciliarios;
        this.domiciliarioSeleccionado = domiciliarios.find(item =>
          item.idDomiciliario === this.domiciliarioSeleccionado?.idDomiciliario
        ) ?? domiciliarios[0];
      },
      error: () => this.error = 'No fue posible cargar los domiciliarios.'
    });
  }

  seleccionarDomiciliario(domiciliario: Domiciliario): void {
    this.domiciliarioSeleccionado = domiciliario;
    this.mensaje = '';
  }

  cambiarEstadoActivo(domiciliario: Domiciliario): void {
    this.error = '';
    this.mensaje = '';
    this.domiciliarioService.cambiarEstadoActivo(
      domiciliario,
      !domiciliario.activo
    ).subscribe({
      next: actualizado => {
        this.domiciliarios = this.domiciliarios.map(item =>
          item.idDomiciliario === actualizado.idDomiciliario ? actualizado : item
        );
        this.domiciliarioSeleccionado = actualizado;
        this.mensaje = actualizado.activo
          ? 'Domiciliario activado correctamente.'
          : 'Domiciliario desactivado correctamente.';
      },
      error: err => this.error = err?.error?.message || 'No fue posible cambiar el estado del domiciliario.'
    });
  }

  cambiarDisponibilidad(domiciliario: Domiciliario): void {
    this.error = '';
    this.mensaje = '';

    if (!domiciliario.activo) {
      this.error = 'Activa el domiciliario antes de cambiar su disponibilidad.';
      return;
    }

    if (!domiciliario.disponible) {
      this.pedidoService.getPedidos().subscribe({
        next: pedidos => {
          const estaEnReparto = pedidos.some(pedido =>
            pedido.idDomiciliario === domiciliario.idDomiciliario &&
            ['enviado', 'en camino'].includes(pedido.estado.trim().toLowerCase())
          );
          if (estaEnReparto) {
            this.error = 'No se puede activar un domiciliario mientras está entregando un pedido.';
            return;
          }
          this.guardarDisponibilidad(domiciliario, true);
        },
        error: () => this.error = 'No fue posible verificar si el domiciliario tiene un pedido en reparto.'
      });
      return;
    }

    this.guardarDisponibilidad(domiciliario, false);
  }

  private guardarDisponibilidad(domiciliario: Domiciliario, disponible: boolean): void {
    this.domiciliarioService.cambiarDisponibilidad(domiciliario, disponible).subscribe({
      next: actualizado => {
        this.domiciliarios = this.domiciliarios.map(item =>
          item.idDomiciliario === actualizado.idDomiciliario ? actualizado : item
        );
        this.domiciliarioSeleccionado = actualizado;
        this.mensaje = 'Estado del domiciliario actualizado correctamente.';
      },
      error: () => this.error = 'No fue posible cambiar el estado del domiciliario.'
    });
  }

  eliminarDomiciliario(domiciliario: Domiciliario): void {
    this.error = '';
    this.domiciliarioService.deleteDomiciliario(domiciliario.idDomiciliario).subscribe({
      next: () => {
        this.domiciliarioSeleccionado = undefined;
        this.mensaje = 'Domiciliario eliminado correctamente.';
        this.cargarDomiciliarios();
      },
      error: () => this.error = 'No fue posible eliminar el domiciliario.'
    });
  }

}
