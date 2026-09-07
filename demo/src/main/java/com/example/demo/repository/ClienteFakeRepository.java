package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio falso en memoria para almacenar clientes.
 * Contiene datos iniciales sin tildes para pruebas rápidas.
 */
@Repository
public interface ClienteFakeRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreo(String correo);
     Optional<Cliente> findByNombre(String nombre);
}
