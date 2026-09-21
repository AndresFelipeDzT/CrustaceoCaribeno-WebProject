package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;
import com.example.demo.entitys.Adicional;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.repository.ProductoFakeRepository;

/**
 * Implementación de la capa de servicio para categorias de platos.
 * Maneja la lógica de negocio y se comunica con el repositorio.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService{

    @Autowired
    CategoriaFakeRepository categoriaRepository;
    @Autowired
    ProductoFakeRepository productoRepository;
    @Autowired
    AdicionalRepository adicionalRepository;
    @Autowired
    ProductoService productoService;

    @Override
    public List<Categoria> obtenerTodasLasCategorias(){
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaPorNombre(String nombre){
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    public void eliminarCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria).orElseThrow();
        for (Producto producto : productoRepository.findByCategoria(categoria)) {
            productoService.eliminarProducto(producto.getIdProducto());
            producto.setCategoria(null);
            productoRepository.save(producto);
        }
        for (Adicional adicional : adicionalRepository.findByCategoria(categoria)) {
            adicional.setActivo(false);
            adicional.setCategoria(null);
            adicionalRepository.save(adicional);
        }
        categoriaRepository.delete(categoria);
    }
}
