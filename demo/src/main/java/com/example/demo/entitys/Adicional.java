package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @ManyToOne
    private Categoria categoria;

    @OneToMany(mappedBy = "adicional")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<ProductoAdicional> productosDisponibles = new ArrayList<>();

    public Adicional(String nombre, double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.activo = true;
    }
}
