package com.example.demo.entitys;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @OneToOne
    @JoinColumn(name = "idCliente", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
    }

    public double getDomicilio() {
        return items.isEmpty() ? 0 : 5000;
    }

    public double getTotal() {
        return getSubtotal() + getDomicilio();
    }
}
