package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador para la funcionalidad de Login de clientes.
 * Sprint actual: Identificación y visualización simple basada en Figma.
 */
@Controller
public class LoginController {

    /**
     * Muestra la pantalla de inicio de sesión (/login).
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    /**
     * Procesa las credenciales ingresadas en el formulario.
     */
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("nombre") String nombre,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        if (nombre == null || nombre.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Por favor ingresa tu nombre y contraseña.");
            return "login";
        }

        // Guarda el usuario en la sesión HTTP para identificarlo en la navegación
        session.setAttribute("usuarioLogueado", nombre.trim());

        // Redirige al menú del restaurante
        return "redirect:/comidas/tarjetas";
    }

    /**
     * Cierra la sesión activa.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
