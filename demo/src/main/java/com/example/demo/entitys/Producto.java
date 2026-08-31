package com.example.demo.entitys;

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
public class Producto {

    /** Identificador único del producto */
    private Integer idProducto;

    /** Nombre comercial del plato */
    private String nombre;

    /** Precio en pesos colombianos (COP) */
    private double precio;

    /** Descripción detallada de los ingredientes y preparación del plato */
    private String descripcion;

    /** URL de la imagen representativa del plato (manejada como String) */
    private String imagenURL;

    /** Categoría del plato en el menú (Entrada, Plato Fuerte, Especialidades De La Casa) */
    private Categoria categoria;
}
