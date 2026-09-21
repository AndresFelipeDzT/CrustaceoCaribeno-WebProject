package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un adicional o acompañamiento disponible para los platos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Adicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdicional;

    @Column(nullable = false, length = 70)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @ManyToOne
    private Categoria categoria;

    public Adicional(String nombre, double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }
}
