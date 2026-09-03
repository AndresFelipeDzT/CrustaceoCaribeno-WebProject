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

import java.util.Optional;

@Controller
public class PerfilController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/perfil")
    public String perfil(@RequestParam("id") Integer id, Model model) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) return "redirect:/login";

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", false);
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editar(@RequestParam("id") Integer id, Model model) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) return "redirect:/login";

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", true);
        return "perfil";
    }

    // Recibe todo el objeto Cliente mediante @ModelAttribute
    @PostMapping("/perfil/editar")
    public String guardar(@ModelAttribute("cliente") Cliente clienteForm, RedirectAttributes redirectAttributes) {

        if (clienteForm.getIdCliente() == null) {
            return "redirect:/login";
        }

        Cliente clienteExistente = clienteService.obtenerClientePorId(clienteForm.getIdCliente());
        if (clienteExistente == null) {
            return "redirect:/login";
        }

        // Actualizar datos del cliente persistido
        Cliente clienteBD = clienteExistente;
        clienteBD.setNombreCompleto(clienteForm.getNombreCompleto().trim());
        clienteBD.setCorreo(clienteForm.getCorreo().trim());
        clienteBD.setTelefono(clienteForm.getTelefono() != null ? clienteForm.getTelefono().trim() : "");
        clienteBD.setDireccion(clienteForm.getDireccion().trim());
        clienteBD.setPassword(clienteForm.getPassword().trim());

        clienteService.guardarCliente(clienteBD);

        redirectAttributes.addFlashAttribute("mensaje", "Tus datos se actualizaron correctamente.");
        return "redirect:/perfil?id=" + clienteBD.getIdCliente();
    }

    @PostMapping("/perfil/eliminar")
    public String eliminar(@RequestParam("id") Integer id) {
        clienteService.eliminarCliente(id);
        return "redirect:/home";
    }
}