package com.example.demo.errors;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(Long idProducto) {
        super("El producto con ID " + idProducto + " no fue encontrado en El Crustáceo Caribeño.");
    }
}
