package com.example.demo.errors;

/**
 * Excepción que informa un problema relacionado con producto no encontrado.
 */
public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(Long idProducto) {
        super("El producto con ID " + idProducto + " no fue encontrado en El Crustáceo Caribeño.");
    }
}
