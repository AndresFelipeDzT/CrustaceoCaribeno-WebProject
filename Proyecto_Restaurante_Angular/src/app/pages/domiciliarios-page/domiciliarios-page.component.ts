import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Domiciliario } from '../../models/domiciliario.model';
import { DomiciliarioService } from '../../service/domiciliario.service';

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

  cambiarDisponibilidad(domiciliario: Domiciliario): void {
    this.error = '';
    this.mensaje = '';
    const cambio = this.domiciliarioService.cambiarDisponibilidad(domiciliario, !domiciliario.disponible);
    cambio.subscribe({
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
