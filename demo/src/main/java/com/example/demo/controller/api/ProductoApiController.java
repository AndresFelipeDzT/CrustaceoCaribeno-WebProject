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
import com.example.demo.dto.ProductoDTO;
import com.example.demo.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoApiController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoDTO> listar(
            @RequestParam(defaultValue = "false") boolean incluirInactivos,
            @RequestParam(required = false) Long categoriaId) {
        return productoService.obtenerProductosDTO(incluirInactivos, categoriaId);
    }

    @GetMapping("/{id}")
    public ProductoDTO obtener(@PathVariable Long id) {
        return productoService.obtenerProductoDTOPorId(id);
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
