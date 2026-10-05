import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TableNavbarComponent } from '../../components/navbar/table-navbar/table-navbar.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { ClienteService } from '../../service/cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, TableNavbarComponent, FooterComponent],
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.scss'
})
export class ClienteFormComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  clienteId: number | undefined;
  isEdit = false;

  clienteForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/)]],
    apellido: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/)]],
    correo: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(15), Validators.pattern(/^[0-9+\- ]+$/)]],
    direccion: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    password: ['1234', [Validators.required, Validators.minLength(4)]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;

    this.clienteId = Number(idParam);
    const cliente = this.clienteService.getClienteById(this.clienteId);
    if (!cliente) {
      void this.router.navigate(['/clientes']);
      return;
    }

    this.isEdit = true;
    this.clienteForm.patchValue({
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      correo: cliente.correo,
      telefono: cliente.telefono ?? '',
      direccion: cliente.direccion ?? '',
      password: cliente.password || '••••'
    });
  }

  guardar(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    const formVal = this.clienteForm.getRawValue();

    if (this.isEdit && this.clienteId !== undefined) {
      this.clienteService.updateCliente(this.clienteId, {
        nombre: formVal.nombre,
        apellido: formVal.apellido,
        correo: formVal.correo,
        telefono: formVal.telefono,
        direccion: formVal.direccion,
        password: formVal.password,
        activo: true
      });
    } else {
      this.clienteService.addCliente({
        nombre: formVal.nombre,
        apellido: formVal.apellido,
        correo: formVal.correo,
        telefono: formVal.telefono,
        direccion: formVal.direccion,
        password: formVal.password,
        activo: true
      });
    }

    void this.router.navigate(['/clientes']);
  }
}
