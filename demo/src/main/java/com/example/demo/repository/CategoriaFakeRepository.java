package com.example.demo.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;


/**
 * Implementación de la falsa base de datos de categorías
 * Los productos se almacenan en un HashMap donde la llave es el idCategoria,
 * para permitir accesos directos por id.
 */
@Repository
public interface CategoriaFakeRepository extends JpaRepository<Categoria, Long> {
    Categoria findByName(String nombre);
}
