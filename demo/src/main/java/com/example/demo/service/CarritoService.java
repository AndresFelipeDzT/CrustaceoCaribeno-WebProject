package com.example.demo.service;

import com.example.demo.entitys.Carrito;
import java.util.List;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.CarritoDTO;
import com.example.demo.dto.ItemCarritoDTO;
import com.example.demo.dto.PedidoDTO;

/**
 * Contrato de las operaciones de servicio para carrito.
 */
public interface CarritoService {
    Carrito obtenerCarrito(Long idCliente);
    CarritoDTO obtenerCarritoDTO(Long idCliente);
    List<ItemCarritoDTO> obtenerItemsDTO(Long idCliente);
    List<AdicionalDTO> obtenerAdicionalesDTO(Long idCliente, Long idItemCarrito);
    void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales);
    void actualizarCantidad(Long idCliente, Long idItemCarrito, int cantidad);
    void actualizarAdicionales(Long idCliente, Long idItemCarrito, Long[] idsAdicionales);
    void eliminarItem(Long idCliente, Long idItemCarrito);
    PedidoDTO confirmarPedido(Long idCliente);
}
