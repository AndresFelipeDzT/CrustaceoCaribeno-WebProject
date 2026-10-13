import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClienteService } from '../../service/cliente.service';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
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
  error = '';

  clienteForm = this.formBuilder.nonNullable.group({
    nombreCompleto: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/)]],
    correo: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(15), Validators.pattern(/^[0-9+\- ]+$/)]],
    direccion: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam === null) return;

    this.clienteId = Number(idParam);
    this.clienteService.getClienteById(this.clienteId).subscribe({
      next: cliente => {
        this.isEdit = true;
        this.clienteForm.patchValue({
          nombreCompleto: `${cliente.nombre} ${cliente.apellido}`.trim(),
          correo: cliente.correo,
          telefono: cliente.telefono ?? '',
          direccion: cliente.direccion ?? '',
          password: cliente.password || ''
        });
      },
      error: () => {
        this.error = 'No fue posible cargar el cliente.';
      }
    });
  }

  guardar(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    const formVal = this.clienteForm.getRawValue();
    const [nombre, ...apellidos] = formVal.nombreCompleto.trim().split(/\s+/);
    const cliente = {
      nombre,
      apellido: apellidos.join(' '),
      correo: formVal.correo,
      telefono: formVal.telefono,
      direccion: formVal.direccion,
      password: formVal.password,
      activo: true
    };
    this.error = '';

    if (this.isEdit && this.clienteId !== undefined) {
      this.clienteService.updateCliente(this.clienteId, cliente).subscribe({
        next: () => void this.router.navigate(['/clientes']),
        error: () => this.error = 'No fue posible actualizar el cliente.'
      });
    } else {
      this.clienteService.addCliente(cliente).pipe(
        switchMap(() => this.clienteService.iniciarSesion(cliente.correo, cliente.password))
      ).subscribe({
        next: clienteAutenticado => {
          if (clienteAutenticado) {
            void this.router.navigate(['/comidas/tarjetas']);
          } else {
            void this.router.navigate(['/login']);
          }
        },
        error: () => this.error = 'No fue posible registrar el cliente.'
      });
    }
  }
}
