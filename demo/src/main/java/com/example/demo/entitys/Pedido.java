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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidad que representa un pedido realizado por un cliente en el restaurante.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Domiciliario domiciliario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<ItemPedido> items = new ArrayList<>();

    public Pedido(LocalDate fechaCreacion, LocalDate fechaEntrega, String estado, Cliente cliente, Domiciliario domiciliario) {
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.cliente = cliente;
        this.domiciliario = domiciliario;
    }

    public double getTotal() {
        return items.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }
}
