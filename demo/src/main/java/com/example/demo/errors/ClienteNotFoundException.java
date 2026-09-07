package com.example.demo.errors;

/**
 * Excepción de negocio para solicitudes que hacen referencia a un cliente inexistente.
 */
public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(int idCliente) {
        super("El cliente con ID " + idCliente + " no fue encontrado en El Crustáceo Caribeño.");
    }
}
