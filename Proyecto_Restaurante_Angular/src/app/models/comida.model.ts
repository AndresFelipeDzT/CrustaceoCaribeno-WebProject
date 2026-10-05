export interface Comida {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  categoria: string;
  imagenURL: string;
  activo?: boolean;
}
