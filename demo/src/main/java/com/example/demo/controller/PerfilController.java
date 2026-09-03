package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController {

    @Autowired
    private ClienteService clienteService;

    private Integer obtenerIdCliente(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("clienteId".equals(c.getName())) {
                    try {
                        return Integer.parseInt(c.getValue());
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return null;
    }

    @GetMapping("/perfil")
    public String perfil(HttpServletRequest request, Model model) {
        Integer clienteId = obtenerIdCliente(request);
        if (clienteId == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", false);
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editar(HttpServletRequest request, Model model) {
        Integer clienteId = obtenerIdCliente(request);
        if (clienteId == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", true);
        return "perfil";
    }

    @PostMapping("/perfil/editar")
    public String guardar(@ModelAttribute("cliente") Cliente cliente, RedirectAttributes redirectAttributes) {
        if (cliente == null || cliente.getIdCliente() == null) {
            return "redirect:/login";
        }

        clienteService.guardarCliente(cliente);

        redirectAttributes.addFlashAttribute("mensaje", "Tus datos se actualizaron correctamente.");
        return "redirect:/perfil";
    }

    @PostMapping("/perfil/eliminar")
    public String eliminar(HttpServletRequest request, HttpServletResponse response) {
        Integer clienteId = obtenerIdCliente(request);
        if (clienteId != null) {
            clienteService.eliminarCliente(clienteId);

            Cookie c = new Cookie("clienteId", "");
            c.setPath("/");
            c.setMaxAge(0);
            response.addCookie(c);
        }
        return "redirect:/home";
    }
}