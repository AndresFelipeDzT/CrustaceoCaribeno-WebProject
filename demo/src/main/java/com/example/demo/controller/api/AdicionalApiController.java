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

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.AdicionalRequestDTO;
import com.example.demo.service.AdicionalService;

/**
 * Controlador REST para las operaciones de adicional.
 */
@RestController
@RequestMapping("/api/adicionales")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalApiController {

    @Autowired
    private AdicionalService adicionalService;

    @GetMapping
    public List<AdicionalDTO> listar(
            @RequestParam(defaultValue = "false") boolean incluirInactivos,
            @RequestParam(required = false) Long categoriaId) {
        return adicionalService.obtenerAdicionalesDTO(incluirInactivos, categoriaId);
    }

    @GetMapping("/{id}")
    public AdicionalDTO obtener(@PathVariable Long id) {
        return adicionalService.obtenerAdicionalDTOPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody AdicionalRequestDTO adicional) {
        adicionalService.guardarAdicional(adicional);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody AdicionalRequestDTO adicional) {
        adicionalService.actualizarAdicional(id, adicional);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        adicionalService.eliminarAdicional(id);
    }

    @PutMapping("/{id}/activar")
    public void activar(@PathVariable Long id) {
        adicionalService.activarAdicional(id);
    }

}
