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

import com.example.demo.dto.ClienteDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;

/**
 * Controlador REST para las operaciones de cliente.
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteApiController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteService.obtenerTodosLosClientesDTO();
    }

    @GetMapping("/{id}")
    public ClienteDTO obtener(@PathVariable Long id) {
        return clienteService.obtenerClienteDTOPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Cliente cliente) {
        clienteService.guardarCliente(cliente);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        clienteService.actualizarCliente(id, cliente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    @PostMapping("/login")
    public ClienteDTO login(@RequestBody Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Las credenciales son obligatorias.");
        }
        return clienteService.loginDTO(cliente.getCorreo(), cliente.getPassword());
    }
}
