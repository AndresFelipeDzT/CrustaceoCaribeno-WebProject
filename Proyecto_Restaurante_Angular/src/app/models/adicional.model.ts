import { Categoria } from './categoria.model';

/** Representa la entidad Adicional de Spring Boot. */
export interface Adicional {
  idAdicional: number;
  nombre: string;
  precio: number;
  activo: boolean;
  categorias: Categoria[];
}
