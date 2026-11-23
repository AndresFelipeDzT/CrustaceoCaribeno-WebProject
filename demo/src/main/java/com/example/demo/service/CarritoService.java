package com.example.demo.service;

import com.example.demo.entitys.Carrito;
import java.util.List;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.CarritoDTO;
import com.example.demo.dto.CuponAplicacionDTO;
import com.example.demo.dto.CuponDTO;
import com.example.demo.dto.ItemCarritoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.dto.RepetirPedidoDTO;

/**
 * Contrato de las operaciones de servicio para carrito.
 */
public interface CarritoService {
    Carrito obtenerCarrito(Long idCliente);
    CarritoDTO obtenerCarritoDTO(Long idCliente);
    List<ItemCarritoDTO> obtenerItemsDTO(Long idCliente);
    List<AdicionalDTO> obtenerAdicionalesDTO(Long idCliente, Long idItemCarrito);
    CuponDTO obtenerCuponDisponible(Long idCliente);
    CuponAplicacionDTO validarCupon(Long idCliente, String codigo);
    RepetirPedidoDTO repetirPedido(Long idCliente, Long idPedido);
    void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales);
    void actualizarCantidad(Long idCliente, Long idItemCarrito, int cantidad);
    void actualizarAdicionales(Long idCliente, Long idItemCarrito, Long[] idsAdicionales);
    void eliminarItem(Long idCliente, Long idItemCarrito);
    PedidoDTO confirmarPedido(Long idCliente, String codigoCupon);
}
