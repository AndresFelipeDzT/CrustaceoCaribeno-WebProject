package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entitys.Pedido;
import com.example.demo.service.PedidoService;

@Controller
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.obtenerTodosLosPedidos());
        return "pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes atributos) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido == null) {
            atributos.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/pedidos";
        }
        model.addAttribute("pedido", pedido);
        return "pedido-detalle";
    }

    @PostMapping("/pedidos/{id}/actualizar")
    public String actualizarPedido(@PathVariable Long id, @RequestParam String estado,
            RedirectAttributes atributos) {
        Pedido pedido = pedidoService.actualizarEstadoPedido(id, estado);
        if (pedido == null) {
            atributos.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/pedidos";
        }
        atributos.addFlashAttribute("mensaje", "Pedido actualizado correctamente.");
        return "redirect:/pedidos/" + id;
    }

    @PostMapping("/pedidos/{id}/eliminar")
    public String eliminarPedido(@PathVariable Long id, RedirectAttributes atributos) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido == null) {
            atributos.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/pedidos";
        }
        pedidoService.eliminarPedido(id);
        atributos.addFlashAttribute("mensaje", "Pedido eliminado correctamente.");
        return "redirect:/pedidos";
    }

}
