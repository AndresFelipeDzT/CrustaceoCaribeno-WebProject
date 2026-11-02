package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ClienteDTO>> listar() {
        return new ResponseEntity<>(clienteService.obtenerTodosLosClientesDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtener(@PathVariable Long id) {
        return new ResponseEntity<>(clienteService.obtenerClienteDTOPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        clienteService.actualizarCliente(id, cliente);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<ClienteDTO> login(@RequestBody Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Las credenciales son obligatorias.");
        }
        return new ResponseEntity<>(
                clienteService.loginDTO(cliente.getCorreo(), cliente.getPassword()),
                HttpStatus.OK);
    }
}
