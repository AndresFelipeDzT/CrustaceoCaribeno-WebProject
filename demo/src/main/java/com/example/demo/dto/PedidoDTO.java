package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para pedido.
 */
@Data
@NoArgsConstructor
public class PedidoDTO {
    private Long idPedido;
    private LocalDate fechaCreacion;
    private LocalDate fechaEntrega;
    private String estado;
    private Long idCliente;
    private Long idDomiciliario;
    private double total;
    private double subtotal;
    private double descuento;
    private double domicilio;
    private String codigoCupon;

    public PedidoDTO(Long idPedido, LocalDate fechaCreacion, LocalDate fechaEntrega, String estado,
            Long idCliente, Long idDomiciliario, double total) {
        this(idPedido, fechaCreacion, fechaEntrega, estado, idCliente, idDomiciliario, total, total, 0, 0, null);
    }

    public PedidoDTO(Long idPedido, LocalDate fechaCreacion, LocalDate fechaEntrega, String estado,
            Long idCliente, Long idDomiciliario, double total, double subtotal,
            double descuento, double domicilio, String codigoCupon) {
        this.idPedido = idPedido;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idDomiciliario = idDomiciliario;
        this.total = total;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.domicilio = domicilio;
        this.codigoCupon = codigoCupon;
    }
}
