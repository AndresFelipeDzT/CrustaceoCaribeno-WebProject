import { Carrito } from './carrito.model';

/** Representa la entidad Cliente de Spring Boot. */
export interface Cliente {
  idCliente: number;
  nombre: string;
  apellido: string;
  correo: string;
  telefono?: string | null;
  direccion?: string | null;
  password: string;
  activo: boolean;
  carrito?: Carrito | null;
}
