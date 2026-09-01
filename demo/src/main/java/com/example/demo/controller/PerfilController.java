package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PerfilController {
    @Autowired private ClienteService clienteService;

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Cliente cliente = cliente(session);
        if (cliente == null) return "redirect:/login";
        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", false);
        if (session.getAttribute("perfilActualizado") != null) {
            model.addAttribute("mensaje", "Tus datos se actualizaron correctamente.");
            session.removeAttribute("perfilActualizado");
        }
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editar(HttpSession session, Model model) {
        Cliente cliente = cliente(session);
        if (cliente == null) return "redirect:/login";
        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", true);
        return "perfil";
    }

    @PostMapping("/perfil/editar")
    public String guardar(@RequestParam String correo, @RequestParam String telefono, @RequestParam String direccion,
            @RequestParam String password, HttpSession session) {
        Cliente cliente = cliente(session);
        if (cliente == null) return "redirect:/login";
        if (correo.isBlank() || telefono.isBlank() || direccion.isBlank() || password.isBlank()) return "redirect:/perfil/editar";
        cliente.setCorreo(correo.trim());
        cliente.setTelefono(telefono.trim());
        cliente.setDireccion(direccion.trim());
        cliente.setPassword(password.trim());
        clienteService.guardarCliente(cliente);
        session.setAttribute("clienteActivo", cliente);
        session.setAttribute("perfilActualizado", true);
        return "redirect:/perfil";
    }

    @PostMapping("/perfil/eliminar")
    public String eliminar(HttpSession session) {
        Cliente cliente = cliente(session);
        if (cliente != null && cliente.getIdCliente() != null) clienteService.eliminarCliente(cliente.getIdCliente());
        session.invalidate();
        return "redirect:/home";
    }

    private Cliente cliente(HttpSession session) { return (Cliente) session.getAttribute("clienteActivo"); }
}
