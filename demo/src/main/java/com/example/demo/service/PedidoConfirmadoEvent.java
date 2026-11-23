package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.PedidoDTO;

public record PedidoConfirmadoEvent(
        String correoCliente,
        String nombreCliente,
        PedidoDTO pedido,
        List<String> lineasPedido,
        String codigoCuponDisponible) {
}
