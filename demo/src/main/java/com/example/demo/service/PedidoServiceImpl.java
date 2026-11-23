package com.example.demo.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.HistorialEstadoPedidoDTO;
import com.example.demo.dto.ItemPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.HistorialEstadoPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.errors.PedidoNotFoundException;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.HistorialEstadoPedidoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoFakeRepository;

import jakarta.transaction.Transactional;

/**
 * Implementación de la lógica de servicio para pedido.
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private static final List<String> FLUJO_OPERADOR = List.of(
            "pendiente", "en preparacion", "enviado", "en camino", "entregado");

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private HistorialEstadoPedidoRepository historialEstadoPedidoRepository;

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private ClienteService clienteService;

    @Override
    public List<PedidoDTO> obtenerPedidosDTO() {
        return obtenerPedidosDTO(false, null);
    }

    @Override
    public List<PedidoDTO> obtenerPedidosDTO(boolean soloActivos, Long clienteId) {
        List<Pedido> pedidos;
        if (clienteId != null) {
            Cliente cliente = clienteService.obtenerClientePorId(clienteId);
            pedidos = pedidoRepository.findByCliente(cliente);
        } else {
            pedidos = pedidoRepository.findAll();
        }

        return pedidos.stream()
                .filter(pedido -> !soloActivos || !pedidoCompletado(pedido))
                .map(this::mapearPedidoDTO)
                .toList();
    }

    private boolean pedidoCompletado(Pedido pedido) {
        if (pedido.getEstado() == null) {
            return false;
        }
        String estado = pedido.getEstado().trim();
        return "Entregado".equalsIgnoreCase(estado) || "Cancelado".equalsIgnoreCase(estado);
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
    public boolean perteneceACliente(Long idPedido, Long idCliente) {
        return pedidoRepository.existsByIdPedidoAndCliente_IdCliente(idPedido, idCliente);
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

    @Override
    public List<HistorialEstadoPedidoDTO> obtenerHistorialEstado(Long idPedido) {
        Pedido pedido = obtenerPedidoPorId(idPedido);
        return historialEstadoPedidoRepository.findByPedidoOrderByFechaHoraAsc(pedido).stream()
                .map(historial -> new HistorialEstadoPedidoDTO(historial.getEstado(), historial.getFechaHora()))
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
                pedido.getTotal(),
                pedido.getSubtotal(),
                pedido.getDescuento(),
                pedido.getDomicilio(),
                pedido.getCodigoCupon());
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
    public void actualizarEstadoPedido(Long id, Pedido pedido) {
        actualizarEstadoPedido(id, pedido, false);
    }

    @Override
    public void actualizarEstadoPedido(Long id, Pedido pedido, boolean esOperador) {
        if (pedido == null) {
            throw new IllegalArgumentException("Los datos del pedido no pueden ser nulos.");
        }
        String estado = pedido.getEstado();
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio.");
        }
        if (id == null) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio.");
        }
        Pedido pedidoExistente = pedidoRepository.findByIdParaActualizar(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
        String estadoActual = normalizarEstado(pedidoExistente.getEstado());
        String estadoNuevo = normalizarEstado(estado);
        if (("entregado".equals(estadoActual) || "cancelado".equals(estadoActual))
                && !estadoActual.equals(estadoNuevo)) {
            throw new IllegalArgumentException("Un pedido entregado o cancelado no puede cambiar de estado.");
        }
        if (esOperador && !transicionValidaOperador(pedidoExistente.getEstado(), estado)) {
            throw new IllegalArgumentException("Como operador, solo puedes avanzar al siguiente estado o cancelar un pedido activo.");
        }
        if (!estadoActual.equals(estadoNuevo) && "cancelado".equals(estadoNuevo)) {
            for (ItemPedido item : itemPedidoRepository.findByPedido(pedidoExistente)) {
                if (item.getProducto() != null) {
                    var producto = productoRepository.findByIdParaActualizar(item.getProducto().getIdProducto())
                            .orElseThrow(() -> new IllegalArgumentException("No se encontró un producto del pedido."));
                    producto.setStock(producto.getStock() + item.getCantidad());
                }
            }
        }
        boolean cambioEstado = !estadoActual.equals(estadoNuevo);
        if (cambioEstado && "entregado".equals(estadoNuevo)) {
            pedidoExistente.setFechaEntrega(LocalDate.now());
        }
        pedidoExistente.setEstado(estado.trim());
        pedidoRepository.save(pedidoExistente);
        if (cambioEstado) {
            historialEstadoPedidoRepository.save(new HistorialEstadoPedido(pedidoExistente, pedidoExistente.getEstado()));
        }
    }

    private boolean transicionValidaOperador(String actual, String nuevo) {
        String actualNormalizado = normalizarEstado(actual);
        String nuevoNormalizado = normalizarEstado(nuevo);
        if ("cancelado".equals(nuevoNormalizado)) {
            return !actualNormalizado.isEmpty() && !"entregado".equals(actualNormalizado)
                    && !"cancelado".equals(actualNormalizado);
        }
        int indiceActual = FLUJO_OPERADOR.indexOf(actualNormalizado);
        return indiceActual >= 0 && indiceActual + 1 < FLUJO_OPERADOR.size()
                && FLUJO_OPERADOR.get(indiceActual + 1).equals(nuevoNormalizado);
    }

    private String normalizarEstado(String estado) {
        if (estado == null) return "";
        return Normalizer.normalize(estado.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
    }

    @Override
    public void eliminarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedidoRepository.delete(pedido);
    }
}
