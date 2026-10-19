/** Representa la entidad Operador de Spring Boot. */
export interface Operador {
  idOperador: number;
  nombre: string;
  imagenURL?: string | null;
  usuario: string;
  contrasena: string;
}
