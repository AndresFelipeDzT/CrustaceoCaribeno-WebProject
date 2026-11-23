package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CuponAplicacionDTO;
import com.example.demo.dto.CuponDTO;
import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Cupon;
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
import com.example.demo.repository.ClienteFakeRepository;
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
    private ClienteFakeRepository clienteRepository;

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

    @Autowired
    private CuponService cuponService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito(carrito);
        double subtotal = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        double domicilio = items.isEmpty() ? 0 : 5000;
        return new CarritoDTO(
                carrito.getIdCarrito(),
                carrito.getCliente().getIdCliente(),
                subtotal,
                domicilio,
                subtotal + domicilio);
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

    @Override
    public CuponDTO obtenerCuponDisponible(Long idCliente) {
        clienteService.obtenerClientePorId(idCliente);
        return cuponService.obtenerDisponible(idCliente);
    }

    @Override
    public CuponAplicacionDTO validarCupon(Long idCliente, String codigo) {
        Carrito carrito = obtenerCarrito(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito(carrito);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Agrega productos al carrito antes de aplicar un descuento.");
        }
        Cupon cupon = cuponService.validar(idCliente, codigo);
        double subtotal = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        double descuento = calcularDescuento(subtotal);
        double domicilio = 5000;
        return new CuponAplicacionDTO(
                cupon.getCodigo(), CuponService.PORCENTAJE_DESCUENTO,
                subtotal, descuento, domicilio, subtotal - descuento + domicilio);
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

    public PedidoDTO confirmarPedido(Long idCliente, String codigoCupon) {
        clienteService.obtenerClientePorId(idCliente);
        clienteRepository.findByIdParaActualizar(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));
        Carrito carrito = obtenerCarrito(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito(carrito);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }
        double subtotal = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        Cupon cupon = codigoCupon == null || codigoCupon.isBlank()
                ? null
                : cuponService.validar(idCliente, codigoCupon);
        double descuento = cupon == null ? 0 : calcularDescuento(subtotal);
        double domicilio = 5000;
        Pedido pedido = new Pedido();
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido.setCliente(carrito.getCliente());
        pedido.setDescuento(descuento);
        pedido.setDomicilio(domicilio);
        pedido.setCodigoCupon(cupon == null ? null : cupon.getCodigo());
        pedido = pedidoRepository.save(pedido);

        // Copia los ítems antes de vaciar el carrito; ambos cambios participan en la misma transacción.
        List<String> lineasPedido = new ArrayList<>();
        for (ItemCarrito item : items) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProducto(item.getProducto());
            itemPedido.setCantidad(item.getCantidad());
            itemPedido.setAdicionales(new ArrayList<>(item.getAdicionales()));
            itemPedidoRepository.save(itemPedido);
            pedido.getItems().add(itemPedido);
            String adicionales = item.getAdicionales().stream()
                    .map(Adicional::getNombre)
                    .collect(java.util.stream.Collectors.joining(", "));
            lineasPedido.add(String.format(
                    java.util.Locale.forLanguageTag("es-CO"),
                    "%d x %s%s - $%,.0f COP",
                    item.getCantidad(),
                    item.getProducto().getNombre(),
                    adicionales.isEmpty() ? "" : " (adicionales: " + adicionales + ")",
                    item.getSubtotal()));
        }
        if (cupon != null) {
            cuponService.marcarUsado(cupon);
        }
        itemCarritoRepository.deleteAll(items);
        CuponDTO cuponDisponible = cuponService.obtenerOCrearDisponible(carrito.getCliente());
        PedidoDTO pedidoDTO = new PedidoDTO(
                pedido.getIdPedido(),
                pedido.getFechaCreacion(),
                pedido.getFechaEntrega(),
                pedido.getEstado(),
                pedido.getCliente().getIdCliente(),
                null,
                subtotal - descuento + domicilio,
                subtotal,
                descuento,
                domicilio,
                pedido.getCodigoCupon());
        eventPublisher.publishEvent(new PedidoConfirmadoEvent(
                carrito.getCliente().getCorreo(),
                carrito.getCliente().getNombre(),
                pedidoDTO,
                List.copyOf(lineasPedido),
                cuponDisponible.codigo()));
        return pedidoDTO;
    }

    private double calcularDescuento(double subtotal) {
        return Math.round(subtotal * CuponService.PORCENTAJE_DESCUENTO / 100.0);
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
