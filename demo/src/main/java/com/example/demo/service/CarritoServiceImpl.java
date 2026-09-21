package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.ItemCarrito;
import com.example.demo.entitys.ItemCarritoAdicional;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.ItemPedidoAdicional;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Producto;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.ItemCarritoAdicionalRepository;
import com.example.demo.repository.ItemCarritoRepository;
import com.example.demo.repository.ItemPedidoAdicionalRepository;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoFakeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepository carritoRepository;
    private final ClienteService clienteService;
    private final ProductoFakeRepository productoRepository;
    private final AdicionalRepository adicionalRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ItemCarritoAdicionalRepository itemCarritoAdicionalRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ItemPedidoAdicionalRepository itemPedidoAdicionalRepository;

    public CarritoServiceImpl(CarritoRepository carritoRepository, ClienteService clienteService,
            ProductoFakeRepository productoRepository, AdicionalRepository adicionalRepository,
            ItemCarritoRepository itemCarritoRepository, ItemCarritoAdicionalRepository itemCarritoAdicionalRepository,
            PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository,
            ItemPedidoAdicionalRepository itemPedidoAdicionalRepository) {
        this.carritoRepository = carritoRepository; this.clienteService = clienteService;
        this.productoRepository = productoRepository; this.adicionalRepository = adicionalRepository;
        this.itemCarritoRepository = itemCarritoRepository; this.itemCarritoAdicionalRepository = itemCarritoAdicionalRepository;
        this.pedidoRepository = pedidoRepository; this.itemPedidoRepository = itemPedidoRepository;
        this.itemPedidoAdicionalRepository = itemPedidoAdicionalRepository;
    }

    public Carrito obtenerCarrito(Long idCliente) {
        Cliente cliente = clienteService.obtenerClientePorId(idCliente);
        return carritoRepository.findByCliente(cliente).orElseGet(() -> carritoRepository.save(new Carrito(cliente)));
    }

    public void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales) {
        Carrito carrito = obtenerCarrito(idCliente);
        Producto producto = productoRepository.findById(idProducto).orElseThrow();
        if (!producto.isActivo()) {
            throw new IllegalArgumentException("El producto ya no está disponible.");
        }
        ItemCarrito item = itemCarritoRepository.save(new ItemCarrito(1, carrito, producto));
        if (idsAdicionales != null) for (Long idAdicional : idsAdicionales) {
            Adicional adicional = adicionalRepository.findById(idAdicional).orElseThrow();
            if (!adicional.isActivo()) {
                throw new IllegalArgumentException("El adicional ya no está disponible.");
            }
            itemCarritoAdicionalRepository.save(new ItemCarritoAdicional(item, adicional));
        }
    }

    public Pedido confirmarPedido(Long idCliente) {
        Carrito carrito = obtenerCarrito(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito(carrito);
        if (items.isEmpty()) throw new IllegalArgumentException("El carrito está vacío.");
        Pedido pedido = pedidoRepository.save(Pedido.builder().fechaCreacion(LocalDate.now())
                .estado("Pendiente").cliente(carrito.getCliente()).build());
        for (ItemCarrito item : items) {
            ItemPedido itemPedido = itemPedidoRepository.save(ItemPedido.builder().pedido(pedido)
                    .producto(item.getProducto()).cantidad(item.getCantidad())
                    .precioUnitario(item.getProducto().getPrecio()).build());
            for (ItemCarritoAdicional relacion : itemCarritoAdicionalRepository.findByItemCarrito(item))
                itemPedidoAdicionalRepository.save(new ItemPedidoAdicional(itemPedido, relacion.getAdicional()));
            itemCarritoAdicionalRepository.deleteAll(itemCarritoAdicionalRepository.findByItemCarrito(item));
        }
        itemCarritoRepository.deleteAll(items);
        return pedido;
    }
}
