package com.example.demo.errors;

public class DomiciliarioNotFoundException extends RuntimeException {

    public DomiciliarioNotFoundException(Long idDomiciliario) {
        super("El domiciliario con ID " + idDomiciliario + " no fue encontrado.");
    }
}
