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
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.ItemCarritoRepository;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoFakeRepository;

import jakarta.transaction.Transactional;

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

    public void agregarProducto(Long idCliente, Long idProducto, Long[] idsAdicionales) {
        Carrito carrito = obtenerCarrito(idCliente);
        Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (producto == null || !producto.isActivo()) {
            throw new IllegalArgumentException("El producto ya no está disponible.");
        }
        ItemCarrito item = new ItemCarrito(1, carrito, producto);
        if (idsAdicionales != null) {
            for (Long idAdicional : idsAdicionales) {
                Adicional adicional = adicionalRepository.findById(idAdicional).orElse(null);
                if (adicional == null || !adicional.isActivo()) {
                    throw new IllegalArgumentException("El adicional ya no está disponible.");
                }
                item.getAdicionales().add(adicional);
            }
        }
        itemCarritoRepository.save(item);
    }

    public void actualizarCantidad(Long idCliente, Long idItemCarrito, int cantidad) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = itemCarritoRepository.findById(idItemCarrito).orElse(null);
        if (item == null || !item.getCarrito().equals(carrito)) {
            throw new IllegalArgumentException("El producto no pertenece a este carrito.");
        }
        if (cantidad < 1) {
            eliminarItem(idCliente, idItemCarrito);
            return;
        }
        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);
    }

    public void actualizarAdicionales(Long idCliente, Long idItemCarrito, Long[] idsAdicionales) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = itemCarritoRepository.findById(idItemCarrito).orElse(null);
        if (item == null || !item.getCarrito().equals(carrito)) {
            throw new IllegalArgumentException("El producto no pertenece a este carrito.");
        }
        item.getAdicionales().clear();
        if (idsAdicionales != null) {
            for (Long idAdicional : idsAdicionales) {
                Adicional adicional = adicionalRepository.findById(idAdicional).orElse(null);
                boolean disponible = item.getProducto().getCategoria() != null &&
                    item.getProducto().getCategoria().getAdicionales().contains(adicional);
                if (adicional == null || !adicional.isActivo() || !disponible) {
                    throw new IllegalArgumentException("El adicional no está disponible para este producto.");
                }
                item.getAdicionales().add(adicional);
            }
        }
        itemCarritoRepository.save(item);
    }

    public void eliminarItem(Long idCliente, Long idItemCarrito) {
        Carrito carrito = obtenerCarrito(idCliente);
        ItemCarrito item = itemCarritoRepository.findById(idItemCarrito).orElse(null);
        if (item == null || !item.getCarrito().equals(carrito)) {
            throw new IllegalArgumentException("El producto no pertenece a este carrito.");
        }
        itemCarritoRepository.delete(item);
    }

    public Pedido confirmarPedido(Long idCliente) {
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

        for (ItemCarrito item : items) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProducto(item.getProducto());
            itemPedido.setCantidad(item.getCantidad());
            itemPedido.setPrecioUnitario(item.getProducto().getPrecio());
            itemPedido.setAdicionales(new ArrayList<>(item.getAdicionales()));
            itemPedidoRepository.save(itemPedido);
        }
        itemCarritoRepository.deleteAll(items);
        return pedido;
    }
}
