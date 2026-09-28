package com.example.demo.errors;

public class AdicionalNotFoundException extends RuntimeException {

    public AdicionalNotFoundException(Long idAdicional) {
        super("El adicional con ID " + idAdicional + " no fue encontrado.");
    }
}
