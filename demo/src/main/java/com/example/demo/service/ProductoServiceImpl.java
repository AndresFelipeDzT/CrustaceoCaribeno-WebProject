package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;
import com.example.demo.errors.CategoriaNotFoundException;
import com.example.demo.errors.ProductoNotFoundException;
import com.example.demo.repository.CategoriaFakeRepository;
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
    @Autowired
    CategoriaFakeRepository categoriaRepository;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public List<Producto> obtenerTodosLosProductosIncluyendoInactivos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> obtenerProductosPorCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Override
    public List<Producto> obtenerProductosPorCategoriaIncluyendoInactivos(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public List<ProductoDTO> obtenerProductosDTO(boolean incluirInactivos, Long categoriaId) {
        List<Producto> productos;
        if (categoriaId != null) {
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new CategoriaNotFoundException(categoriaId));
            productos = incluirInactivos
                    ? obtenerProductosPorCategoriaIncluyendoInactivos(categoria)
                    : obtenerProductosPorCategoria(categoria);
        } else {
            productos = incluirInactivos
                    ? obtenerTodosLosProductosIncluyendoInactivos()
                    : obtenerTodosLosProductos();
        }
        return productos.stream().map(this::aDTO).toList();
    }

    @Override
    public ProductoDTO obtenerProductoDTOPorId(Long idProducto) {
        return aDTO(obtenerProductoPorId(idProducto));
    }

    @Override
    public Producto obtenerProductoPorId(Long idProducto) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio.");
        }
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoNotFoundException(idProducto));
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Los datos del producto no pueden ser nulos.");
        }
        if (producto.getIdProducto() != null) {
            Producto existente = obtenerProductoPorId(producto.getIdProducto());
            producto.setActivo(existente.isActivo());
        }
        return productoRepository.save(producto);
    }

    @Override
    public void actualizarProducto(Long idProducto, Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Los datos del producto no pueden ser nulos.");
        }
        producto.setIdProducto(idProducto);
        guardarProducto(producto);
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

    @Override
    public void activarProducto(Long idProducto) {
        Producto producto = obtenerProductoPorId(idProducto);
        producto.setActivo(true);
        productoRepository.save(producto);
    }

    private ProductoDTO aDTO(Producto producto) {
        Categoria categoria = producto.getCategoria();
        return new ProductoDTO(
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDescripcion(),
                producto.getImagenURL(),
                producto.isActivo(),
                categoria == null ? null : categoria.getIdCategoria(),
                categoria == null ? null : categoria.getNombre());
    }
}
