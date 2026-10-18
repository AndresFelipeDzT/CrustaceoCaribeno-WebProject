package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CategoriaDTO;
import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;
import com.example.demo.errors.CategoriaNotFoundException;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.repository.ProductoFakeRepository;

/**
 * Implementación de la capa de servicio para categorias de platos.
 * Maneja la lógica de negocio y se comunica con el repositorio.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaFakeRepository categoriaRepository;

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public List<CategoriaDTO> obtenerCategoriasDTO() {
        return obtenerTodasLasCategorias().stream().map(this::aDTO).toList();
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long idCategoria) {
        if (idCategoria == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio.");
        }
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException(idCategoria));
    }

    @Override
    public CategoriaDTO obtenerCategoriaDTOPorId(Long idCategoria) {
        return aDTO(obtenerCategoriaPorId(idCategoria));
    }

    @Override
    public Categoria obtenerCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        String nombreNormalizado = nombre.trim();
        Categoria categoria = categoriaRepository.findByNombre(nombreNormalizado);
        if (categoria == null) {
            throw new CategoriaNotFoundException(nombreNormalizado);
        }
        return categoria;
    }

    @Override
    public CategoriaDTO obtenerCategoriaDTOPorNombre(String nombre) {
        return aDTO(obtenerCategoriaPorNombre(nombre));
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Los datos de la categoría no pueden ser nulos.");
        }
        return categoriaRepository.save(categoria);
    }

    @Override
    public CategoriaDTO guardarCategoriaDTO(Categoria categoria) {
        return aDTO(guardarCategoria(categoria));
    }

    @Override
    public CategoriaDTO actualizarCategoriaDTO(Long idCategoria, Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Los datos de la categoría no pueden ser nulos.");
        }
        obtenerCategoriaPorId(idCategoria);
        categoria.setIdCategoria(idCategoria);
        return guardarCategoriaDTO(categoria);
    }

    @Override
    public void eliminarCategoria(Long idCategoria) {
        if (idCategoria == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio.");
        }
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException(idCategoria));
        // Desvincula productos y adicionales antes de borrar la categoría por sus relaciones JPA.
        for (Producto producto : productoRepository.findByCategoria(categoria)) {
            productoService.eliminarProducto(producto.getIdProducto());
            producto.setCategoria(null);
            productoRepository.save(producto);
        }
        categoria.getAdicionales().clear();
        categoriaRepository.delete(categoria);
    }

    private CategoriaDTO aDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getIdCategoria(), categoria.getNombre());
    }
}
