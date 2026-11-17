package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ItemPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Role;
import com.example.demo.service.ClienteService;
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

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar(
            @RequestParam(defaultValue = "false") boolean soloActivos,
            @RequestParam(required = false) Long clienteId,
            Authentication autenticacion) {
        if (tieneRol(autenticacion, Role.CLIENTE)) {
            Long idAutenticado = clienteService.obtenerClienteDTOPorCorreo(autenticacion.getName())
                    .map(ClienteDTO::getIdCliente)
                    .orElse(null);
            if (idAutenticado == null || (clienteId != null && !idAutenticado.equals(clienteId))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(pedidoService.obtenerPedidosDTO(soloActivos, idAutenticado));
        }
        boolean filtrarActivos = soloActivos || tieneRol(autenticacion, Role.OPERADOR);
        return ResponseEntity.ok(pedidoService.obtenerPedidosDTO(filtrarActivos, clienteId));
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
    public void actualizarEstado(@PathVariable Long id, @RequestBody Pedido pedido, Authentication autenticacion) {
        pedidoService.actualizarEstadoPedido(id, pedido, tieneRol(autenticacion, Role.OPERADOR));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
    }

    private boolean tieneRol(Authentication autenticacion, String rol) {
        return autenticacion.getAuthorities().stream().anyMatch(autoridad -> rol.equals(autoridad.getAuthority()));
    }

}
