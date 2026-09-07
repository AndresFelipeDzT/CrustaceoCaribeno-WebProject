package com.example.demo.errors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNotFoundException.class)
    public String handleProductoNotFound(ProductoNotFoundException ex, Model model) {
        model.addAttribute("titulo", "Producto no encontrado");
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public String handleClienteNotFound(ClienteNotFoundException ex, Model model) {
        model.addAttribute("titulo", "Cliente no encontrado");
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ClienteAlreadyExistsException.class)
    public String handleClienteAlreadyExists(ClienteAlreadyExistsException ex, Model model) {
        model.addAttribute("titulo", "Error en el registro");
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("titulo", "Ocurrió un error en El Crustáceo Caribeño");
        model.addAttribute("mensaje", "Ha ocurrido un problema inesperado: " + ex.getMessage());
        return "error";
    }
}
