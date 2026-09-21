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
@IdClass(ProductoAdicionalId.class)
public class ProductoAdicional {

    @Id
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    @ToString.Exclude
    private Producto producto;

    @Id
    @ManyToOne
    @JoinColumn(name = "idAdicional", nullable = false)
    @ToString.Exclude
    private Adicional adicional;

    public ProductoAdicional(Producto producto, Adicional adicional) {
        this.producto = producto;
        this.adicional = adicional;
    }
}
