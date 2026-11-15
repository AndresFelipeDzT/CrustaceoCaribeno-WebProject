package com.example.demo.dto;

import com.example.demo.entitys.Domiciliario;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos públicos de respuesta para un domiciliario.
 */
@Data
@NoArgsConstructor
public class DomiciliarioDTO {
    private Long idDomiciliario;
    private String nombre;
    private String celular;
    private String cedula;
    private String username;
    private String imagenURL;
    private Boolean disponible;
    private Boolean activo;

    public DomiciliarioDTO(Domiciliario domiciliario) {
        this.idDomiciliario = domiciliario.getIdDomiciliario();
        this.nombre = domiciliario.getNombre();
        this.celular = domiciliario.getCelular();
        this.cedula = domiciliario.getCedula();
        this.username = domiciliario.getUsername();
        this.imagenURL = domiciliario.getImagenURL();
        this.disponible = domiciliario.getDisponible();
        this.activo = domiciliario.getActivo();
    }
}
