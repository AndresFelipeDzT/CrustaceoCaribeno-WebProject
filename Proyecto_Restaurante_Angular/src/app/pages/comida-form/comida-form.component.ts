import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ComidaService } from '../../service/comida.service';
import { Categoria } from '../../models/categoria.model';
import { Producto } from '../../models/producto.model';
import { FooterComponent } from '../../components/footer/footer.component';
import { ComidaFormNavbarComponent } from './components/comida-form-navbar/comida-form-navbar.component';
import { ComidaFormPanelComponent } from './components/comida-form-panel/comida-form-panel.component';

@Component({
  selector: 'app-comida-form',
  imports: [ReactiveFormsModule, FooterComponent, ComidaFormNavbarComponent, ComidaFormPanelComponent],
  templateUrl: './comida-form.component.html',
  styleUrl: './comida-form.component.scss'
})
export class ComidaFormComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private comidaService = inject(ComidaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  categorias: Categoria[] = [];
  comidaId: number | undefined;
  error = '';

  comidaForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    descripcion: ['', [Validators.required, Validators.maxLength(250)]],
    precio: [0, [Validators.required, Validators.min(1)]],
    categoriaId: [0, [Validators.required, Validators.min(1)]],
    imagenURL: ['/imagenes/imagen1.png', Validators.required]
  });

  get esEdicion(): boolean {
    return this.comidaId !== undefined;
  }

  ngOnInit(): void {
    this.comidaService.getCategorias().subscribe({
      next: categorias => {
        this.categorias = categorias;
        if (!this.esEdicion && categorias.length > 0) {
          this.comidaForm.controls.categoriaId.setValue(categorias[0].idCategoria);
        }
      },
      error: () => this.error = 'No fue posible cargar las categorías de productos.'
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;

    this.comidaId = Number(idParam);
    this.comidaService.getComidaById(this.comidaId).subscribe({
      next: comida => this.comidaForm.patchValue({
        nombre: comida.nombre,
        descripcion: comida.descripcion ?? '',
        precio: comida.precio,
        categoriaId: comida.categoria?.idCategoria ?? 0,
        imagenURL: comida.imagenURL ?? ''
      }),
      error: () => this.error = 'No fue posible cargar el producto.'
    });
  }

  guardar(): void {
    if (this.comidaForm.invalid) {
      this.comidaForm.markAllAsTouched();
      return;
    }

    const valores = this.comidaForm.getRawValue();
    const categoria = this.categorias.find(item => item.idCategoria === valores.categoriaId);
    if (!categoria) {
      this.error = 'Selecciona una categoría válida.';
      return;
    }
    const comida: Omit<Producto, 'idProducto'> = {
      nombre: valores.nombre,
      descripcion: valores.descripcion,
      precio: valores.precio,
      imagenURL: valores.imagenURL,
      activo: true,
      categoria: { idCategoria: categoria.idCategoria, nombre: categoria.nombre }
    };
    this.error = '';
    if (this.comidaId === undefined) {
      this.comidaService.addComida(comida).subscribe({
        next: () => void this.router.navigate(['/comidas/tabla']),
        error: () => this.error = 'No fue posible guardar el producto.'
      });
    } else {
      this.comidaService.updateComida(this.comidaId, comida).subscribe({
        next: () => void this.router.navigate(['/comidas/tabla']),
        error: () => this.error = 'No fue posible actualizar el producto.'
      });
    }
  }
}
