package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class PerfilController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/perfil")
    public String perfil(@RequestParam(value = "id", required = false) Integer id, Model model,
            HttpSession session) {
        if (id == null) {
            id = (Integer) session.getAttribute("clienteId");
        }
        if (id == null) return "redirect:/login";
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) return "redirect:/login";

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", false);
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editar(@RequestParam(value = "id", required = false) Integer id, Model model,
            HttpSession session) {
        if (id == null) {
            id = (Integer) session.getAttribute("clienteId");
        }
        if (id == null) return "redirect:/login";
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) return "redirect:/login";

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", true);
        return "perfil";
    }

    // Recibe todo el objeto Cliente mediante @ModelAttribute
    @PostMapping("/perfil/editar")
    public String guardar(@RequestParam(value = "id", required = false) Integer id,
            @ModelAttribute("cliente") Cliente clienteForm, RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (id == null) {
            id = (Integer) session.getAttribute("clienteId");
        }
        if (id == null) {
            return "redirect:/login";
        }

        Cliente clienteExistente = clienteService.obtenerClientePorId(id);
        if (clienteExistente == null) {
            return "redirect:/login";
        }

        // Actualizar datos del cliente persistido
        Cliente clienteBD = clienteExistente;
        if (clienteForm.getNombreCompleto() != null) {
            clienteBD.setNombreCompleto(clienteForm.getNombreCompleto().trim());
        }
        if (clienteForm.getCorreo() != null) {
            clienteBD.setCorreo(clienteForm.getCorreo().trim());
        }
        if (clienteForm.getTelefono() != null) {
            clienteBD.setTelefono(clienteForm.getTelefono().trim());
        }
        if (clienteForm.getDireccion() != null) {
            clienteBD.setDireccion(clienteForm.getDireccion().trim());
        }
        if (clienteForm.getPassword() != null) {
            clienteBD.setPassword(clienteForm.getPassword().trim());
        }

        clienteService.guardarCliente(clienteBD);
        session.setAttribute("clienteId", clienteBD.getIdCliente());

        redirectAttributes.addFlashAttribute("mensaje", "Tus datos se actualizaron correctamente.");
        return "redirect:/perfil?id=" + clienteBD.getIdCliente();
    }

    @PostMapping("/perfil/eliminar")
    public String eliminar(@RequestParam(value = "id", required = false) Integer id, HttpSession session) {
        if (id == null) {
            id = (Integer) session.getAttribute("clienteId");
        }
        if (id == null) {
            return "redirect:/login";
        }

        clienteService.eliminarCliente(id);
        session.invalidate();
        return "redirect:/home";
    }
}