import { Injectable } from '@angular/core';
import { Cliente } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private readonly storageKey = 'restaurante.clientes';

  // 10 clientes iniciales que corresponden con la base de datos de Spring Boot (DataLoader.java)
  private clientes: Cliente[] = [
    {
      idCliente: 1,
      nombre: 'Juan',
      apellido: 'Rodríguez',
      correo: 'Juan.Rodriguez@gmail.com',
      telefono: '33034534',
      direccion: 'Av carrera 45 # 34-56',
      password: '••••',
      activo: true
    },
    {
      idCliente: 2,
      nombre: 'María',
      apellido: 'Gómez',
      correo: 'maria.gomez@gmail.com',
      telefono: '3104567890',
      direccion: 'Calle 100 # 15-23',
      password: '••••',
      activo: true
    },
    {
      idCliente: 3,
      nombre: 'Carlos',
      apellido: 'López',
      correo: 'carlos.lopez@hotmail.com',
      telefono: '3209876543',
      direccion: 'Carrera 7 # 72-10',
      password: '••••',
      activo: true
    },
    {
      idCliente: 4,
      nombre: 'Ana',
      apellido: 'Martínez',
      correo: 'ana.martinez@yahoo.com',
      telefono: '3152345678',
      direccion: 'Diagonal 45 # 12-89',
      password: '••••',
      activo: true
    },
    {
      idCliente: 5,
      nombre: 'Luis',
      apellido: 'García',
      correo: 'luis.garcia@outlook.com',
      telefono: '3007654321',
      direccion: 'Avenida Calle 26 # 68-90',
      password: '••••',
      activo: true
    },
    {
      idCliente: 6,
      nombre: 'Diana',
      apellido: 'Sánchez',
      correo: 'diana.sanchez@gmail.com',
      telefono: '3123450987',
      direccion: 'Transversal 23 # 45-67',
      password: '••••',
      activo: true
    },
    {
      idCliente: 7,
      nombre: 'Andrés',
      apellido: 'Pérez',
      correo: 'andres.perez@hotmail.com',
      telefono: '3187654321',
      direccion: 'Calle 134 # 45A-12',
      password: '••••',
      activo: true
    },
    {
      idCliente: 8,
      nombre: 'Laura',
      apellido: 'Ramírez',
      correo: 'laura.ramirez@gmail.com',
      telefono: '3214560987',
      direccion: 'Carrera 50 # 80-45',
      password: '••••',
      activo: true
    },
    {
      idCliente: 9,
      nombre: 'Jorge',
      apellido: 'Herrera',
      correo: 'jorge.herrera@outlook.com',
      telefono: '3149876543',
      direccion: 'Avenida Boyacá # 53-22',
      password: '••••',
      activo: true
    },
    {
      idCliente: 10,
      nombre: 'Sofía',
      apellido: 'Castro',
      correo: 'sofia.castro@yahoo.com',
      telefono: '3165432109',
      direccion: 'Circular 4 # 71-15',
      password: '••••',
      activo: true
    }
  ];

  constructor() {
    this.cargarDesdeStorage();
  }

  private cargarDesdeStorage(): void {
    try {
      if (typeof window === 'undefined' || !window.localStorage) return;
      const raw = localStorage.getItem(this.storageKey);
      if (raw) {
        const parsed = JSON.parse(raw);
        if (Array.isArray(parsed) && parsed.length > 0) {
          this.clientes = parsed;
        }
      }
    } catch {
      // Si el navegador bloquea localStorage, los datos continúan en memoria
    }
  }

  private guardarEnStorage(): void {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.setItem(this.storageKey, JSON.stringify(this.clientes));
      }
    } catch {
      // Fallback seguro en memoria
    }
  }

  // Obtener todos los clientes (activos e inactivos) para la tabla de administración
  getClientes(): Cliente[] {
    return this.clientes;
  }

  // Obtener únicamente clientes activos
  getClientesActivos(): Cliente[] {
    return this.clientes.filter(c => c.activo !== false);
  }

  // Buscar un cliente por ID
  getClienteById(id: number): Cliente | undefined {
    return this.clientes.find(c => c.idCliente === id);
  }

  // Crear nuevo cliente
  addCliente(cliente: Omit<Cliente, 'idCliente'>): void {
    const nextId = Math.max(0, ...this.clientes.map(c => c.idCliente)) + 1;
    const nuevoCliente: Cliente = {
      ...cliente,
      idCliente: nextId,
      activo: true
    };
    this.clientes.push(nuevoCliente);
    this.guardarEnStorage();
  }

  // Actualizar datos de cliente existente
  updateCliente(id: number, datos: Omit<Cliente, 'idCliente'>): void {
    const index = this.clientes.findIndex(c => c.idCliente === id);
    if (index >= 0) {
      const activo = this.clientes[index].activo !== false;
      this.clientes[index] = {
        ...this.clientes[index],
        ...datos,
        idCliente: id,
        activo
      };
      this.guardarEnStorage();
    }
  }

  // Desactivar cliente (eliminación lógica según patrón del profesor)
  desactivarCliente(id: number): void {
    const cliente = this.getClienteById(id);
    if (cliente) {
      cliente.activo = false;
      this.guardarEnStorage();
    }
  }

  // Reactivar cliente
  activarCliente(id: number): void {
    const cliente = this.getClienteById(id);
    if (cliente) {
      cliente.activo = true;
      this.guardarEnStorage();
    }
  }

  // Eliminar / Desactivar cliente
  deleteCliente(id: number): void {
    this.desactivarCliente(id);
  }
}
