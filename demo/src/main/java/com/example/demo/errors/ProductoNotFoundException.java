package com.example.demo.errors;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(int idProducto) {
        super("El producto con ID " + idProducto + " no fue encontrado en El Crustáceo Caribeño.");
    }
}
