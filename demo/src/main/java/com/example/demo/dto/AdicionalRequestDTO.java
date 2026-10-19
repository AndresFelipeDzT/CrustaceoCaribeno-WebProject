package com.example.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para adicionalrequest.
 */
@Data
@NoArgsConstructor
public class AdicionalRequestDTO {
    private String nombre;
    private double precio;
    private List<Long> categoriaIds;
}