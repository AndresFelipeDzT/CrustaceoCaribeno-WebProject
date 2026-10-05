import { Adicional } from './adicional.model';
import { Producto } from './producto.model';

/** Representa la entidad Categoria de Spring Boot. */
export interface Categoria {
  idCategoria: number;
  nombre: string;
  productos: Producto[];
  adicionales: Adicional[];
}
