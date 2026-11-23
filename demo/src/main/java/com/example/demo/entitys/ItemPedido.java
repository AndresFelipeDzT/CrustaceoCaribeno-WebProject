package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad intermedia (tabla puente) que relaciona un Pedido con un Producto,
 * indicando la cantidad solicitada y el precio unitario pactado.
 */
@Data
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemPedido;

    @Column(nullable = false)
    private int cantidad;

    private Double precioUnitarioVenta;
    private Double subtotalVenta;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Pedido pedido;

    @ManyToOne
    private Producto producto;

    @ManyToMany
    @ToString.Exclude
    private List<Adicional> adicionales = new ArrayList<>();

    public ItemPedido() {
        this.adicionales = new ArrayList<>();
    }

    public ItemPedido(int cantidad, Pedido pedido, Producto producto) {
        this.cantidad = cantidad;
        this.pedido = pedido;
        this.producto = producto;
    }

    @Builder
    public ItemPedido(Long idItemPedido, int cantidad, Pedido pedido, Producto producto,
            List<Adicional> adicionales) {
        this.idItemPedido = idItemPedido;
        this.cantidad = cantidad;
        this.pedido = pedido;
        this.producto = producto;
        this.adicionales = adicionales == null ? new ArrayList<>() : adicionales;
    }

    @PrePersist
    public void guardarImportesDeVenta() {
        if (producto != null) {
            precioUnitarioVenta = producto.getPrecio();
            subtotalVenta = calcularSubtotal();
        }
    }

    public double getSubtotal() {
        return subtotalVenta != null ? subtotalVenta : calcularSubtotal();
    }

    private double calcularSubtotal() {
        double subtotalAdicionales = this.adicionales == null ? 0
                : this.adicionales.stream().mapToDouble(Adicional::getPrecio).sum();
        return (this.cantidad * this.producto.getPrecio()) + this.cantidad *subtotalAdicionales;
    }
}
