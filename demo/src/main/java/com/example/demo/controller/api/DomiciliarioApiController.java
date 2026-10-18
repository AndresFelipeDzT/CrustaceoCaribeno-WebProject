package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.Domiciliario;
import com.example.demo.service.DomiciliarioService;

@RestController
@RequestMapping("/api/domiciliarios")
@CrossOrigin(origins = "http://localhost:4200")
public class DomiciliarioApiController {

    @Autowired
    private DomiciliarioService domiciliarioService;

    @GetMapping
    public List<Domiciliario> listar() {
        return domiciliarioService.obtenerTodosLosDomiciliarios();
    }

    @GetMapping("/{id}")
    public Domiciliario obtener(@PathVariable Long id) {
        return domiciliarioService.obtenerDomiciliarioPorId(id);
    }

    @PostMapping
    public Domiciliario crear(@RequestBody Domiciliario domiciliario) {
        return domiciliarioService.guardarDomiciliario(domiciliario);
    }

    @PutMapping("/{id}")
    public Domiciliario actualizar(@PathVariable Long id, @RequestBody Domiciliario domiciliario) {
        return domiciliarioService.actualizarDomiciliario(id, domiciliario);
    }

    @PatchMapping("/{id}/activar")
    public Domiciliario activar(@PathVariable Long id) {
        return domiciliarioService.cambiarDisponibilidad(id, true);
    }

    @PatchMapping("/{id}/desactivar")
    public Domiciliario desactivar(@PathVariable Long id) {
        return domiciliarioService.cambiarDisponibilidad(id, false);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        domiciliarioService.eliminarDomiciliario(id);
    }
}
