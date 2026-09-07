package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Cliente;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.errors.ClienteAlreadyExistsException;
import com.example.demo.errors.ClienteNotFoundException;
import com.example.demo.repository.ClienteFakeRepository;


/**
 * Implementación de la lógica de negocio para Clientes.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    ClienteFakeRepository clienteRepository;

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(Long idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ClienteNotFoundException(idCliente));
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        // Usamos el método existeCorreo que ya tienes implementado
        if (existeCorreo(cliente.getCorreo())) {
            throw new ClienteAlreadyExistsException(cliente.getCorreo());
        }
        
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente autenticar(String nombreOCorreo, String password) {
    if (nombreOCorreo == null || password == null) {
        return null;
    }

    Optional<Cliente> clienteOpt = clienteRepository.findByNombre(nombreOCorreo);
    if (clienteOpt.isEmpty()) {
        clienteOpt = clienteRepository.findByCorreo(nombreOCorreo);
    }

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
