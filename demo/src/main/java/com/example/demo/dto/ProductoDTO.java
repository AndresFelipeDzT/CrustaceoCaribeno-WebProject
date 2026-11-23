package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para producto.
 */
@Data
@NoArgsConstructor
public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private double precio;
    private String descripcion;
    private String imagenURL;
    private boolean activo;
    private int stock;
    private Long idCategoria;
    private String nombreCategoria;

    public ProductoDTO(Long idProducto, String nombre, double precio, String descripcion, String imagenURL,
            boolean activo, int stock, Long idCategoria, String nombreCategoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenURL = imagenURL;
        this.activo = activo;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }
}
