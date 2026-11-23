package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVentasDTO {
    private int pedidosEntregados;
    private long unidadesVendidas;
    private double totalCobrado;
    private int pedidosSinImporte;
    private List<VentaPlatoDTO> platos;
}
