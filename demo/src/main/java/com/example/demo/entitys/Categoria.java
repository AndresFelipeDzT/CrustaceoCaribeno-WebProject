package com.example.demo.entitys;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase que representa un categoria de plato en el restaurante El Crustáceo Caribeño.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity 
public class Categoria {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long idCategoria;
    
   /** Categoría del plato en el menú (Entrada, Plato Fuerte, Especialidades De La Casa) */
    @Column (nullable = false, unique = true, length = 50)
    private String nombre;
    
    @OneToMany (mappedBy = "categoria")
    private List<Producto> productos;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
