package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.AdicionalRequestDTO;
import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;

public interface AdicionalService {

    /* Obtiene todos los adicionales disponibles */
    List<Adicional> obtenerTodosLosAdicionales();

    List<Adicional> obtenerTodosLosAdicionalesIncluyendoInactivos();

    /**
     * Busca un adicional por su ID.
     */
    Adicional obtenerAdicionalPorId(Long idAdicional);

    /* Obtiene los adicionales disponibles acorde a una categoría */
    List<Adicional> obtenerAdicionalesPorCategoria(Categoria categoria);

    List<Adicional> obtenerAdicionalesPorCategoriaIncluyendoInactivos(Categoria categoria);

    List<AdicionalDTO> obtenerAdicionalesDTO(boolean incluirInactivos, Long categoriaId);

    AdicionalDTO obtenerAdicionalDTOPorId(Long idAdicional);

    /* Guarda o actualiza un adicional */
    Adicional guardarAdicional(AdicionalRequestDTO adicional);

    void actualizarAdicional(Long idAdicional, AdicionalRequestDTO adicional);

    /* Elimina un adicional por ID */
    void eliminarAdicional(Long idAdicional);

    void activarAdicional(Long idAdicional);
}
