package com.example.demo.errors;

/**
 * Excepción que informa un problema relacionado con domiciliario no encontrado.
 */
public class DomiciliarioNotFoundException extends RuntimeException {

    public DomiciliarioNotFoundException(Long idDomiciliario) {
        super("El domiciliario con ID " + idDomiciliario + " no fue encontrado.");
    }
}
