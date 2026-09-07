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

    private final ClienteFakeRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteFakeRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(int id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public boolean eliminarCliente(int id) {
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

    @Override
    public boolean existeCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        return clienteRepository.findByCorreo(correo).isPresent();
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return Optional.empty();
        }
        return clienteRepository.findByCorreo(correo);
    }
}
