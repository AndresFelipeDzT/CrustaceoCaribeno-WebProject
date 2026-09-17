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
import lombok.ToString;

/**
 * Entidad intermedia (tabla puente) que relaciona un Pedido con un Producto,
 * indicando la cantidad solicitada y el precio unitario pactado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemPedido;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    @ManyToOne
    @ToString.Exclude
    private Pedido pedido;

    @ManyToOne
    private Producto producto;

    public ItemPedido(int cantidad, double precioUnitario, Pedido pedido, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.pedido = pedido;
        this.producto = producto;
    }

    public double getSubtotal() {
        return this.cantidad * this.precioUnitario;
    }
}
