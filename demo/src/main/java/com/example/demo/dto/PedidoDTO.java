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

    public PedidoDTO(Long idPedido, LocalDate fechaCreacion, LocalDate fechaEntrega, String estado,
            Long idCliente, Long idDomiciliario, double total) {
        this.idPedido = idPedido;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idDomiciliario = idDomiciliario;
        this.total = total;
    }
}
