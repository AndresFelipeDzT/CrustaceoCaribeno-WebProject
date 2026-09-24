package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Pedido;
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
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public Pedido actualizarEstadoPedido(Long id, String estado) {
        Pedido pedido = obtenerPedidoPorId(id);
        if (pedido != null) {
            pedido.setEstado(estado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    @Override
    public void eliminarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        if (pedido != null) {
            pedidoRepository.delete(pedido);
        }
    }
}
