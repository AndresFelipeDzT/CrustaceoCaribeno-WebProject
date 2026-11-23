package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.example.demo.entitys.HistorialEstadoPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Producto;
import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.CarritoDTO;
import com.example.demo.dto.ItemCarritoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.dto.RepetirPedidoDTO;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.ClienteFakeRepository;
import com.example.demo.repository.ItemCarritoRepository;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.HistorialEstadoPedidoRepository;
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
    private HistorialEstadoPedidoRepository historialEstadoPedidoRepository;

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
        Producto producto = productoRepository.findByIdParaActualizar(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto ya no está disponible."));
        int cantidadEnCarrito = itemCarritoRepository.findByCarrito(carrito).stream()
                .filter(item -> item.getProducto().getIdProducto().equals(idProducto))
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
        if (!producto.isActivo()) {
            throw new IllegalArgumentException("El producto ya no está disponible.");
        }
        if (producto.getStock() <= cantidadEnCarrito) {
            throw new IllegalArgumentException("No hay inventario disponible para agregar otra unidad.");
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

    @Override
    public RepetirPedidoDTO repetirPedido(Long idCliente, Long idPedido) {
        clienteRepository.findByIdParaActualizar(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));
        Pedido pedido = pedidoRepository.findByIdPedidoAndCliente_IdCliente(idPedido, idCliente)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no pertenece al cliente."));
        if (!"Entregado".equalsIgnoreCase(pedido.getEstado().trim())) {
            throw new IllegalArgumentException("Solo puedes repetir pedidos entregados.");
        }

        Carrito carrito = obtenerCarrito(idCliente);
        Map<Long, Integer> enCarrito = new TreeMap<>();
        for (ItemCarrito item : itemCarritoRepository.findByCarrito(carrito)) {
            enCarrito.merge(item.getProducto().getIdProducto(), item.getCantidad(), Integer::sum);
        }
        List<String> avisos = new ArrayList<>();
        int agregadas = 0;
        // Adquiere los bloqueos en el mismo orden que la confirmación del carrito.
        List<ItemPedido> lineas = pedido.getItems().stream()
                .filter(item -> item.getProducto() != null)
                .sorted(Comparator.comparing((ItemPedido item) -> item.getProducto().getIdProducto())
                        .thenComparing(ItemPedido::getIdItemPedido))
                .toList();
        if (lineas.size() < pedido.getItems().size()) {
            avisos.add("Un producto del pedido ya no existe.");
        }
        for (ItemPedido linea : lineas) {
            Long id = linea.getProducto().getIdProducto();
            Producto producto = productoRepository.findByIdParaActualizar(id).orElse(null);
            int stock = producto == null || !producto.isActivo() ? 0 : producto.getStock();
            int cantidad = Math.min(linea.getCantidad(), Math.max(0, stock - enCarrito.getOrDefault(id, 0)));
            if (cantidad < linea.getCantidad()) {
                avisos.add(linea.getProducto().getNombre() + ": " + (linea.getCantidad() - cantidad)
                        + " unidad(es) sin disponibilidad.");
            }
            if (cantidad == 0) continue;
            ItemCarrito nuevo = new ItemCarrito(cantidad, carrito, producto);
            for (Adicional adicional : linea.getAdicionales()) {
                if (adicionalDisponible(producto, adicional)) {
                    nuevo.getAdicionales().add(adicional);
                } else {
                    avisos.add(producto.getNombre() + ": se omitió " + adicional.getNombre() + ".");
                }
            }
            itemCarritoRepository.save(nuevo);
            enCarrito.merge(id, cantidad, Integer::sum);
            agregadas += cantidad;
        }
        return new RepetirPedidoDTO(agregadas, avisos);
    }

    public void actualizarCantidad(Long idCliente, Long idItemCarrito, int cantidad) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = obtenerItemDelCarrito(carrito, idItemCarrito);
        if (cantidad < 1) {
            eliminarItem(idCliente, idItemCarrito);
            return;
        }
        Producto producto = productoRepository.findByIdParaActualizar(item.getProducto().getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("El producto ya no está disponible."));
        int otrasUnidades = itemCarritoRepository.findByCarrito(carrito).stream()
                .filter(otro -> otro.getProducto().getIdProducto().equals(producto.getIdProducto())
                        && !otro.getIdItemCarrito().equals(idItemCarrito))
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
        if (!producto.isActivo()) {
            throw new IllegalArgumentException("El producto ya no está disponible.");
        }
        if (cantidad + otrasUnidades > producto.getStock()) {
            throw new IllegalArgumentException("La cantidad solicitada supera el inventario disponible.");
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
                if (!adicionalDisponible(item.getProducto(), adicional)) {
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
        Map<Long, Integer> cantidadesSolicitadas = new TreeMap<>();
        for (ItemCarrito item : items) {
            cantidadesSolicitadas.merge(item.getProducto().getIdProducto(), item.getCantidad(), Integer::sum);
        }
        Map<Long, Producto> productosBloqueados = new TreeMap<>();
        for (Map.Entry<Long, Integer> entrada : cantidadesSolicitadas.entrySet()) {
            Producto producto = productoRepository.findByIdParaActualizar(entrada.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Un producto del carrito ya no está disponible."));
            if (!producto.isActivo() || producto.getStock() < entrada.getValue()) {
                throw new IllegalArgumentException("Inventario insuficiente para " + producto.getNombre()
                        + ". Disponible: " + producto.getStock() + ".");
            }
            productosBloqueados.put(entrada.getKey(), producto);
        }
        for (Map.Entry<Long, Integer> entrada : cantidadesSolicitadas.entrySet()) {
            Producto producto = productosBloqueados.get(entrada.getKey());
            producto.setStock(producto.getStock() - entrada.getValue());
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
        historialEstadoPedidoRepository.save(new HistorialEstadoPedido(pedido, pedido.getEstado()));
        double total = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();

        // Copia los ítems antes de vaciar el carrito; ambos cambios participan en la misma transacción.
        List<String> lineasPedido = new ArrayList<>();
        for (ItemCarrito item : items) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProducto(productosBloqueados.get(item.getProducto().getIdProducto()));
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

    private boolean adicionalDisponible(Producto producto, Adicional adicional) {
        return adicional.isActivo() && producto.getCategoria() != null
                && producto.getCategoria().getAdicionales().stream()
                        .anyMatch(opcion -> opcion.getIdAdicional().equals(adicional.getIdAdicional()));
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
