package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para itempedido.
 */
@Data
@NoArgsConstructor
public class ItemPedidoDTO {
    private Long idItemPedido;
    private Long idPedido;
    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public ItemPedidoDTO(Long idItemPedido, Long idPedido, Long idProducto, String nombreProducto,
            int cantidad, double precioUnitario, double subtotal) {
        this.idItemPedido = idItemPedido;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
}
