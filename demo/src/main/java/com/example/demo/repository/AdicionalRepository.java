package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;

@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Long> {
    List<Adicional> findByActivoTrue();
    List<Adicional> findByCategoria(Categoria categoria);
    List<Adicional> findByCategoriaIdCategoria(Long idCategoria);
    List<Adicional> findByCategoriaAndActivoTrue(Categoria categoria);
}
