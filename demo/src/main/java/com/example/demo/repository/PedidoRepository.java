package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Domiciliario;
import com.example.demo.entitys.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente(Cliente cliente);
    List<Pedido> findByDomiciliario(Domiciliario domiciliario);
    List<Pedido> findByEstado(String estado);
}
