package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para categoria.
 */
@Data
@NoArgsConstructor
public class CategoriaDTO {
    private Long idCategoria;
    private String nombre;

    public CategoriaDTO(Long idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }
}
