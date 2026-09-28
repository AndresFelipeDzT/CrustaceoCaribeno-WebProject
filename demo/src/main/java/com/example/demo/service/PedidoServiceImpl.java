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
    public Pedido actualizarEstadoPedido(Long id, String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio.");
        }
        Pedido pedido = obtenerPedidoPorId(id);
        pedido.setEstado(estado.trim());
        return pedidoRepository.save(pedido);
    }

    @Override
    public void eliminarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedidoRepository.delete(pedido);
    }
}
