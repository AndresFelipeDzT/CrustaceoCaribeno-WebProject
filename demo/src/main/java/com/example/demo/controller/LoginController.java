package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;

/**
 * Controlador para la funcionalidad de Login de clientes.
 */
@Controller
public class LoginController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String cerrarSesion() {
        return "redirect:/home";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "password", required = false) String password,
            Model model) {

        try {
            Cliente cliente = clienteService.login(correo, password);
            return "redirect:/comidas/tarjetas?id=" + cliente.getIdCliente();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("correo", correo);
            return "login";
        }
    }
}