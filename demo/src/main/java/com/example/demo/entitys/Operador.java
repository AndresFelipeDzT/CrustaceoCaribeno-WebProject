package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un operador del sistema.
 */
@Data
@NoArgsConstructor
@Entity
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOperador;

    @Column(nullable = false, length = 50)
    private String nombre;

    private String imagenURL;

    @Column(nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(nullable = false, length = 50)
    private String contrasena;

    public Operador(Long idOperador, String nombre, String imagenURL, String usuario, String contrasena) {
        this.idOperador = idOperador;
        this.nombre = nombre;
        this.imagenURL = imagenURL;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Operador(String nombre, String usuario, String contrasena) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Operador(String nombre, String imagenURL, String usuario, String contrasena) {
        this.nombre = nombre;
        this.imagenURL = imagenURL;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
}
