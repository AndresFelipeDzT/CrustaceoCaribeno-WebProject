package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entitys.Producto;
import com.example.demo.entitys.Categoria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

/**
 * Implementación de la falsa base de datos de productos para el Sprint 2.
 * Precarga los 12 platos del menú de Figma con datos realistas del restaurante caribeño.
 *
 * Los productos se almacenan en un HashMap donde la llave es el idProducto,
 * para permitir accesos directos por id sin recorrer toda la colección.
 */
@Repository
public interface ProductoFakeRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);

    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.nombre")
    List<Producto> buscarProductosActivos();

    @Query("SELECT p FROM Producto p WHERE p.activo = true "
            + "AND LOWER(p.categoria.nombre) = LOWER(:nombreCategoria) ORDER BY p.nombre")
    List<Producto> buscarActivosPorNombreCategoria(@Param("nombreCategoria") String nombreCategoria);

    @Query("SELECT p FROM Producto p WHERE p.activo = true "
            + "AND p.precio BETWEEN :precioMinimo AND :precioMaximo ORDER BY p.precio")
    List<Producto> buscarActivosEnRangoDePrecio(
            @Param("precioMinimo") double precioMinimo,
            @Param("precioMaximo") double precioMaximo);

    @Query("SELECT p FROM Producto p WHERE p.activo = true "
            + "AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Producto> buscarActivosPorNombre(@Param("termino") String termino);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precio >= :precioMinimo")
    List<Producto> buscarActivosDesdePrecio(@Param("precioMinimo") double precioMinimo);
}
