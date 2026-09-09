package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;

@Controller
public class RegistroController {

    private final ClienteService clienteService;

    @Autowired
    public RegistroController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("cliente") Cliente cliente, Model model) {
        try {
            // Las validaciones de campos, formato de correo y mínimo de teléfono se realizan en el servicio
            Cliente clienteGuardado = clienteService.guardarCliente(cliente);
            return "redirect:/comidas/tarjetas?id=" + clienteGuardado.getIdCliente();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("cliente", cliente);
            return "registro";
        }
    }
}
