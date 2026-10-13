package com.example.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Pedido;
import com.example.demo.service.CarritoService;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "http://localhost:4200")
public class CarritoApiController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{idCliente}")
    public Carrito obtener(@PathVariable Long idCliente) {
        return carritoService.obtenerCarrito(idCliente);
    }

    @PostMapping("/{idCliente}/items")
    public void agregarProducto(@PathVariable Long idCliente, @RequestBody AgregarProductoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos del producto no pueden ser nulos.");
        }
        carritoService.agregarProducto(idCliente, request.idProducto(), request.idsAdicionales());
    }

    @PutMapping("/{idCliente}/items/{idItemCarrito}/cantidad")
    public void actualizarCantidad(
            @PathVariable Long idCliente,
            @PathVariable Long idItemCarrito,
            @RequestBody ActualizarCantidadRequest request) {
        if (request == null || request.cantidad() == null) {
            throw new IllegalArgumentException("La cantidad es obligatoria.");
        }
        carritoService.actualizarCantidad(idCliente, idItemCarrito, request.cantidad());
    }

    @PutMapping("/{idCliente}/items/{idItemCarrito}/adicionales")
    public void actualizarAdicionales(
            @PathVariable Long idCliente,
            @PathVariable Long idItemCarrito,
            @RequestBody ActualizarAdicionalesRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos de los adicionales no pueden ser nulos.");
        }
        carritoService.actualizarAdicionales(idCliente, idItemCarrito, request.idsAdicionales());
    }

    @DeleteMapping("/{idCliente}/items/{idItemCarrito}")
    public void eliminarItem(@PathVariable Long idCliente, @PathVariable Long idItemCarrito) {
        carritoService.eliminarItem(idCliente, idItemCarrito);
    }

    @PostMapping("/{idCliente}/confirmar")
    public Pedido confirmarPedido(@PathVariable Long idCliente) {
        return carritoService.confirmarPedido(idCliente);
    }

    public record AgregarProductoRequest(Long idProducto, Long[] idsAdicionales) {
    }

    public record ActualizarCantidadRequest(Integer cantidad) {
    }

    public record ActualizarAdicionalesRequest(Long[] idsAdicionales) {
    }
}
