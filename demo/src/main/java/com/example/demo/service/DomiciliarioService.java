package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Domiciliario;

public interface DomiciliarioService {

    List<Domiciliario> obtenerTodosLosDomiciliarios();

    Domiciliario obtenerDomiciliarioPorId(Long idDomiciliario);

    Domiciliario guardarDomiciliario(Domiciliario domiciliario);

    Domiciliario actualizarDomiciliario(Long idDomiciliario, Domiciliario domiciliario);

    Domiciliario cambiarDisponibilidad(Long idDomiciliario, Boolean disponible);

    void eliminarDomiciliario(Long idDomiciliario);
}
