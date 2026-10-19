package com.example.demo.errors;

/**
 * Excepción que informa un problema relacionado con cliente ya existente.
 */
public class ClienteAlreadyExistsException extends RuntimeException {

    public ClienteAlreadyExistsException(String email) {
        super("Ya existe un cliente registrado con el correo: " + email);
    }
}
