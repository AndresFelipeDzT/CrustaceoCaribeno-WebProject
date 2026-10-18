package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemCarritoDTO {
    private Long idItemCarrito;
    private Long idCarrito;
    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public ItemCarritoDTO(Long idItemCarrito, Long idCarrito, Long idProducto, String nombreProducto,
            int cantidad, double precioUnitario, double subtotal) {
        this.idItemCarrito = idItemCarrito;
        this.idCarrito = idCarrito;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
}
