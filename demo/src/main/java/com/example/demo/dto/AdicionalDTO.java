package com.example.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para adicional.
 */
@Data
@NoArgsConstructor
public class AdicionalDTO {
    private Long idAdicional;
    private String nombre;
    private double precio;
    private boolean activo;
    private List<Long> categoriaIds;

    public AdicionalDTO(Long idAdicional, String nombre, double precio, boolean activo) {
        this(idAdicional, nombre, precio, activo, List.of());
    }

    public AdicionalDTO(Long idAdicional, String nombre, double precio, boolean activo, List<Long> categoriaIds) {
        this.idAdicional = idAdicional;
        this.nombre = nombre;
        this.precio = precio;
        this.activo = activo;
        this.categoriaIds = categoriaIds;
    }
}
