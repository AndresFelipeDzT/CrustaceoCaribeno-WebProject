import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Operador } from '../../models/operador.model';
import { OperadorService } from '../../service/operador.service';

@Component({
  selector: 'app-operador-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './operador-form.component.html',
  styleUrl: './operador-form.component.scss'
})
/** Crea o edita operadores desde el portal administrativo. */
export class OperadorFormComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private operadorService = inject(OperadorService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  operadorId?: number;
  isEdit = false;
  error = '';

  operadorForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    usuario: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    contrasena: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(100)]],
    imagenURL: ['', [Validators.required, Validators.maxLength(2048), Validators.pattern(/^(https?:\/\/|\/)[^\s]+$/i)]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;
    this.operadorId = Number(idParam);
    this.isEdit = true;
    this.operadorForm.controls.contrasena.clearValidators();
    this.operadorForm.controls.contrasena.updateValueAndValidity();
    this.operadorService.getOperadorById(this.operadorId).subscribe({
      next: operador => {
        this.operadorForm.setValue({ nombre: operador.nombre, usuario: operador.usuario, contrasena: '', imagenURL: operador.imagenURL ?? '' });
      },
      error: () => this.error = 'No fue posible cargar los datos del operador.'
    });
  }

  guardar(): void {
    if (this.operadorForm.invalid) { this.operadorForm.markAllAsTouched(); return; }
    const datos = this.operadorForm.getRawValue();
    const payload = { nombre: datos.nombre.trim(), usuario: datos.usuario.trim(), contrasena: datos.contrasena, imagenURL: datos.imagenURL.trim() };
    this.error = '';
    const solicitud = this.isEdit && this.operadorId !== undefined
      ? this.operadorService.updateOperador(this.operadorId, { idOperador: this.operadorId, ...payload })
      : this.operadorService.addOperador(payload);
    solicitud.subscribe({
      next: () => void this.router.navigate(['/operadores']),
      error: err => this.error = err?.error?.message || (this.isEdit ? 'No fue posible actualizar el operador.' : 'No fue posible registrar el operador.')
    });
  }
}
