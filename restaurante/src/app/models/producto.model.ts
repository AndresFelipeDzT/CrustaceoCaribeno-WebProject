import { Adicional } from './adicional.model';
import { Categoria } from './categoria.model';

/** Representa la entidad Producto de Spring Boot. */
export interface Producto {
  idProducto: number;
  nombre: string;
  precio: number;
  descripcion?: string | null;
  imagenURL?: string | null;
  activo: boolean;
  categoria?: Categoria | null;
  adicionalesDisponibles: Adicional[];
}
