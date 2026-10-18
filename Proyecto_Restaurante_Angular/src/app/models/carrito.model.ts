import { Cliente } from './cliente.model';
import { ItemCarrito } from './item-carrito.model';

/** Representa la entidad Carrito de Spring Boot. */
export interface Carrito {
  idCarrito: number;
  idCliente?: number;
  cliente?: Cliente;
  items: ItemCarrito[];
  subtotal?: number;
  domicilio?: number;
  total?: number;
}

export interface CarritoDTO {
  idCarrito: number;
  idCliente: number;
  subtotal: number;
  domicilio: number;
  total: number;
}
