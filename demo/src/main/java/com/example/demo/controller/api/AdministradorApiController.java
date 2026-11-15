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

import com.example.demo.dto.AdministradorDTO;
import com.example.demo.entitys.Administrador;
import com.example.demo.service.AdministradorService;

/**
 * Controlador REST para las operaciones de administrador.
 */
@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "http://localhost:4200")
public class AdministradorApiController {
    
    @Autowired
    private AdministradorService administradorService;


    @GetMapping
    public List<AdministradorDTO> listar() {
        return administradorService.obtenerTodosLosAdministradores().stream()
                .map(AdministradorDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public AdministradorDTO obtener(@PathVariable Long id) {
        return new AdministradorDTO(administradorService.obtenerAdministradorPorId(id));
    }

    @PostMapping
    public void crear(@RequestBody Administrador administrador) {
        administradorService.guardarAdministrador(administrador);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Administrador administrador) {
        administradorService.actualizarAdministrador(id, administrador);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
    }

    @PostMapping("/login")
    public AdministradorDTO login(@RequestBody Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException("Las credenciales son obligatorias.");
        }
        return new AdministradorDTO(
                administradorService.login(administrador.getUsuario(), administrador.getContrasena()));
    }
}
