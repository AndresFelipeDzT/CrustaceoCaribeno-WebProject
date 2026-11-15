package com.example.demo.dto;

import com.example.demo.entitys.Operador;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos públicos de respuesta para un operador.
 */
@Data
@NoArgsConstructor
public class OperadorDTO {
    private Long idOperador;
    private String nombre;
    private String imagenURL;
    private String usuario;

    public OperadorDTO(Operador operador) {
        this.idOperador = operador.getIdOperador();
        this.nombre = operador.getNombre();
        this.imagenURL = operador.getImagenURL();
        this.usuario = operador.getUsuario();
    }
}
