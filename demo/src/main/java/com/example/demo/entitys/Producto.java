package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un producto/plato gastronómico en el restaurante El Crustáceo Caribeño.
 * Representa la entidad Producto del Diagrama de Clases UML sin herencia.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity 
public class Producto {

    /** Identificador único del producto */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    /** Nombre comercial del plato */
    @Column (nullable = false)
    private String nombre;

    /** Precio en pesos colombianos (COP) */
    @Column (nullable = false)
    private double precio;

    /** Descripción detallada de los ingredientes y preparación del plato */
    private String descripcion;

    /** URL de la imagen representativa del plato (manejada como String) */
    private String imagenURL;

    /** Categoría del plato en el menú (Entrada, Plato Fuerte, Especialidades De La Casa) */
    @ManyToOne 
    private Categoria categoria;

    public Producto(String nombre, double precio, String descripcion, String imagenURL) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenURL = imagenURL;
    }
}
