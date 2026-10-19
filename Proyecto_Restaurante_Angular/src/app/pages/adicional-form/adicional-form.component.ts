import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Adicional, AdicionalRequest } from '../../models/adicional.model';
import { Categoria } from '../../models/categoria.model';
import { ComidaService } from '../../service/comida.service';

@Component({
  selector: 'app-adicional-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './adicional-form.component.html',
  styleUrl: './adicional-form.component.scss'
})
/** Crea o edita adicionales y sus categorías compatibles. */
export class AdicionalFormComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private comidaService = inject(ComidaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  categorias: Categoria[] = [];
  adicionalId: number | undefined;
  error = '';
  categoriasError = '';

  adicionalForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    precio: [0, [Validators.required, Validators.min(0)]],
    categoriaIds: this.formBuilder.nonNullable.control<number[]>([], [
      Validators.required,
      control => Array.isArray(control.value) && control.value.length > 0 ? null : { required: true }
    ])
  });

  get esEdicion(): boolean {
    return this.adicionalId !== undefined;
  }

  get administradorId(): number | undefined {
    const id = Number(this.route.snapshot.queryParamMap.get('idAdministrador'));
    return Number.isInteger(id) && id > 0 ? id : undefined;
  }

  ngOnInit(): void {
    this.comidaService.getCategorias().subscribe({
      next: categorias => this.categorias = categorias,
      error: () => this.categoriasError = 'No fue posible cargar las categorías.'
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;

    this.adicionalId = Number(idParam);
    this.comidaService.getAdicionalById(this.adicionalId).subscribe({
      next: adicional => this.cargarFormulario(adicional),
      error: () => this.error = 'No fue posible cargar el adicional.'
    });
  }

  cambiarCategoria(categoriaId: number, event: Event): void {
    const input = event.target;
    if (!(input instanceof HTMLInputElement)) return;

    const categoriaIds = this.adicionalForm.controls.categoriaIds.value;
    const nuevosIds = input.checked
      ? [...categoriaIds, categoriaId]
      : categoriaIds.filter(id => id !== categoriaId);
    this.adicionalForm.controls.categoriaIds.setValue(nuevosIds);
    this.adicionalForm.controls.categoriaIds.markAsTouched();
  }

  guardar(): void {
    if (this.adicionalForm.invalid) {
      this.adicionalForm.markAllAsTouched();
      return;
    }

    const adicional: AdicionalRequest = this.adicionalForm.getRawValue();
    this.error = '';
    if (this.adicionalId === undefined) {
      this.comidaService.addAdicional(adicional).subscribe({
        next: () => void this.router.navigate(['/adicionales/tabla'], { queryParams: { idAdministrador: this.administradorId } }),
        error: error => this.error = this.mensajeError(error, 'No fue posible guardar el adicional.')
      });
    } else {
      this.comidaService.updateAdicional(this.adicionalId, adicional).subscribe({
        next: () => void this.router.navigate(['/adicionales/tabla'], { queryParams: { idAdministrador: this.administradorId } }),
        error: error => this.error = this.mensajeError(error, 'No fue posible actualizar el adicional.')
      });
    }
  }

  private mensajeError(error: unknown, mensajePredeterminado: string): string {
    if (error instanceof HttpErrorResponse) {
      const mensajeApi: unknown = error.error?.message;
      if (typeof mensajeApi === 'string' && mensajeApi.trim()) return mensajeApi;
    }
    return mensajePredeterminado;
  }

  private cargarFormulario(adicional: Adicional): void {
    this.adicionalForm.patchValue({
      nombre: adicional.nombre,
      precio: adicional.precio,
      categoriaIds: adicional.categoriaIds
    });
  }
}
