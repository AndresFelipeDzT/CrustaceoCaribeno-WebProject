export interface VentaPlato {
  idProducto: number | null;
  nombre: string;
  unidades: number;
  valorPlatos: number;
  unidadesSinPrecio: number;
}

export interface ResumenVentas {
  pedidosEntregados: number;
  unidadesVendidas: number;
  totalCobrado: number;
  pedidosSinImporte: number;
  platos: VentaPlato[];
}
