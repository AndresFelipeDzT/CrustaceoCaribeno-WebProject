package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    @ManyToMany(mappedBy = "adicionales")
    @ToString.Exclude
    @Builder.Default
    private List<Categoria> categorias = new ArrayList<>();

    public List<Categoria> getCategoriasDisponibles() {
        return categorias;
    }

    public void setCategoriasDisponibles(List<Categoria> categoriasDisponibles) {
        this.categorias = categoriasDisponibles;
    }

    @ManyToMany(mappedBy = "adicionalesDisponibles")
    @ToString.Exclude
    @Builder.Default
    private List<Producto> productosDisponibles = new ArrayList<>();

    public Adicional(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.activo = true;
    }
}
