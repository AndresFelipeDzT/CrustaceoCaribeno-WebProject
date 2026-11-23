export interface Cupon {
  codigo: string;
  porcentaje: number;
  fechaEmision: string;
}

export interface CuponAplicacion {
  codigoCupon: string;
  porcentaje: number;
  subtotal: number;
  descuento: number;
  domicilio: number;
  total: number;
}
