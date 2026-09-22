package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.CarritoService;
import com.example.demo.service.ClienteService;

@Controller
public class CarritoController {
    private final CarritoService carritoService;
    private final ClienteService clienteService;

    public CarritoController(CarritoService carritoService, ClienteService clienteService) {
        this.carritoService = carritoService;
        this.clienteService = clienteService;
    }

    @GetMapping("/carrito")
    public String verCarrito(@RequestParam Long clienteId, Model model) {
        model.addAttribute("cliente", clienteService.obtenerClientePorId(clienteId));
        model.addAttribute("carrito", carritoService.obtenerCarrito(clienteId));
        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregar(@RequestParam Long clienteId, @RequestParam Long productoId,
            @RequestParam(required = false) Long[] adicionales) {
        carritoService.agregarProducto(clienteId, productoId, adicionales);
        return "redirect:/carrito?clienteId=" + clienteId;
    }

    @PostMapping("/carrito/cantidad")
    public String actualizarCantidad(@RequestParam Long clienteId, @RequestParam Long itemId,
            @RequestParam int cantidad, RedirectAttributes atributos) {
        try {
            carritoService.actualizarCantidad(clienteId, itemId, cantidad);
        } catch (IllegalArgumentException ex) {
            atributos.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/carrito?clienteId=" + clienteId;
    }

    @PostMapping("/carrito/adicionales")
    public String actualizarAdicionales(@RequestParam Long clienteId, @RequestParam Long itemId,
            @RequestParam(required = false) Long[] adicionales, RedirectAttributes atributos) {
        try {
            carritoService.actualizarAdicionales(clienteId, itemId, adicionales);
        } catch (IllegalArgumentException ex) {
            atributos.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/carrito?clienteId=" + clienteId;
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarItem(@RequestParam Long clienteId, @RequestParam Long itemId,
            RedirectAttributes atributos) {
        try {
            carritoService.eliminarItem(clienteId, itemId);
        } catch (IllegalArgumentException ex) {
            atributos.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/carrito?clienteId=" + clienteId;
    }

    @PostMapping("/carrito/confirmar")
    public String confirmar(@RequestParam Long clienteId, RedirectAttributes atributos) {
        try {
            atributos.addFlashAttribute("mensaje", "Pedido confirmado correctamente.");
            carritoService.confirmarPedido(clienteId);
        } catch (IllegalArgumentException ex) {
            atributos.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/carrito?clienteId=" + clienteId;
    }
}
