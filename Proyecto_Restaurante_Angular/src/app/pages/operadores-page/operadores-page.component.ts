import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Operador } from '../../models/operador.model';
import { OperadorService } from '../../service/operador.service';

@Component({
  selector: 'app-operadores-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './operadores-page.component.html',
  styleUrl: './operadores-page.component.scss'
})
/** Lista, selecciona y elimina cuentas de operador. */
export class OperadoresPageComponent implements OnInit {
  private operadorService = inject(OperadorService);
  private router = inject(Router);
  operadores: Operador[] = [];
  operadorSeleccionado?: Operador;
  error = '';
  mensaje = '';

  get administradorId(): number | undefined {
    const idParam = this.router.parseUrl(this.router.url).queryParams['idAdministrador'];
    if (typeof idParam !== 'string') return undefined;
    const id = Number(idParam);
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  ngOnInit(): void { this.cargarOperadores(); }

  cargarOperadores(): void {
    this.error = '';
    this.operadorService.getOperadores().subscribe({
      next: operadores => {
        this.operadores = operadores;
        this.operadorSeleccionado = operadores.find(item => item.idOperador === this.operadorSeleccionado?.idOperador) ?? operadores[0];
      },
      error: () => this.error = 'No fue posible cargar los operadores.'
    });
  }

  seleccionarOperador(operador: Operador): void {
    this.operadorSeleccionado = operador;
    this.mensaje = '';
  }

  eliminarOperador(operador: Operador): void {
    this.error = '';
    this.operadorService.deleteOperador(operador.idOperador).subscribe({
      next: () => {
        this.operadorSeleccionado = undefined;
        this.mensaje = 'Operador eliminado correctamente.';
        this.cargarOperadores();
      },
      error: () => this.error = 'No fue posible eliminar el operador.'
    });
  }
}
