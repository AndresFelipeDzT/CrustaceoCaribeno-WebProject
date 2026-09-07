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

@Controller
public class PerfilController {

    private final ClienteService clienteService;

    @Autowired
    public PerfilController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/perfil")
    public String perfil(@RequestParam(value = "id", required = false) Integer id, Model model) {
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

    @GetMapping("/perfil/editar")
    public String editar(@RequestParam(value = "id", required = false) Integer id, Model model) {
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
    @PostMapping("/perfil/editar")
    public String guardar(@RequestParam(value = "id", required = false) Integer id,
            @ModelAttribute("cliente") Cliente clienteForm, RedirectAttributes redirectAttributes) {

        if (clienteForm.getIdCliente() == null && id != null) {
            clienteForm.setIdCliente(id);
        }

        if (clienteForm.getIdCliente() == null) {
            return "redirect:/login";
        }

        Cliente clienteExistente = clienteService.obtenerClientePorId(clienteForm.getIdCliente());
        if (clienteExistente == null) {
            return "redirect:/login";
        }

        if (clienteForm.getNombreCompleto() == null || clienteForm.getNombreCompleto().isBlank()) {
            clienteForm.setNombreCompleto(clienteExistente.getNombreCompleto());
        }

        // Se pasa el objeto completo al servicio sin mutar atributo por atributo
        clienteService.guardarCliente(clienteForm);

        redirectAttributes.addFlashAttribute("mensaje", "Tus datos se actualizaron correctamente.");
        return "redirect:/perfil?id=" + clienteForm.getIdCliente();
    }

    @PostMapping("/perfil/eliminar")
    public String eliminar(@RequestParam(value = "id", required = false) Integer id) {
        if (id == null) {
            return "redirect:/login";
        }

        clienteService.eliminarCliente(id);
        return "redirect:/home";
    }
}