package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Domiciliario;

/**
 * Repositorio de acceso a datos para domiciliario mediante Spring Data JPA.
 */
@Repository
public interface DomiciliarioRepository extends JpaRepository<Domiciliario, Long> {
    Optional<Domiciliario> findByCedula(String cedula);
    List<Domiciliario> findByDisponible(Boolean disponible);
}
