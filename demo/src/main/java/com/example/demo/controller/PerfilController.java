package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    ClienteService clienteService;

    @GetMapping({"", "/"})
    public String perfil(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/login";
        }
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", false);
        return "perfil";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/login";
        }
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", true);
        return "perfil";
    }

    // Recibe todo el objeto Cliente mediante @ModelAttribute y lo guarda completo
    @PostMapping("/editar")
    public String guardar(@RequestParam(value = "id", required = false) Long id,
            @ModelAttribute("cliente") Cliente clienteForm, Model model,
            RedirectAttributes redirectAttributes) {

        if (id == null) {
            return "redirect:/login";
        }

        clienteForm = clienteService.prepararClienteEdicion(clienteForm, id);
        if (clienteForm == null) {
            return "redirect:/login";
        }

        try {
            clienteService.guardarCliente(clienteForm);
            redirectAttributes.addFlashAttribute("mensaje", "Tus datos se actualizaron correctamente.");
            return "redirect:/perfil?id=" + clienteForm.getIdCliente();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("cliente", clienteForm);
            model.addAttribute("modoEdicion", true);
            return "perfil";
        }
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            return "redirect:/login";
        }

        clienteService.eliminarCliente(id);
        return "redirect:/home";
    }
}