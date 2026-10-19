/** Representa la entidad Adicional de Spring Boot. */
export interface Adicional {
  idAdicional: number;
  nombre: string;
  precio: number;
  activo: boolean;
  categoriaIds: number[];
}

/** Campos que acepta AdicionalRequestDTO al crear o actualizar. */
export interface AdicionalRequest {
  nombre: string;
  precio: number;
  categoriaIds: number[];
}
