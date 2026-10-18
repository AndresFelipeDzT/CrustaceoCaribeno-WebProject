import { Cliente } from './cliente.model';
import { Domiciliario } from './domiciliario.model';
import { ItemPedido } from './item-pedido.model';

/** Representa la entidad Pedido de Spring Boot. Las fechas usan formato ISO (YYYY-MM-DD). */
export interface Pedido {
  idPedido: number;
  fechaCreacion: string;
  fechaEntrega?: string | null;
  estado: string;
  cliente?: Cliente | null;
  domiciliario?: Domiciliario | null;
  idCliente?: number | null;
  idDomiciliario?: number | null;
  total?: number;
  items?: ItemPedido[];
}

export interface PedidoDTO {
  idPedido: number;
  fechaCreacion: string;
  fechaEntrega?: string | null;
  estado: string;
  idCliente?: number | null;
  idDomiciliario?: number | null;
  total?: number;
}
