package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un categoria de plato en el restaurante El Crustáceo Caribeño.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    private int idCategoria;
    
   /** Categoría del plato en el menú (Entrada, Plato Fuerte, Especialidades De La Casa) */
    private String nombreCategoria;
}
