package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Cliente;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Carrito findByCliente(Cliente cliente);
}
