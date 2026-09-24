package com.example.demo.entitys;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemCarrito;

    @Column(nullable = false)
    private int cantidad;

    @ManyToOne(optional = false)
    private Carrito carrito;

    @ManyToOne(optional = false)
    private Producto producto;

    @ManyToMany
    @ToString.Exclude
    private List<Adicional> adicionales = new ArrayList<>();

    public ItemCarrito(int cantidad, Carrito carrito, Producto producto) {
        this.cantidad = cantidad;
        this.carrito = carrito;
        this.producto = producto;
    }

    public double getSubtotal() {
        return cantidad * producto.getPrecio();
    }
}
