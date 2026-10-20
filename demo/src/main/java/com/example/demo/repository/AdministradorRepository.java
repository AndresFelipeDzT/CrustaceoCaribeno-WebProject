package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Administrador;

/**
 * Repositorio de acceso a datos para administrador mediante Spring Data JPA.
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByUsuarioIgnoreCase(String usuario);
}
