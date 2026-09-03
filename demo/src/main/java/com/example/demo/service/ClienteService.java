package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entitys.Cliente;

/**
 * Interfaz que define las operaciones del servicio de clientes.
 */
public interface ClienteService {

    List<Cliente> obtenerTodosLosClientes();

    Cliente obtenerClientePorId(Integer id);

    Cliente buscarPorCorreo(String correo);

    Cliente guardarCliente(Cliente cliente);

    boolean eliminarCliente(Integer id);

    /**
     * Valida las credenciales de un cliente por su nombre o correo.
     * @param nombreOCorreo Nombre de usuario o correo.
     * @param password Contraseña.
     * @return El cliente si las credenciales son válidas, o null si no coinciden.
     */
    Cliente autenticar(String nombreOCorreo, String password);
}
