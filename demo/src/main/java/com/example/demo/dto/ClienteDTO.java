package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para cliente.
 */
@Data
@NoArgsConstructor
public class ClienteDTO {
    private Long idCliente;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;
    private String password;
    private boolean activo;

    public ClienteDTO(Long idCliente, String nombre, String apellido, String correo, String telefono,
            String direccion, String password, boolean activo) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.password = password;
        this.activo = activo;
    }
}
