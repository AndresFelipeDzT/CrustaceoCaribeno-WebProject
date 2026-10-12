package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Pedido;
import com.example.demo.errors.PedidoNotFoundException;
import com.example.demo.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoService carritoService;

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
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
