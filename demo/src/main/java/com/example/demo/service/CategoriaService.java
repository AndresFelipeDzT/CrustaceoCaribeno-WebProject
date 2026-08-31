package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Categoria;

/**
 * Interfaz que define las operaciones de negocio para la gestión de categorias de platos.
 */
public interface CategoriaService {

    /**
     * Obtiene el listado de todas las categorias de platos del restaurante.
     * @return Lista completa de categorias.
     */
    List<Categoria> obtenerTodasLasCategorias();
    /**
     * Busca una categoria por su nombre.
     * @param nombre nombre de la categoria.
     * @return La categoria encontrada o null si no existe.
     */
    Categoria obtenerCategoriaPorNombre(String nombre);
}
