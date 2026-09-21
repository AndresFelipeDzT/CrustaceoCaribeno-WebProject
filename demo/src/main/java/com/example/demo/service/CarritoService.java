package com.example.demo.service;

import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Pedido;

public interface CarritoService {
    Carrito obtenerCarrito(Long idCliente);
    void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales);
    Pedido confirmarPedido(Long idCliente);
}
