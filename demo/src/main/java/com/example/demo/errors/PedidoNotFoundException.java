package com.example.demo.errors;

/**
 * Excepción que informa un problema relacionado con pedido no encontrado.
 */
public class PedidoNotFoundException extends RuntimeException {

    public PedidoNotFoundException(Long idPedido) {
        super("El pedido con ID " + idPedido + " no fue encontrado.");
    }
}
