package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CategoriaDTO;
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

    List<CategoriaDTO> obtenerCategoriasDTO();

    Categoria obtenerCategoriaPorId(Long idCategoria);

    CategoriaDTO obtenerCategoriaDTOPorId(Long idCategoria);

    /**
     * Busca una categoria por su nombre.
     * @param nombre nombre de la categoria.
     * @return La categoría encontrada.
     */
    Categoria obtenerCategoriaPorNombre(String nombre);

    CategoriaDTO obtenerCategoriaDTOPorNombre(String nombre);

    Categoria guardarCategoria(Categoria categoria);

    CategoriaDTO guardarCategoriaDTO(Categoria categoria);

    CategoriaDTO actualizarCategoriaDTO(Long idCategoria, Categoria categoria);

    void eliminarCategoria(Long idCategoria);
}
