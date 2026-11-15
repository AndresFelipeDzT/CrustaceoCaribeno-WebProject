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

import com.example.demo.dto.DomiciliarioDTO;
import com.example.demo.entitys.Domiciliario;
import com.example.demo.service.DomiciliarioService;

/**
 * Controlador REST para las operaciones de domiciliario.
 */
@RestController
@RequestMapping("/api/domiciliarios")
@CrossOrigin(origins = "http://localhost:4200")
public class DomiciliarioApiController {

    @Autowired
    private DomiciliarioService domiciliarioService;

    @GetMapping
    public List<DomiciliarioDTO> listar() {
        return domiciliarioService.obtenerTodosLosDomiciliarios().stream()
                .map(DomiciliarioDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public DomiciliarioDTO obtener(@PathVariable Long id) {
        return new DomiciliarioDTO(domiciliarioService.obtenerDomiciliarioPorId(id));
    }

    @PostMapping
    public DomiciliarioDTO crear(@RequestBody Domiciliario domiciliario) {
        return new DomiciliarioDTO(domiciliarioService.guardarDomiciliario(domiciliario));
    }

    @PutMapping("/{id}")
    public DomiciliarioDTO actualizar(@PathVariable Long id, @RequestBody Domiciliario domiciliario) {
        return new DomiciliarioDTO(domiciliarioService.actualizarDomiciliario(id, domiciliario));
    }

    @PutMapping("/{id}/activar")
    public DomiciliarioDTO activar(@PathVariable Long id) {
        return new DomiciliarioDTO(domiciliarioService.cambiarDisponibilidad(id, true));
    }

    @PutMapping("/{id}/desactivar")
    public DomiciliarioDTO desactivar(@PathVariable Long id) {
        return new DomiciliarioDTO(domiciliarioService.cambiarDisponibilidad(id, false));
    }

    @PutMapping("/{id}/estado/activar")
    public DomiciliarioDTO activarEstado(@PathVariable Long id) {
        return new DomiciliarioDTO(domiciliarioService.cambiarEstadoActivo(id, true));
    }

    @PutMapping("/{id}/estado/desactivar")
    public DomiciliarioDTO desactivarEstado(@PathVariable Long id) {
        return new DomiciliarioDTO(domiciliarioService.cambiarEstadoActivo(id, false));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        domiciliarioService.eliminarDomiciliario(id);
    }
}
