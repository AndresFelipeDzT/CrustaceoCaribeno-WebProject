import { Adicional } from './adicional.model';
import { Carrito } from './carrito.model';
import { Producto } from './producto.model';

/** Representa la entidad ItemCarrito de Spring Boot. */
export interface ItemCarrito {
  idItemCarrito: number;
  cantidad: number;
  carrito: Carrito;
  producto: Producto;
  adicionales: Adicional[];
  subtotal?: number;
}
