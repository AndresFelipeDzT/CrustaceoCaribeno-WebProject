package com.example.demo.errors;

/**
 * Excepción que informa un problema relacionado con adicional no encontrado.
 */
public class AdicionalNotFoundException extends RuntimeException {

    public AdicionalNotFoundException(Long idAdicional) {
        super("El adicional con ID " + idAdicional + " no fue encontrado.");
    }
}
