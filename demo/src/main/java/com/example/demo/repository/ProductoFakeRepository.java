package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entitys.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación de la falsa base de datos de productos para el Sprint 2.
 * Precarga los 12 platos del menú de Figma con datos realistas del restaurante caribeño.
 *
 * Los productos se almacenan en un HashMap donde la llave es el idProducto,
 * para permitir accesos directos por id sin recorrer toda la colección.
 */
@Repository
public interface ProductoFakeRepository extends JpaRepository<Producto, Long> {}
