package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Operador;

/**
 * Contrato de las operaciones de servicio para operador.
 */
public interface OperadorService {

    List<Operador> obtenerTodosLosOperadores();

    Operador obtenerOperadorPorId(Long id);

    Operador guardarOperador(Operador operador);

    void actualizarOperador(Long id, Operador operador);

    void eliminarOperador(Long id);
}
