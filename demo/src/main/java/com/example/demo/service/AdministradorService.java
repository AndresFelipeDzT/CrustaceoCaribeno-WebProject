package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Administrador;

public interface AdministradorService {

    List<Administrador> obtenerTodosLosAdministradores();

    Administrador obtenerAdministradorPorId(Long id);

    Administrador guardarAdministrador(Administrador administrador);

    void actualizarAdministrador(Long id, Administrador administrador);

    void eliminarAdministrador(Long id);
}
