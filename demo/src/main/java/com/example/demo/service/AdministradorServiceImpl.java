package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Administrador;
import com.example.demo.repository.AdministradorRepository;

/**
 * Implementación de la lógica de servicio para administrador.
 */
@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired 
    private AdministradorRepository administradorRepository;


    @Override
    public List<Administrador> obtenerTodosLosAdministradores() {
        return administradorRepository.findAll();
    }

    @Override
    public Administrador obtenerAdministradorPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del administrador es obligatorio.");
        }
        return administradorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el administrador con ID " + id + "."));
    }

    @Override
    public Administrador guardarAdministrador(Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException("Los datos del administrador no pueden ser nulos.");
        }
        return administradorRepository.save(administrador);
    }

    @Override
    public void actualizarAdministrador(Long id, Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException("Los datos del administrador no pueden ser nulos.");
        }

        Administrador administradorExistente = obtenerAdministradorPorId(id);
        if (administrador.getUsuario() != null && !administrador.getUsuario().isBlank()) {
            administradorExistente.setUsuario(administrador.getUsuario());
        }
        if (administrador.getContrasena() != null && !administrador.getContrasena().isBlank()) {
            administradorExistente.setContrasena(administrador.getContrasena());
        }
        administradorRepository.save(administradorExistente);
    }

    @Override
    public void eliminarAdministrador(Long id) {
        administradorRepository.delete(obtenerAdministradorPorId(id));
    }

    @Override
    public Administrador login(String usuario, String contrasena) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu usuario.");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu contraseña.");
        }
        return administradorRepository.findByUsuarioIgnoreCase(usuario.trim())
                .filter(administrador -> contrasena.equals(administrador.getContrasena()))
                .orElseThrow(() -> new IllegalArgumentException("El usuario o la contraseña no son correctos."));
    }
}
