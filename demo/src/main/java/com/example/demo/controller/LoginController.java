package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para la funcionalidad de Login de clientes.
 */
@Controller
public class LoginController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("nombre") String nombre,
            @RequestParam("password") String password,
            HttpServletResponse response,
            Model model) {

        if (nombre == null || nombre.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Por favor ingresa tu nombre y contraseña.");
            return "login";
        }

        // Autenticar contra el servicio
        Cliente cliente = clienteService.autenticar(nombre, password);

        if (cliente == null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "login";
        }

        // Guardar clienteId en Cookie
        Cookie cookie = new Cookie("clienteId", cliente.getIdCliente().toString());
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/comidas/tarjetas";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("clienteId")) {
                    c.setValue("");
                    c.setPath("/");
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
        }
        return "redirect:/home";
    }
}