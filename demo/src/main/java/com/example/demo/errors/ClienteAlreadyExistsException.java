package com.example.demo.errors;

public class ClienteAlreadyExistsException extends RuntimeException {

    public ClienteAlreadyExistsException(String email) {
        super("Ya existe un cliente registrado con el correo: " + email);
    }
}
