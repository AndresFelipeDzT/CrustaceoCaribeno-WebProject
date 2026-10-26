import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Params, Router, RouterLink } from '@angular/router';
import { Domiciliario } from '../../models/domiciliario.model';
import { DomiciliarioService } from '../../service/domiciliario.service';

@Component({
  selector: 'app-domiciliario-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './domiciliario-form.component.html',
  styleUrl: './domiciliario-form.component.scss'
})
/** Crea o edita domiciliarios y conserva el contexto administrativo. */
export class DomiciliarioFormComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private domiciliarioService = inject(DomiciliarioService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  domiciliarioId?: number;
  disponible = true;
  isEdit = false;
  error = '';

  get queryParams(): Params {
    const idParam = this.route.snapshot.queryParamMap.get('idAdministrador');
    const id = Number(idParam);
    return Number.isInteger(id) && id > 0 ? { idAdministrador: id } : {};
  }

  domiciliarioForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    celular: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(15), Validators.pattern(/^[0-9+\- ]+$/)]],
    cedula: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(20), Validators.pattern(/^[0-9]+$/)]],
    imagenURL: ['', [
      Validators.required,
      Validators.maxLength(2048),
      Validators.pattern(/^(https?:\/\/|\/)[^\s]+$/i)
    ]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;

    this.domiciliarioId = Number(idParam);
    this.domiciliarioService.getDomiciliarioById(this.domiciliarioId).subscribe({
      next: domiciliario => {
        this.isEdit = true;
        this.disponible = domiciliario.disponible;
        this.domiciliarioForm.setValue({
          nombre: domiciliario.nombre,
          celular: domiciliario.celular,
          cedula: domiciliario.cedula,
          imagenURL: domiciliario.imagenURL ?? ''
        });
      },
      error: () => this.error = 'No fue posible cargar los datos del domiciliario.'
    });
  }

  guardar(): void {
    if (this.domiciliarioForm.invalid) {
      this.domiciliarioForm.markAllAsTouched();
      return;
    }

    const datos = this.domiciliarioForm.getRawValue();
    const datosDomiciliario = {
      nombre: datos.nombre.trim(),
      celular: datos.celular.trim(),
      cedula: datos.cedula.trim(),
      imagenURL: datos.imagenURL.trim(),
      disponible: this.disponible
    };
    this.error = '';

    const solicitud = this.isEdit && this.domiciliarioId !== undefined
      ? this.domiciliarioService.updateDomiciliario(this.domiciliarioId, {
        idDomiciliario: this.domiciliarioId,
        ...datosDomiciliario
      })
      : this.domiciliarioService.addDomiciliario(datosDomiciliario);
    solicitud.subscribe({
      next: () => void this.router.navigate(['/domiciliarios'], {
        queryParams: this.queryParams
      }),
      error: err => this.error = err?.error?.message || (this.isEdit
        ? 'No fue posible actualizar el domiciliario.'
        : 'No fue posible registrar el domiciliario.')
    });
  }
}
