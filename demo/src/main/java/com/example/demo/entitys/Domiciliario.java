package com.example.demo.entitys;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidad que representa a un domiciliario encargado de las entregas de pedidos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private Boolean disponible;

    @OneToMany(mappedBy = "domiciliario")
    @ToString.Exclude
    @Builder.Default
    private List<Pedido> pedidos = new ArrayList<>();

    public Domiciliario(String nombre, String celular, String cedula, Boolean disponible) {
        this.nombre = nombre;
        this.celular = celular;
        this.cedula = cedula;
        this.disponible = disponible;
    }
}
