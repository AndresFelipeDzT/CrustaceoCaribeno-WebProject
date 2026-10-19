package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para carrito.
 */
@Data
@NoArgsConstructor
public class CarritoDTO {
    private Long idCarrito;
    private Long idCliente;
    private double subtotal;
    private double domicilio;
    private double total;

    public CarritoDTO(Long idCarrito, Long idCliente, double subtotal, double domicilio, double total) {
        this.idCarrito = idCarrito;
        this.idCliente = idCliente;
        this.subtotal = subtotal;
        this.domicilio = domicilio;
        this.total = total;
    }
}
