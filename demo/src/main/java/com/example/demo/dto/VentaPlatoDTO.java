package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaPlatoDTO {
    private Long idProducto;
    private String nombre;
    private long unidades;
    private double valorPlatos;
    private long unidadesSinPrecio;
}
