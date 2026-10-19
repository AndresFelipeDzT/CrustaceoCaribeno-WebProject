package com.example.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdicionalRequestDTO {
    private String nombre;
    private double precio;
    private List<Long> categoriaIds;
}