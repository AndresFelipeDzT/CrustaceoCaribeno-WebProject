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

import com.example.demo.entitys.Adicional;
import com.example.demo.service.AdicionalService;
import com.example.demo.service.CategoriaService;

@RestController
@RequestMapping("/api/adicionales")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalApiController {

    @Autowired
    private AdicionalService adicionalService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Adicional> listar(@RequestParam(required = false) Long categoriaId) {
        return categoriaId == null
                ? adicionalService.obtenerTodosLosAdicionales()
                : adicionalService.obtenerAdicionalesPorCategoria(categoriaService.obtenerCategoriaPorId(categoriaId));
    }

    @GetMapping("/{id}")
    public Adicional obtener(@PathVariable Long id) {
        return adicionalService.obtenerAdicionalPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Adicional adicional) {
        adicionalService.guardarAdicional(adicional);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Adicional adicional) {
        adicionalService.actualizarAdicional(id, adicional);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        adicionalService.eliminarAdicional(id);
    }
}
