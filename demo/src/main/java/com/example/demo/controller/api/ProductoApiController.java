package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.Producto;
import com.example.demo.entitys.Categoria;
import com.example.demo.service.ProductoService;
import com.example.demo.service.CategoriaService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoApiController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Producto> listar(
            @RequestParam(defaultValue = "false") boolean incluirInactivos,
            @RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(categoriaId);
            return incluirInactivos
                    ? productoService.obtenerProductosPorCategoriaIncluyendoInactivos(categoria)
                    : productoService.obtenerProductosPorCategoria(categoria);
        }
        return incluirInactivos
                ? productoService.obtenerTodosLosProductosIncluyendoInactivos()
                : productoService.obtenerTodosLosProductos();
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Producto producto) {
        productoService.guardarProducto(producto);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        productoService.actualizarProducto(id, producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }

    @PutMapping("/{id}/activar")
    public void activar(@PathVariable Long id) {
        productoService.activarProducto(id);
    }
}
