package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Producto;
import com.example.demo.errors.ProductoNotFoundException;
import com.example.demo.repository.ProductoFakeRepository;
import com.example.demo.repository.ItemCarritoRepository;
import com.example.demo.entitys.ItemCarrito;

import java.util.List;

/**
 * Implementación de la capa de servicio para productos.
 * Maneja la lógica de negocio y se comunica con el repositorio.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoFakeRepository productoRepository;
    @Autowired
    ItemCarritoRepository itemCarritoRepository;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public List<Producto> obtenerTodosLosProductosIncluyendoInactivos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoNotFoundException(idProducto));
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        if (producto.getIdProducto() != null) {
            Producto existente = obtenerProductoPorId(producto.getIdProducto());
            producto.setActivo(existente.isActivo());
        }
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long idProducto) {
        Producto producto = obtenerProductoPorId(idProducto);
        for (ItemCarrito itemCarrito : itemCarritoRepository.findByProducto(producto)) {
            itemCarritoRepository.delete(itemCarrito);
        }
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
