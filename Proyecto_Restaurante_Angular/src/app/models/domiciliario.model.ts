/** Representa la entidad Domiciliario de Spring Boot. */
export interface Domiciliario {
  idDomiciliario: number;
  nombre: string;
  celular: string;
  cedula: string;
  imagenURL?: string | null;
  disponible: boolean;
}
