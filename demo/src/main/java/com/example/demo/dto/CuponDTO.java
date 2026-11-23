package com.example.demo.dto;

import java.time.LocalDate;

public record CuponDTO(String codigo, int porcentaje, LocalDate fechaEmision) {
}
