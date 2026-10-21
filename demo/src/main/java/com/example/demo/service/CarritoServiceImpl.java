package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.ItemCarrito;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Producto;
import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.CarritoDTO;
import com.example.demo.dto.ItemCarritoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.ItemCarritoRepository;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoFakeRepository;

import jakarta.transaction.Transactional;

/**
 * Implementación de la lógica de servicio para carrito.
 */
@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public Carrito obtenerCarrito(Long idCliente) {
        Cliente cliente = clienteService.obtenerClientePorId(idCliente);
        Carrito carrito = carritoRepository.findByCliente(cliente);
        if (carrito == null) {
            carrito = new Carrito(cliente);
            carrito = carritoRepository.save(carrito);
        }
        return carrito;
    }

    @Override
    public CarritoDTO obtenerCarritoDTO(Long idCliente) {
        Carrito carrito = obtenerCarrito(idCliente);
        return new CarritoDTO(
                carrito.getIdCarrito(),
                carrito.getCliente().getIdCliente(),
                carrito.getSubtotal(),
                carrito.getDomicilio(),
                carrito.getTotal());
    }

    @Override
    public List<ItemCarritoDTO> obtenerItemsDTO(Long idCliente) {
        Carrito carrito = obtenerCarrito(idCliente);
        return itemCarritoRepository.findByCarrito(carrito).stream()
                .map(item -> new ItemCarritoDTO(
                        item.getIdItemCarrito(),
                        carrito.getIdCarrito(),
                        item.getProducto().getIdProducto(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        item.getProducto().getPrecio(),
                        item.getSubtotal()))
                .toList();
    }

    @Override
    public List<AdicionalDTO> obtenerAdicionalesDTO(Long idCliente, Long idItemCarrito) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = obtenerItemDelCarrito(carrito, idItemCarrito);
        return item.getAdicionales().stream()
                .map(adicional -> new AdicionalDTO(
                        adicional.getIdAdicional(),
                        adicional.getNombre(),
                        adicional.getPrecio(),
                        adicional.isActivo()))
                .toList();
    }

    public void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales) {
        Carrito carrito = obtenerCarrito(idCliente);
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio.");
        }
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto ya no está disponible."));
        if (!producto.isActivo()) {
            throw new IllegalArgumentException("El producto ya no está disponible.");
        }
        ItemCarrito item = new ItemCarrito(1, carrito, producto);
        if (idsAdicionales != null) {
            for (Long idAdicional : idsAdicionales) {
                if (idAdicional == null) {
                    throw new IllegalArgumentException("El ID del adicional es obligatorio.");
                }
                Adicional adicional = adicionalRepository.findById(idAdicional)
                        .orElseThrow(() -> new IllegalArgumentException("El adicional ya no está disponible."));
                if (!adicional.isActivo()) {
                    throw new IllegalArgumentException("El adicional ya no está disponible.");
                }
                item.getAdicionales().add(adicional);
            }
        }
        itemCarritoRepository.save(item);
    }

    public void actualizarCantidad(Long idCliente, Long idItemCarrito, int cantidad) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = obtenerItemDelCarrito(carrito, idItemCarrito);
        if (cantidad < 1) {
            eliminarItem(idCliente, idItemCarrito);
            return;
        }
        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);
    }

    public void actualizarAdicionales(Long idCliente, Long idItemCarrito, Long[] idsAdicionales) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = obtenerItemDelCarrito(carrito, idItemCarrito);
        item.getAdicionales().clear();
        if (idsAdicionales != null) {
            for (Long idAdicional : idsAdicionales) {
                if (idAdicional == null) {
                    throw new IllegalArgumentException("El ID del adicional es obligatorio.");
                }
                Adicional adicional = adicionalRepository.findById(idAdicional)
                        .orElseThrow(() -> new IllegalArgumentException("El adicional no está disponible para este producto."));
                // Los adicionales se habilitan por la categoría del producto, no solo por existir.
                boolean disponible = item.getProducto().getCategoria() != null &&
                    item.getProducto().getCategoria().getAdicionales().contains(adicional);
                if (!adicional.isActivo() || !disponible) {
                    throw new IllegalArgumentException("El adicional no está disponible para este producto.");
                }
                item.getAdicionales().add(adicional);
            }
        }
        itemCarritoRepository.save(item);
    }

    public void eliminarItem(Long idCliente, Long idItemCarrito) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = obtenerItemDelCarrito(carrito, idItemCarrito);
        itemCarritoRepository.delete(item);
    }

    public PedidoDTO confirmarPedido(Long idCliente) {
        Carrito carrito = obtenerCarrito(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito(carrito);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }
        Pedido pedido = new Pedido();
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido.setCliente(carrito.getCliente());
        pedido = pedidoRepository.save(pedido);
        double total = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();

        // Copia los ítems antes de vaciar el carrito; ambos cambios participan en la misma transacción.
        for (ItemCarrito item : items) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProducto(item.getProducto());
            itemPedido.setCantidad(item.getCantidad());
            itemPedido.setAdicionales(new ArrayList<>(item.getAdicionales()));
            itemPedidoRepository.save(itemPedido);
        }
        itemCarritoRepository.deleteAll(items);
        return new PedidoDTO(
                pedido.getIdPedido(),
                pedido.getFechaCreacion(),
                pedido.getFechaEntrega(),
                pedido.getEstado(),
                pedido.getCliente().getIdCliente(),
                null,
                total);
    }

    private ItemCarrito obtenerItemDelCarrito(Carrito carrito, Long idItemCarrito) {
        if (idItemCarrito == null) {
            throw new IllegalArgumentException("El ID del producto en el carrito es obligatorio.");
        }
        ItemCarrito item = itemCarritoRepository.findById(idItemCarrito)
                .orElseThrow(() -> new IllegalArgumentException("El producto no pertenece a este carrito."));
        // El ID por sí solo no basta: el ítem debe pertenecer al carrito del cliente solicitado.
        if (!item.getCarrito().equals(carrito)) {
            throw new IllegalArgumentException("El producto no pertenece a este carrito.");
        }
        return item;
    }
}
