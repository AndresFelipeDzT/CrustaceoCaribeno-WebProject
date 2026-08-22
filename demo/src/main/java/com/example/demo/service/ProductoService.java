package com.example.demo.service;
import java.util.List;

import com.example.demo.entitys.Producto;

/**
 * Interfaz que define las operaciones de negocio para la gestión de productos/comidas.
 */
public interface ProductoService {

    /**
     * Obtiene el listado de todas las comidas del restaurante.
     * @return Lista completa de productos.
     */
    List<Producto> obtenerTodosLosProductos();

    /**
     * Busca un plato por su identificador.
     * @param idProducto ID del producto.
     * @return El producto encontrado o null si no existe.
     */
    Producto obtenerProductoPorId(int idProducto);

    /**
     * Registra o actualiza un producto.
     * @param producto Producto a registrar.
     * @return Producto registrado.
     */
    Producto guardarProducto(Producto producto);

    /**
     * Elimina un producto del catálogo.
     * @param idProducto ID del producto.
     * @return true si se eliminó, false en caso contrario.
     */
    boolean eliminarProducto(int idProducto);
}