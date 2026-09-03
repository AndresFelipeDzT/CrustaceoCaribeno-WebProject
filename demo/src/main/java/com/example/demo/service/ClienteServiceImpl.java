package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Cliente;
import com.example.demo.repository.ClienteFakeRepository;

/**
 * Implementación de la lógica de negocio para Clientes.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteFakeRepository clienteRepository;

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(Integer id) {
        if (id == null) {
            return null;
        }
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        return clienteRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public boolean eliminarCliente(Integer id) {
        if (id == null) {
            return false;
        }
        return clienteRepository.deleteById(id);
    }

    @Override
    public Cliente autenticar(String nombreOCorreo, String password) {
        if (nombreOCorreo == null || password == null) {
            return null;
        }

        Optional<Cliente> clienteOpt = clienteRepository.findByNombreOrCorreo(nombreOCorreo);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (password.equals(cliente.getPassword())) {
                return cliente;
            }
        }
        return null;
    }
}
