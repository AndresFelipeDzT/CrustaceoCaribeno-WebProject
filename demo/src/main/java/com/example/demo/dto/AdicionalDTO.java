package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdicionalDTO {
    private Long idAdicional;
    private String nombre;
    private double precio;
    private boolean activo;

    public AdicionalDTO(Long idAdicional, String nombre, double precio, boolean activo) {
        this.idAdicional = idAdicional;
        this.nombre = nombre;
        this.precio = precio;
        this.activo = activo;
    }
}
