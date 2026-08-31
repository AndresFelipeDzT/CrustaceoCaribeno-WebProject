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
 * Controlador para la funcionalidad de Login de clientes.
 * Valida contra el repositorio de clientes.
 */
@Controller
public class LoginController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Muestra la pantalla de inicio de sesión (/login).
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    /**
     * Procesa las credenciales ingresadas en el formulario autenticando con ClienteService.
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

        // Autenticar contra el repositorio falso de clientes
        Cliente cliente = clienteService.autenticar(nombre, password);

        if (cliente == null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "login";
        }

        // Guarda el usuario en la sesión HTTP para identificarlo en la navegación
        session.setAttribute("usuarioLogueado", cliente.getNombreCompleto());
        session.setAttribute("clienteActivo", cliente);

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
