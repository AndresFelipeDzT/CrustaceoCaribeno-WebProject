package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Domiciliario;

/**
 * Contrato de las operaciones de servicio para domiciliario.
 */
public interface DomiciliarioService {

    List<Domiciliario> obtenerTodosLosDomiciliarios();

    Domiciliario obtenerDomiciliarioPorId(Long idDomiciliario);

    Domiciliario guardarDomiciliario(Domiciliario domiciliario);

    Domiciliario actualizarDomiciliario(Long idDomiciliario, Domiciliario domiciliario);

    Domiciliario cambiarDisponibilidad(Long idDomiciliario, Boolean disponible);

    Domiciliario cambiarEstadoActivo(Long idDomiciliario, Boolean activo);

    void eliminarDomiciliario(Long idDomiciliario);
}
