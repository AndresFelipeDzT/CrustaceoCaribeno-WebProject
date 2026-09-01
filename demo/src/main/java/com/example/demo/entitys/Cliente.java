package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa a un cliente registrado en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    /** Identificador único del cliente */
    private Integer idCliente;

    /** Nombre completo del cliente */
    private String nombreCompleto;

    /** Correo electrónico */
    private String correo;

    /** Número telefónico de contacto */
    private String telefono;

    /** Dirección de entrega o contacto del cliente */
    private String direccion;

    /** Contraseña del cliente */
    private String password;
}
