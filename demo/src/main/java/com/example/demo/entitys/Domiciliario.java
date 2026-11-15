package com.example.demo.entitys;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Entidad que representa a un domiciliario encargado de las entregas de pedidos.
 */
@Data
@Builder
@Entity
public class Domiciliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDomiciliario;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 15)
    private String celular;

    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(nullable = false, unique = true, length = 70)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    private String imagenURL;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity userEntity;

    @OneToMany(mappedBy = "domiciliario")
    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    private List<Pedido> pedidos = new ArrayList<>();

    Domiciliario(Long idDomiciliario, String nombre, String celular, String cedula, String username, String password,
            String imagenURL, Boolean disponible, Boolean activo, UserEntity userEntity, List<Pedido> pedidos) {
        this.idDomiciliario = idDomiciliario;
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.username = username;
        this.password = password;
        this.imagenURL = imagenURL;
        this.disponible = disponible;
        this.activo = activo;
        this.userEntity = userEntity;
        this.pedidos = pedidos;
    }

    public Domiciliario() {
        this.pedidos = new ArrayList<>();
    }

    public Domiciliario(String nombre, String celular, String cedula, Boolean disponible) {
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.disponible = disponible;
        this.activo = true;
    }

    public Domiciliario(String nombre, String celular, String cedula, String imagenURL, Boolean disponible) {
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.imagenURL = imagenURL;
        this.disponible = disponible;
        this.activo = true;
    }

    public Domiciliario(Long idDomiciliario, String nombre, String celular, String cedula, Boolean disponible,
            List<Pedido> pedidos) {
        this.idDomiciliario = idDomiciliario;
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.disponible = disponible;
        this.activo = true;
        this.pedidos = pedidos;
    }

    public Domiciliario(Long idDomiciliario, String nombre, String celular, String cedula, String imagenURL,
            Boolean disponible, List<Pedido> pedidos) {
        this.idDomiciliario = idDomiciliario;
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.imagenURL = imagenURL;
        this.disponible = disponible;
        this.activo = true;
        this.pedidos = pedidos;
    }

    public Domiciliario(Long idDomiciliario, String nombre, String celular, String cedula, String imagenURL,
            Boolean disponible, Boolean activo, List<Pedido> pedidos) {
        this.idDomiciliario = idDomiciliario;
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.imagenURL = imagenURL;
        this.disponible = disponible;
        this.activo = activo;
        this.pedidos = pedidos;
    }
}
