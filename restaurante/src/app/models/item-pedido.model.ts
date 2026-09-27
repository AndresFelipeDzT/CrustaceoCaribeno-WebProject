import { Adicional } from './adicional.model';
import { Pedido } from './pedido.model';
import { Producto } from './producto.model';

/** Representa la entidad ItemPedido de Spring Boot. */
export interface ItemPedido {
  idItemPedido: number;
  cantidad: number;
  precioUnitario: number;
  pedido?: Pedido | null;
  producto?: Producto | null;
  adicionales: Adicional[];
  subtotal?: number;
}
