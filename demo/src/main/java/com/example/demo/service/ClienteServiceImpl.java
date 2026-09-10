package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Cliente;
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
        if (!nombreValido(cliente.getNombre())) {
            throw new IllegalArgumentException(
                    "Por favor ingresa tu nombre completo (mínimo 2 caracteres).");
        }
        if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (!correoValido(cliente.getCorreo())) {
            throw new IllegalArgumentException("El correo no es un correo válido.");
        }
        if (!telefonoValido(cliente.getTelefono())) {
            throw new IllegalArgumentException(
                    "Por favor ingresa un número de teléfono válido (solo números, mínimo 7 dígitos).");
        }
        if (!direccionValida(cliente.getDireccion())) {
            throw new IllegalArgumentException(
                    "Por favor ingresa una dirección válida (mínimo 5 caracteres).");
        }
        if (!passwordValida(cliente.getPassword())) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 4 caracteres.");
        }
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    private boolean nombreValido(String nombre) {
        return nombre != null && nombre.trim().length() >= 2;
    }

    private boolean correoValido(String correo) {
        return correo != null
                && correo.trim().matches("^[^\\s@]+@[^\\s@]+$");
    }

    private boolean telefonoValido(String telefono) {
        return telefono != null && telefono.trim().matches("[0-9]{7,}");
    }

    private boolean direccionValida(String direccion) {
        return direccion != null && direccion.trim().length() >= 5;
    }

    private boolean passwordValida(String password) {
        return password != null && password.trim().length() >= 4;
    }

    @Override
    public Cliente prepararClienteEdicion(Cliente clienteFormulario, Long idCliente) {
        if (clienteFormulario == null || idCliente == null) {
            return null;
        }

        Cliente clienteExistente = obtenerClientePorId(idCliente);
        clienteFormulario.setIdCliente(idCliente);

        if (clienteFormulario.getNombre() == null || clienteFormulario.getNombre().isBlank()) {
            clienteFormulario.setNombre(clienteExistente.getNombre());
        }
        if (clienteFormulario.getApellido() == null || clienteFormulario.getApellido().isBlank()) {
            clienteFormulario.setApellido(clienteExistente.getApellido());
        }
        if (clienteFormulario.getPassword() == null || clienteFormulario.getPassword().isBlank()) {
            clienteFormulario.setPassword(clienteExistente.getPassword());
        }
        if (clienteFormulario.getTelefono() == null || clienteFormulario.getTelefono().isBlank()) {
            clienteFormulario.setTelefono(clienteExistente.getTelefono());
        }
        if (clienteFormulario.getDireccion() == null || clienteFormulario.getDireccion().isBlank()) {
            clienteFormulario.setDireccion(clienteExistente.getDireccion());
        }

        return clienteFormulario;
    }

    @Override
    public Cliente login(String correo, String password) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu correo electrónico.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu contraseña.");
        }
        if (password.trim().length() < 3) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 3 caracteres.");
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
}
