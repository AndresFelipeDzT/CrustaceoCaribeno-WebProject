package com.example.demo.service;

import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Pedido;

public interface CarritoService {
    Carrito obtenerCarrito(Long idCliente);
    void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales);
    void actualizarCantidad(Long idCliente, Long idItemCarrito, int cantidad);
    void actualizarAdicionales(Long idCliente, Long idItemCarrito, Long[] idsAdicionales);
    void eliminarItem(Long idCliente, Long idItemCarrito);
    Pedido confirmarPedido(Long idCliente);
}
