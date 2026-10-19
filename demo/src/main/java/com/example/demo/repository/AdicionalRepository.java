package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;

/**
 * Repositorio de acceso a datos para adicional mediante Spring Data JPA.
 */
@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Long> {
    List<Adicional> findByActivoTrue();
    List<Adicional> findByCategoriasContaining(Categoria categoria);
    List<Adicional> findByCategoriasContainingAndActivoTrue(Categoria categoria);
}
