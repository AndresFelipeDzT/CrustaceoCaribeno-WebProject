package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.ItemPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Pedido;

/**
 * Contrato de las operaciones de servicio para pedido.
 */
public interface PedidoService {
    List<PedidoDTO> obtenerPedidosDTO();

    List<PedidoDTO> obtenerPedidosDTO(boolean soloActivos, Long clienteId);

    /**
     * Busca un pedido por su identificador.
     */
    Pedido obtenerPedidoPorId(Long id);

    PedidoDTO obtenerPedidoDTOPorId(Long id);

    List<ItemPedidoDTO> obtenerItemsDTODePedido(Long id);

    List<AdicionalDTO> obtenerAdicionalesDTODeItem(Long idPedido, Long idItemPedido);

    void actualizarEstadoPedido(Long id, Pedido pedido);

    void eliminarPedido(Long id);
}
