package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Producto;
import com.example.demo.repository.ProductoFakeRepository;

import java.util.List;

/**
 * Implementación de la capa de servicio para productos.
 * Maneja la lógica de negocio y se comunica con el repositorio.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoFakeRepository productoRepository;
    
    public ProductoServiceImpl(ProductoFakeRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(int idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public boolean eliminarProducto(int idProducto) {
        return productoRepository.deleteById(idProducto);
    }
}