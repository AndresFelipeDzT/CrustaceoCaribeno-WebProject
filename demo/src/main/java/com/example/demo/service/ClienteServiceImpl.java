package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Pedido;
import com.example.demo.errors.ClienteAlreadyExistsException;
import com.example.demo.errors.ClienteNotFoundException;
import com.example.demo.repository.ClienteFakeRepository;
import com.example.demo.repository.PedidoRepository;


/**
 * Implementación de la lógica de negocio para Clientes.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    ClienteFakeRepository clienteRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(Long idCliente) {
        if (idCliente == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }
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
        Cliente cliente = obtenerClientePorId(id);

        if (!cliente.isActivo()) {
            throw new IllegalArgumentException("La cuenta ya fue eliminada.");
        }

        List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);
        boolean tienePedidoEnCurso = pedidos.stream()
                .anyMatch(pedido -> !pedidoFinalizado(pedido));

        if (tienePedidoEnCurso) {
            throw new IllegalArgumentException(
                    "No puedes eliminar tu cuenta mientras tengas pedidos en preparación, en camino o pendientes.");
        }

        // No se elimina la fila: los pedidos entregados o cancelados conservan su cliente.
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private boolean pedidoFinalizado(Pedido pedido) {
        if (pedido.getEstado() == null) {
            return false;
        }

        return "Entregado".equalsIgnoreCase(pedido.getEstado().trim())
                || "Cancelado".equalsIgnoreCase(pedido.getEstado().trim());
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
    public void actualizarCliente(Long idCliente, Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Los datos del cliente no pueden ser nulos.");
        }
        if (idCliente == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }

        Cliente clienteExistente = obtenerClientePorId(idCliente);
        cliente.setIdCliente(idCliente);
        cliente.setActivo(clienteExistente.isActivo());

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            cliente.setNombre(clienteExistente.getNombre());
        }
        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            cliente.setApellido(clienteExistente.getApellido());
        }
        if (cliente.getPassword() == null || cliente.getPassword().isBlank()) {
            cliente.setPassword(clienteExistente.getPassword());
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()) {
            cliente.setTelefono(clienteExistente.getTelefono());
        }
        if (cliente.getDireccion() == null || cliente.getDireccion().isBlank()) {
            cliente.setDireccion(clienteExistente.getDireccion());
        }

        guardarCliente(cliente);
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
            if (cliente.isActivo() && password.equals(cliente.getPassword())) {
                return cliente;
            }
        }
        return null;
    }
}
