package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador para la funcionalidad de Registro (Signup) de clientes.
 * Guarda al nuevo cliente en el repositorio de clientes.
 */
@Controller
public class RegistroController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Muestra la pantalla de registro (/registro).
     */
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    /**
     * Procesa el formulario de registro y almacena el nuevo cliente.
     */
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        // Validación básica de campos obligatorios
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            direccion == null || direccion.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            model.addAttribute("error", "Por favor completa todos los campos obligatorios.");
            return "registro";
        }

        // Crear y guardar el cliente en el servicio
        Cliente nuevoCliente = new Cliente(
            null,
            nombreCompleto.trim(),
            correo.trim(),
            telefono != null ? telefono.trim() : "",
            direccion.trim(),
            password.trim()
        );

        Cliente clienteGuardado = clienteService.guardarCliente(nuevoCliente);

        // Guardamos el usuario en sesión y redirigimos al menú
        session.setAttribute("usuarioLogueado", clienteGuardado.getNombreCompleto());
        session.setAttribute("clienteActivo", clienteGuardado);

        return "redirect:/comidas/tarjetas";
    }
}
