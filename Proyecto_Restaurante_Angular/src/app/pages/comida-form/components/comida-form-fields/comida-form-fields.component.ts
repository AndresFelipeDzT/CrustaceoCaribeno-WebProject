import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Categoria } from '../../../../models/categoria.model';

@Component({
  selector: 'app-comida-form-fields',
  imports: [ReactiveFormsModule],
  templateUrl: './comida-form-fields.component.html',
  styleUrl: './comida-form-fields.component.scss'
})
/** Contiene los controles reutilizables del formulario de platos. */
export class ComidaFormFieldsComponent {
  @Input({ required: true }) comidaForm!: FormGroup;
  @Input({ required: true }) categorias: Categoria[] = [];
  @Input() esEdicion = false;
  @Output() guardar = new EventEmitter<void>();
}
