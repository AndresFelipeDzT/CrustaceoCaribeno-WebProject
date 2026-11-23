package com.example.demo.service;

import java.time.LocalDate;
import com.example.demo.dto.ResumenVentasDTO;

public interface EstadisticasVentasService {
    ResumenVentasDTO consultar(LocalDate desde, LocalDate hasta);
}
