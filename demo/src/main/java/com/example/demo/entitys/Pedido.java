package com.example.demo.entitys;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Entidad que representa un pedido realizado por un cliente en el restaurante.
 */
@Data
@Builder
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    private LocalDate fechaEntrega;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false, columnDefinition = "double precision default 0")
    private double descuento;

    @Column(nullable = false, columnDefinition = "double precision default 0")
    private double domicilio;

    @Column(length = 40)
    private String codigoCupon;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Domiciliario domiciliario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<ItemPedido> items = new ArrayList<>();

    public Pedido() {
        this.items = new ArrayList<>();
    }

    public Pedido(LocalDate fechaCreacion, LocalDate fechaEntrega, String estado, Cliente cliente, Domiciliario domiciliario) {
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.cliente = cliente;
        this.domiciliario = domiciliario;
    }

    public Pedido(Long idPedido, LocalDate fechaCreacion, LocalDate fechaEntrega, String estado, Cliente cliente,
            Domiciliario domiciliario, List<ItemPedido> items) {
        this.idPedido = idPedido;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.cliente = cliente;
        this.domiciliario = domiciliario;
        this.items = items;
    }

    public Pedido(Long idPedido, LocalDate fechaCreacion, LocalDate fechaEntrega, String estado,
            double descuento, double domicilio, String codigoCupon, Cliente cliente,
            Domiciliario domiciliario, List<ItemPedido> items) {
        this.idPedido = idPedido;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.descuento = descuento;
        this.domicilio = domicilio;
        this.codigoCupon = codigoCupon;
        this.cliente = cliente;
        this.domiciliario = domiciliario;
        this.items = items;
    }

    public double getTotal() {
        return getSubtotal() - descuento + domicilio;
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }
}
