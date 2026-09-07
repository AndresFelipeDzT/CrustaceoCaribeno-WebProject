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

        // Validar campos vacíos
        if (cliente.getNombre() == null || cliente.getNombre().isBlank() ||
            cliente.getCorreo() == null || cliente.getCorreo().isBlank() ||
            cliente.getDireccion() == null || cliente.getDireccion().isBlank() ||
            cliente.getPassword() == null || cliente.getPassword().isBlank()) {
            
            model.addAttribute("error", "Por favor completa todos los campos obligatorios.");
            return "registro";
        }

        // Si el correo ya existe, ClienteAlreadyExistsException se manejara en GlobalExceptionHandler y se mostrará la vista error.html.
        Cliente clienteGuardado = clienteService.guardarCliente(cliente);
        return "redirect:/comidas/tarjetas?id=" + clienteGuardado.getIdCliente();
    }
}
