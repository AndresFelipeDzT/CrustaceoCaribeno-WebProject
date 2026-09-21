package com.example.demo.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@IdClass(ItemCarritoAdicionalId.class)
public class ItemCarritoAdicional {

    @Id
    @ManyToOne
    @JoinColumn(name = "idItemCarrito", nullable = false)
    @ToString.Exclude
    private ItemCarrito itemCarrito;

    @Id
    @ManyToOne
    @JoinColumn(name = "idAdicional", nullable = false)
    @ToString.Exclude
    private Adicional adicional;

    public ItemCarritoAdicional(ItemCarrito itemCarrito, Adicional adicional) {
        this.itemCarrito = itemCarrito;
        this.adicional = adicional;
    }
}
