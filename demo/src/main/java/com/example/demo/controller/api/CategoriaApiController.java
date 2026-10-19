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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoriaDTO;
import com.example.demo.entitys.Categoria;
import com.example.demo.service.CategoriaService;

/**
 * Controlador REST para las operaciones de categoria.
 */
@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaApiController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaDTO> listar() {
        return categoriaService.obtenerCategoriasDTO();
    }

    @GetMapping("/{id}")
    public CategoriaDTO obtener(@PathVariable Long id) {
        return categoriaService.obtenerCategoriaDTOPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public CategoriaDTO obtenerPorNombre(@PathVariable String nombre) {
        return categoriaService.obtenerCategoriaDTOPorNombre(nombre);
    }

    @PostMapping
    public CategoriaDTO crear(@RequestBody Categoria categoria) {
        return categoriaService.guardarCategoriaDTO(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaDTO actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoriaDTO(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }

}
