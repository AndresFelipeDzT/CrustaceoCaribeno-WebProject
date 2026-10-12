package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Pedido;

public interface PedidoService {
    List<Pedido> obtenerTodosLosPedidos();

    /**
     * Busca un pedido por su identificador.
     */
    Pedido obtenerPedidoPorId(Long id);

    void guardarPedido(Pedido pedido);

    void actualizarEstadoPedido(Long id, Pedido pedido);

    void eliminarPedido(Long id);
}
