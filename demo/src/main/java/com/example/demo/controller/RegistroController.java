package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador para la funcionalidad de Registro (Signup) de clientes.
 * Permite capturar los datos del nuevo usuario para el Sprint actual.
 */
@Controller
public class RegistroController {

    /**
     * Muestra la pantalla de registro (/registro o /signup).
     */
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    /**
     * Procesa el formulario de registro.
     */
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validación básica de campos obligatorios
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            model.addAttribute("error", "Por favor completa todos los campos obligatorios.");
            return "registro";
        }

        // Guardamos el usuario en sesión y redirigimos al menú (o al login con mensaje)
        session.setAttribute("usuarioLogueado", nombreCompleto.trim());
        session.setAttribute("correoLogueado", correo.trim());

        // Redirige al menú principal tras el registro exitoso
        return "redirect:/comidas/tarjetas";
    }
}
