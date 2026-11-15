package com.example.demo.dto;

import com.example.demo.entitys.Administrador;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos públicos de respuesta para un administrador.
 */
@Data
@NoArgsConstructor
public class AdministradorDTO {
    private Long idAdministrador;
    private String usuario;

    public AdministradorDTO(Administrador administrador) {
        this.idAdministrador = administrador.getIdAdministrador();
        this.usuario = administrador.getUsuario();
    }
}
