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

    private final ClienteService clienteService;

    @Autowired
    public PerfilController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

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

        if (clienteForm.getNombre() == null || clienteForm.getNombre().isBlank()) {
            clienteForm.setNombre(clienteExistente.getNombre());
        }
        if (clienteForm.getApellido() == null || clienteForm.getApellido().isBlank()) {
            clienteForm.setApellido(clienteExistente.getApellido());
        }
        if (clienteForm.getPassword() == null || clienteForm.getPassword().isBlank()) {
            clienteForm.setPassword(clienteExistente.getPassword());
        }
        if (clienteForm.getTelefono() == null || clienteForm.getTelefono().isBlank()) {
            clienteForm.setTelefono(clienteExistente.getTelefono());
        }
        if (clienteForm.getDireccion() == null || clienteForm.getDireccion().isBlank()) {
            clienteForm.setDireccion(clienteExistente.getDireccion());
        }

        try {
            // Se pasa el objeto completo al servicio validando formato de correo y teléfono
            clienteService.guardarCliente(clienteForm);
            redirectAttributes.addFlashAttribute("mensaje", "Tus datos se actualizaron correctamente.");
            return "redirect:/perfil?id=" + clienteForm.getIdCliente();
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/perfil/editar?id=" + clienteForm.getIdCliente();
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