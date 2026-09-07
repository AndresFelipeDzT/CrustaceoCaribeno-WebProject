package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa a un cliente registrado en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity 
public class Cliente {

    /** Identificador único del cliente */
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCliente;

    /** Nombre del cliente */
    @Column (nullable = false)
    private String nombre;

    /** Apellido del cliente */
    @Column (nullable = false)
    private String apellido;

    /** Correo electrónico */
    @Column (nullable = false, unique = true)
    private String correo;

    /** Número telefónico de contacto */
    private String telefono;

    /** Dirección de entrega o contacto del cliente */
    private String direccion;

    /** Contraseña del cliente */
    @Column (nullable = false)
    private String password;

    public Cliente(String nombre, String apellido, String correo, String telefono, String direccion, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.password = password;
    }
}
