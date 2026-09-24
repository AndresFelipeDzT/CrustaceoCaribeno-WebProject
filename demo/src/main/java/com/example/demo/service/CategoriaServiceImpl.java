package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;
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
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    public void eliminarCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);
        if (categoria == null) {
            return;
        }
        for (Producto producto : productoRepository.findByCategoria(categoria)) {
            productoService.eliminarProducto(producto.getIdProducto());
            producto.setCategoria(null);
            productoRepository.save(producto);
        }
        categoria.getAdicionales().clear();
        categoriaRepository.delete(categoria);
    }
}
