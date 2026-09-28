import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-comida-form-fields',
  imports: [ReactiveFormsModule],
  templateUrl: './comida-form-fields.component.html',
  styleUrl: './comida-form-fields.component.scss'
})
export class ComidaFormFieldsComponent {
  @Input({ required: true }) comidaForm!: FormGroup;
  @Input({ required: true }) categorias: string[] = [];
  @Input() esEdicion = false;
  @Output() guardar = new EventEmitter<void>();
}
