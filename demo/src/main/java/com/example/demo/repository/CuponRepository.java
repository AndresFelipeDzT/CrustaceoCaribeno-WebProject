package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Cupon;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {
    Optional<Cupon> findFirstByCliente_IdClienteAndFechaUsoIsNullOrderByIdCuponAsc(Long idCliente);
    Optional<Cupon> findByCodigoIgnoreCase(String codigo);
    boolean existsByCodigo(String codigo);
}
