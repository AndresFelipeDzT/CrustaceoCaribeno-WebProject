package com.example.demo.errors;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(String nombre) {
        super("La categoría \"" + nombre + "\" no fue encontrada.");
    }

    public CategoriaNotFoundException(Long idCategoria) {
        super("La categoría con ID " + idCategoria + " no fue encontrada.");
    }
}
