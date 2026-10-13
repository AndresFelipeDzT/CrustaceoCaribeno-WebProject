import { Adicional } from './adicional.model';
import { Producto } from './producto.model';

/** Representa la entidad ItemCarrito de Spring Boot. */
export interface ItemCarrito {
  idItemCarrito: number;
  cantidad: number;
  producto: Producto;
  adicionales: Adicional[];
  subtotal?: number;
}
