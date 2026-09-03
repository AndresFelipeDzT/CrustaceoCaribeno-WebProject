package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegistroController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("cliente") Cliente cliente, HttpServletResponse response, Model model) {

        // Validar campos vacíos
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().isBlank() ||
            cliente.getCorreo() == null || cliente.getCorreo().isBlank() ||
            cliente.getDireccion() == null || cliente.getDireccion().isBlank() ||
            cliente.getPassword() == null || cliente.getPassword().isBlank()) {
            
            model.addAttribute("error", "Por favor completa todos los campos obligatorios.");
            return "registro";
        }

        // Validar si el correo ya se encuentra registrado
        if (clienteService.buscarPorCorreo(cliente.getCorreo().trim()) != null) {
            model.addAttribute("error", "Este correo electrónico ya se encuentra registrado.");
            return "registro";
        }

        // Normalizar datos
        cliente.setNombreCompleto(cliente.getNombreCompleto().trim());
        cliente.setCorreo(cliente.getCorreo().trim());
        cliente.setTelefono(cliente.getTelefono() != null ? cliente.getTelefono().trim() : "");
        cliente.setDireccion(cliente.getDireccion().trim());
        cliente.setPassword(cliente.getPassword().trim());

        // Guardar cliente
        Cliente clienteGuardado = clienteService.guardarCliente(cliente);

        // Guardar en Cookie
        Cookie cookie = new Cookie("clienteId", clienteGuardado.getIdCliente().toString());
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/comidas/tarjetas";
    }
}