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
@IdClass(ItemPedidoAdicionalId.class)
public class ItemPedidoAdicional {

    @Id
    @ManyToOne
    @JoinColumn(name = "idItemPedido", nullable = false)
    @ToString.Exclude
    private ItemPedido itemPedido;

    @Id
    @ManyToOne
    @JoinColumn(name = "idAdicional", nullable = false)
    @ToString.Exclude
    private Adicional adicional;

    public ItemPedidoAdicional(ItemPedido itemPedido, Adicional adicional) {
        this.itemPedido = itemPedido;
        this.adicional = adicional;
    }
}
