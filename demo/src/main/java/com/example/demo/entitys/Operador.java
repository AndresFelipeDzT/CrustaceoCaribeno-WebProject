package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity userEntity;

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
