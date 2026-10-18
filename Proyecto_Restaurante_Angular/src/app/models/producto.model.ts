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
}

export interface ProductoDTO {
  idProducto: number;
  nombre: string;
  precio: number;
  descripcion?: string | null;
  imagenURL?: string | null;
  activo: boolean;
  idCategoria?: number | null;
  nombreCategoria?: string | null;
}
