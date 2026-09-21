package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;

public interface AdicionalService {

    /* Obtiene todos los adicionales disponibles */
    List<Adicional> obtenerTodosLosAdicionales();

    /* Busca un adicional por su ID */
    Adicional obtenerAdicionalPorId(Long idAdicional);

    /* Obtiene los adicionales disponibles acorde a una categoría */
    List<Adicional> obtenerAdicionalesPorCategoria(Categoria categoria);

    /* Guarda o actualiza un adicional */
    Adicional guardarAdicional(Adicional adicional);

    /* Elimina un adicional por ID */
    void eliminarAdicional(Long idAdicional);
}
