package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void eliminarCategoria(Long idCategoria) {
        if (idCategoria == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio.");
        }
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException(idCategoria));
        for (Producto producto : productoRepository.findByCategoria(categoria)) {
            productoService.eliminarProducto(producto.getIdProducto());
            producto.setCategoria(null);
            productoRepository.save(producto);
        }
        categoria.getAdicionales().clear();
        categoriaRepository.delete(categoria);
    }
}
