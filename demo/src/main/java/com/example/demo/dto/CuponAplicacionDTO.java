package com.example.demo.dto;

public record CuponAplicacionDTO(
        String codigoCupon,
        int porcentaje,
        double subtotal,
        double descuento,
        double domicilio,
        double total) {
}
