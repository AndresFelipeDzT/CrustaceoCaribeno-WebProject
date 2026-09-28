import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ComidaFormFieldsComponent } from '../comida-form-fields/comida-form-fields.component';

@Component({
  selector: 'app-comida-form-panel',
  imports: [ComidaFormFieldsComponent],
  templateUrl: './comida-form-panel.component.html',
  styleUrl: './comida-form-panel.component.scss'
})
export class ComidaFormPanelComponent {
  @Input({ required: true }) comidaForm!: FormGroup;
  @Input({ required: true }) categorias: string[] = [];
  @Input() esEdicion = false;
  @Output() guardar = new EventEmitter<void>();
}
