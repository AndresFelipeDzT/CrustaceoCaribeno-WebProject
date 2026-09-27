import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ComidaService } from '../../service/comida.service';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-comida-form',
  imports: [ReactiveFormsModule, RouterLink, FooterComponent],
  templateUrl: './comida-form.component.html',
  styleUrl: './comida-form.component.scss'
})
export class ComidaFormComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private comidaService = inject(ComidaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  readonly categorias = ['Entrada', 'Plato Fuerte', 'Especialidades De La Casa', 'Postre', 'Bebida'];
  comidaId: number | undefined;

  comidaForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    descripcion: ['', [Validators.required, Validators.maxLength(250)]],
    precio: [0, [Validators.required, Validators.min(1)]],
    categoria: ['Entrada', Validators.required],
    imagenURL: ['/imagenes/imagen1.png', Validators.required]
  });

  get esEdicion(): boolean {
    return this.comidaId !== undefined;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;

    this.comidaId = Number(idParam);
    const comida = this.comidaService.getComidaById(this.comidaId);
    if (!comida) {
      void this.router.navigate(['/comidas/tabla']);
      return;
    }
    this.comidaForm.patchValue(comida);
  }

  guardar(): void {
    if (this.comidaForm.invalid) {
      this.comidaForm.markAllAsTouched();
      return;
    }

    const comida = this.comidaForm.getRawValue();
    if (this.comidaId === undefined) {
      this.comidaService.addComida(comida);
    } else {
      this.comidaService.updateComida(this.comidaId, comida);
    }
    void this.router.navigate(['/comidas/tabla']);
  }
}
