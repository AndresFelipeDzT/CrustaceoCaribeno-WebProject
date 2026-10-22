package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.ItemPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Pedido;
import com.example.demo.service.PedidoService;

/**
 * Controlador REST para las operaciones de pedido.
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoApiController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoDTO> listar() {
        return pedidoService.obtenerPedidosDTO();
    }

    @GetMapping("/{id}")
    public PedidoDTO obtener(@PathVariable Long id) {
        return pedidoService.obtenerPedidoDTOPorId(id);
    }

    @GetMapping("/{id}/items")
    public List<ItemPedidoDTO> listarItems(@PathVariable Long id) {
        return pedidoService.obtenerItemsDTODePedido(id);
    }

    @GetMapping("/{idPedido}/items/{idItemPedido}/adicionales")
    public List<AdicionalDTO> listarAdicionales(
            @PathVariable Long idPedido,
            @PathVariable Long idItemPedido) {
        return pedidoService.obtenerAdicionalesDTODeItem(idPedido, idItemPedido);
    }

    // Se quita metodo post 

    @PutMapping("/{id}")
    public void actualizarEstado(@PathVariable Long id, @RequestBody Pedido pedido) {
        pedidoService.actualizarEstadoPedido(id, pedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
    }

}
