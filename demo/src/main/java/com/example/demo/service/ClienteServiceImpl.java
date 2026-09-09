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
        validarCliente(cliente);

        Optional<Cliente> existente = clienteRepository.findByCorreo(cliente.getCorreo().trim());
        if (existente.isPresent() && (cliente.getIdCliente() == null || !existente.get().getIdCliente().equals(cliente.getIdCliente()))) {
            throw new ClienteAlreadyExistsException(cliente.getCorreo().trim());
        }
        
        // Formatear campos limpios
        cliente.setCorreo(cliente.getCorreo().trim());
        if (cliente.getNombre() != null) {
            cliente.setNombre(cliente.getNombre().trim());
        }
        if (cliente.getTelefono() != null) {
            cliente.setTelefono(cliente.getTelefono().trim());
        }
        if (cliente.getDireccion() != null) {
            cliente.setDireccion(cliente.getDireccion().trim());
        }

        return clienteRepository.save(cliente);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Los datos del cliente no pueden ser nulos.");
        }

        // Validación de nombre (mismo criterio que JS: nombre.length < 2)
        if (cliente.getNombre() == null || cliente.getNombre().trim().length() < 2) {
            throw new IllegalArgumentException("Por favor ingresa tu nombre completo (mínimo 2 caracteres).");
        }

        // Validación de correo y formato (mismo criterio que JS: regex email)
        if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        String regexCorreo = "^[^\\s@]+@[^\\s@]+$";
        if (!cliente.getCorreo().trim().matches(regexCorreo)) {
            throw new IllegalArgumentException("El correo no es un correo válido.");
        }

        // Validación de teléfono (mismo criterio que JS: telefono.length < 7)
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().length() < 7) {
            throw new IllegalArgumentException("Por favor ingresa un número de teléfono válido (mínimo 7 dígitos).");
        }

        // Validación de dirección (mismo criterio que JS: direccion.length < 5)
        if (cliente.getDireccion() == null || cliente.getDireccion().trim().length() < 5) {
            throw new IllegalArgumentException("Por favor ingresa una dirección válida (mínimo 5 caracteres).");
        }

        // Validación de contraseña (mismo criterio que JS: password.length < 4)
        if (cliente.getPassword() == null || cliente.getPassword().trim().length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente login(String correo, String password) {
        // Validaciones del lado del servidor equivalentes al JavaScript de login
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu correo electrónico.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu contraseña.");
        }
        if (password.trim().length() < 3) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 3 caracteres.");
        }

        Cliente cliente = autenticar(correo, password);
        if (cliente == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
        }
        return cliente;
    }

    @Override
    public Cliente autenticar(String correoONombre, String password) {
        if (correoONombre == null || password == null) {
            return null;
        }

        String identificador = correoONombre.trim();
        Optional<Cliente> clienteOpt = clienteRepository.findByCorreo(identificador);
        if (clienteOpt.isEmpty()) {
            clienteOpt = clienteRepository.findByNombre(identificador);
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
