package com.example.demo.errors;

public class PedidoNotFoundException extends RuntimeException {

    public PedidoNotFoundException(Long idPedido) {
        super("El pedido con ID " + idPedido + " no fue encontrado.");
    }
}
