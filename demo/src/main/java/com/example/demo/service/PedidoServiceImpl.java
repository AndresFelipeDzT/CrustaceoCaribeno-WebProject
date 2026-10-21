package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.ItemPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.errors.PedidoNotFoundException;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;

import jakarta.transaction.Transactional;

/**
 * Implementación de la lógica de servicio para pedido.
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private CarritoService carritoService;

    @Override
    public List<PedidoDTO> obtenerPedidosDTO() {
        return pedidoRepository.findAll().stream()
                .map(this::mapearPedidoDTO)
                .toList();
    }

    @Override
    public Pedido obtenerPedidoPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio.");
        }
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
    }

    @Override
    public PedidoDTO obtenerPedidoDTOPorId(Long id) {
        return mapearPedidoDTO(obtenerPedidoPorId(id));
    }

    @Override
    public List<ItemPedidoDTO> obtenerItemsDTODePedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        return itemPedidoRepository.findByPedido(pedido).stream()
                .map(this::mapearItemPedidoDTO)
                .toList();
    }

    @Override
    public List<AdicionalDTO> obtenerAdicionalesDTODeItem(Long idPedido, Long idItemPedido) {
        obtenerPedidoPorId(idPedido);
        if (idItemPedido == null) {
            throw new IllegalArgumentException("El ID del ítem del pedido es obligatorio.");
        }
        ItemPedido item = itemPedidoRepository.findById(idItemPedido)
                .orElseThrow(() -> new IllegalArgumentException("El ítem no pertenece a este pedido."));
        // Evita consultar adicionales usando un ítem perteneciente a otro pedido.
        if (item.getPedido() == null || !idPedido.equals(item.getPedido().getIdPedido())) {
            throw new IllegalArgumentException("El ítem no pertenece a este pedido.");
        }
        return item.getAdicionales().stream()
                .map(adicional -> new AdicionalDTO(
                        adicional.getIdAdicional(),
                        adicional.getNombre(),
                        adicional.getPrecio(),
                        adicional.isActivo()))
                .toList();
    }

    private PedidoDTO mapearPedidoDTO(Pedido pedido) {
        return new PedidoDTO(
                pedido.getIdPedido(),
                pedido.getFechaCreacion(),
                pedido.getFechaEntrega(),
                pedido.getEstado(),
                pedido.getCliente() == null ? null : pedido.getCliente().getIdCliente(),
                pedido.getDomiciliario() == null ? null : pedido.getDomiciliario().getIdDomiciliario(),
                pedido.getTotal());
    }

    private ItemPedidoDTO mapearItemPedidoDTO(ItemPedido item) {
        return new ItemPedidoDTO(
                item.getIdItemPedido(),
                item.getPedido().getIdPedido(),
                item.getProducto() == null ? null : item.getProducto().getIdProducto(),
                item.getProducto() == null ? null : item.getProducto().getNombre(),
                item.getCantidad(),
                item.getSubtotal());
    }

    @Override
    public void guardarPedido(Pedido pedido) {
        if (pedido == null || pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio para crear el pedido.");
        }
        carritoService.confirmarPedido(pedido.getCliente().getIdCliente());
    }

    @Override
    public void actualizarEstadoPedido(Long id, Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Los datos del pedido no pueden ser nulos.");
        }
        String estado = pedido.getEstado();
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio.");
        }
        Pedido pedidoExistente = obtenerPedidoPorId(id);
        pedidoExistente.setEstado(estado.trim());
        pedidoRepository.save(pedidoExistente);
    }

    @Override
    public void eliminarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedidoRepository.delete(pedido);
    }
}
