import { Pedido } from './pedido.model';

/** Representa la entidad Domiciliario de Spring Boot. */
export interface Domiciliario {
  idDomiciliario: number;
  nombre: string;
  celular: string;
  cedula: string;
  disponible: boolean;
  pedidos: Pedido[];
}
