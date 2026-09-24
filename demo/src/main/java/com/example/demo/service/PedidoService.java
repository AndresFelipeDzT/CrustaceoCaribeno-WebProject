package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Pedido;

public interface PedidoService {
    List<Pedido> obtenerTodosLosPedidos();
    Pedido obtenerPedidoPorId(Long id);
    Pedido actualizarEstadoPedido(Long id, String estado);
    void eliminarPedido(Long id);
}
